package emerge.repo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import emerge.conf.Configuration;
import emerge.ebuild.EbuildFile;
import emerge.ebuild.EbuildReader;
import emerge.entity.PackageName;
import emerge.exception.InternalException;
import emerge.misc.FileHelper;
import emerge.misc.Logger;
import emerge.misc.NameDeterminer;
import emerge.repo.entity.RepositoryCacheFile;
import emerge.system.BinUtils;
import emerge.system.OsType;
import emerge.system.Path;

public class EbuildLocalRepositoryReader implements LocalRepositoryReader<EbuildFile> {

	private String ebuildRepositoryDir;

	private String cacheDbFilename = "cache.db";

	public EbuildLocalRepositoryReader() {
		ebuildRepositoryDir = Configuration.ebuildRepositoryDir;
	}

	public EbuildLocalRepositoryReader(String ebuildRepositoryDir) {
		this.ebuildRepositoryDir = ebuildRepositoryDir;
	}

	@Override
	public void cache() throws InternalException {
		RepositoryCacheFile cacheFile = initCacheFile();
		getFileReader().write(RepositoryCacheFile.class, ebuildRepositoryDir + "/" + cacheDbFilename, cacheFile);
	}

	@Override
	public EbuildFile readExactlyOne(PackageName packageId) throws InternalException {
		List<EbuildFile> retVal = readByCriteria(packageId.toString(), SearchCriteria.EXACTLY_PACKAGE_ID);
		if (retVal.size() != 1) {
			return null;
		} else {
			return retVal.get(0);
		}
	}

	/**
	 * packageId = 7zip , 7zip-4.9 , app/7zip , app/7zip-4.9
	 */
	@Override
	public List<EbuildFile> readByCriteria(String userInput, SearchCriteria criteria) throws InternalException {
		PackageName packageId = NameDeterminer.parseCategoryNameVersion(userInput);
		List<EbuildFile> retVal = new ArrayList<EbuildFile>();
		if (packageId == null) {
			throw new InternalException("packageId can not be null");
		}
		RepositoryCacheFile cacheFile = getFileReader().read(RepositoryCacheFile.class,
				ebuildRepositoryDir + "/" + cacheDbFilename);

		retVal.addAll(getEbuildFilesByCache(packageId, cacheFile, criteria));
		return retVal;
	}

	@Override
	public List<EbuildFile> readAll() throws InternalException {
		RepositoryCacheFile cacheFile = getFileReader().read(RepositoryCacheFile.class,
				ebuildRepositoryDir + "/" + cacheDbFilename);
		return getEbuildFilesByCache(new PackageName("", "", ""), cacheFile, SearchCriteria.CONTAINS_NAME_ANY_VERSION);
	}
	
	public List<EbuildFile> readAllInfo() throws InternalException {
		RepositoryCacheFile cacheFile = getFileReader().read(RepositoryCacheFile.class,
				ebuildRepositoryDir + "/" + cacheDbFilename);
		return getInfoFilesByCache(new PackageName("", "", ""), cacheFile);
	}

	@Override
	public void saveOrUpdate(EbuildFile t) throws InternalException {
		throw new UnsupportedOperationException("can not write something in ebuild repo");
	}

	@Override
	public void remove(PackageName packageId) throws InternalException {
		BinUtils.rm(ebuildRepositoryDir + FileHelper.getEbuildPath(packageId));
	}

	public Serializer getFileReader() {
		return new Serializer();
	}

	protected RepositoryCacheFile initCacheFile() {
		RepositoryCacheFile cacheFile = new RepositoryCacheFile();
		File ebuildRepoDir = new File(new Path(ebuildRepositoryDir, OsType.UNIX).getNativeValue());

		for (String category : ebuildRepoDir.list()) {
			String categoryPath = ebuildRepoDir + "/" + category;
			if (new File(categoryPath).isDirectory()) {

				for (String packageName : new File(categoryPath).list()) {
					String packageNamePath = ebuildRepoDir + "/" + category + "/" + packageName;
					if (new File(packageNamePath).exists()) {

						for (String ebuildFilename : new File(packageNamePath).list()) {
							String ebuildPath = ebuildRepoDir + "/" + category + "/" + packageName + "/"
									+ ebuildFilename;
							if (new File(ebuildPath).exists() && new File(ebuildPath).isFile()) {

								if (!cacheFile.getCategoryPackageMap().containsKey(category)) {
									cacheFile.getCategoryPackageMap().put(category, new ArrayList<String>());
								}
								if (!cacheFile.getCategoryPackageMap().get(category).contains(packageName)) {
									cacheFile.getCategoryPackageMap().get(category).add(packageName);
								}

								String version = NameDeterminer
										.parseCategoryNameVersion(ebuildFilename.replace(".ebuild", "")).getVersion();

								if (!cacheFile.getCategoryAndPackageVersionMap()
										.containsKey(category + "/" + packageName)) {
									cacheFile.getCategoryAndPackageVersionMap().put(category + "/" + packageName,
											new ArrayList<String>());
								}
								List<String> versionList = cacheFile.getCategoryAndPackageVersionMap()
										.get(category + "/" + packageName);
								versionList.add(version);

								Logger.debug("put to cache.db: " + category + "/" + packageName + "/" + ebuildFilename);

							}
						}
					}
				}
			}
		}
		return cacheFile;
	}

	protected List<EbuildFile> getEbuildFilesByCache(PackageName packageId, RepositoryCacheFile cacheFile,
			SearchCriteria criteria) throws InternalException {
		List<EbuildFile> retVal = new ArrayList<EbuildFile>();
		for (Entry<String, List<String>> entry : cacheFile.getCategoryPackageMap().entrySet()) {
			String realCategory = entry.getKey();

			// category = category
			if (packageId.getCategory().isEmpty() || packageId.getCategory().equals(realCategory)) {
				for (String realPackageName : entry.getValue()) {

					// package name = package name
					if (packageId.getPackageName().isEmpty() || (criteria == SearchCriteria.EXACTLY_PACKAGE_ID
							&& realPackageName.equals(packageId.getPackageName())
							|| criteria == SearchCriteria.CONTAINS_NAME_ANY_VERSION
									&& realPackageName.contains(packageId.getPackageName()))) {

						List<EbuildFile> availablePackages = new ArrayList<EbuildFile>();
						for (String realVersion : cacheFile.getCategoryAndPackageVersionMap()
								.get(realCategory + "/" + realPackageName)) {

							// package version = package version
							if (packageId.getVersion().isEmpty() || realVersion.equals(packageId.getVersion())) {
								availablePackages.add(new EbuildReader()
										.read(new PackageName(realCategory, realPackageName, realVersion)));
							}

						}

						retVal.addAll(availablePackages);

					}
				}
			}

		}
		return retVal;
	}
	
	protected List<EbuildFile> getInfoFilesByCache(PackageName packageId, RepositoryCacheFile cacheFile) throws InternalException {
		List<EbuildFile> retVal = new ArrayList<EbuildFile>();
		for (Entry<String, List<String>> entry : cacheFile.getCategoryPackageMap().entrySet()) {
			String realCategory = entry.getKey();

			// category = category
			if (packageId.getCategory().isEmpty() || packageId.getCategory().equals(realCategory)) {
				for (String realPackageName : entry.getValue()) {

					// package name = package name
					if (packageId.getPackageName().isEmpty() || realPackageName.contains(packageId.getPackageName())) {

						retVal.add(new EbuildReader()
								.readInfo(new PackageName(realCategory, realPackageName, "")));

					}
				}
			}

		}
		return retVal;
	}


}
