package emerge.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emerge.api.RepositoryService;
import emerge.conf.Configuration;
import emerge.ebuild.EbuildFile;
import emerge.entity.PackageDetailsInfo;
import emerge.entity.PackageName;
import emerge.exception.UserException;
import emerge.exception.InternalException;
import emerge.misc.Logger;
import emerge.repo.SearchCriteria;
import emerge.repo.entity.InstalledPackageEntry;
import emerge.system.BinUtils;
import emerge.util.ServiceLocator;

public class RepositoryServiceImpl implements RepositoryService {

	@Override
	public List<PackageDetailsInfo> find(PackageName userInput) throws InternalException, UserException {
		List<PackageDetailsInfo> retVal = new ArrayList<PackageDetailsInfo>();
		try {
			retVal = findQuiet(userInput, SearchCriteria.CONTAINS_NAME_ANY_VERSION);

			if (retVal.isEmpty()) {
				// Logger.user("package: " + userInput + " not found");
			} else {
				for (PackageDetailsInfo entry : retVal) {
					Logger.user(entry.toString());
				}
			}
			Logger.user("Found " + retVal.size() + " matches.");
		} catch (Exception e) {
			Logger.user("Internal error, can not find package " + userInput);
		}
		return retVal;
	}

	@Override
	public void sync() throws InternalException, UserException {
		try {
			Logger.user(">>> Sync with central repository");
			// TODO: download ebuild repo, clear local repo, install to local
			String uri = "http://github.com/jameskirk/emerge-repository/archive/master.zip";
			String ebuildRepoDir = Configuration.ebuildRepositoryDir;
			Logger.user(">>> Downloading files");
			BinUtils.wget(uri, ebuildRepoDir + "/master.zip",
					ServiceLocator.getInstance().getUserSettingReader().read().getProxy()).exec();

			// remove only dirs
			BinUtils.rmForce(ebuildRepoDir + "/*/").exec();

			BinUtils.unarchive(ebuildRepoDir + "/master.zip", ebuildRepoDir).exec();

			BinUtils.mv(ebuildRepoDir + "/emerge-repository-master/*", ebuildRepoDir).exec();

			BinUtils.rmForce(ebuildRepoDir + "/emerge-repository-master").exec();

			ServiceLocator.getInstance().getEbuildRepositoryReader().cache();
			ServiceLocator.getInstance().getInstalledRepositoryReader().cache();
			Logger.user("Synchronization successful");
		} catch (Exception e) {
			Logger.user("Synchronization failed");
			throw new InternalException(e);
		}

	}

	@Override
	public void firstInit() throws InternalException, UserException {
		try {
			Logger.user(">>> First initialization");
			ServiceLocator.getInstance().getEbuildRepositoryReader().cache();
			ServiceLocator.getInstance().getInstalledRepositoryReader().cache();
			Logger.user("Initialization successful");
		} catch (RuntimeException e) {
			Logger.user("Initialization failed");
			throw new InternalException(e);
		}
	}

	@Override
	public List<PackageDetailsInfo> findQuiet(PackageName packageId, SearchCriteria criteria)
			throws InternalException, UserException {
		List<PackageDetailsInfo> retVal = new ArrayList<PackageDetailsInfo>();

		if (packageId == null) {
			throw new UserException("entered package name :" + packageId
					+ " is incorrect. Please enter package name in correct format");
		}

		if (criteria == SearchCriteria.CONTAINS_NAME_ANY_VERSION) {
			// packageId.setVersion("");
		} else if (criteria == SearchCriteria.EXACTLY_PACKAGE_ID) {
			// packageId.setVersion("");
		}
		List<InstalledPackageEntry> entries = ServiceLocator.getInstance().getInstalledRepositoryReader()
				.readByCriteria(packageId.toString(), criteria);
		List<EbuildFile> ebuilds = ServiceLocator.getInstance().getEbuildRepositoryReader()
				.readByCriteria(packageId.toString(), criteria);

		for (InstalledPackageEntry entry : entries) {

			List<EbuildFile> ebuildInInfo = new ArrayList<EbuildFile>();
			List<EbuildFile> toRemove = new ArrayList<EbuildFile>();
			for (EbuildFile e : ebuilds) {
				if (entry.getPackageId().getCategory().equals(e.getPackageId().getCategory())
						&& entry.getPackageId().getPackageName().equals(e.getPackageId().getPackageName())) {
					toRemove.add(e);
					ebuildInInfo.add(e);
				}
			}
			ebuilds.removeAll(toRemove);
			PackageDetailsInfo info = new PackageDetailsInfo(entry, ebuildInInfo);
			retVal.add(info);

		}

		Map<String, List<EbuildFile>> map = new HashMap<String, List<EbuildFile>>();
		for (EbuildFile e : ebuilds) {
			String key = e.getPackageId().getCategory() + "/" + e.getPackageId().getPackageName();
			if (map.get(key) == null) {
				map.put(key, new ArrayList<EbuildFile>());
			}
			map.get(key).add(e);
		}
		for (List<EbuildFile> eList : map.values()) {
			retVal.add(new PackageDetailsInfo(null, eList));
		}
		return retVal;
	}

	@Override
	public List<PackageDetailsInfo> findByContainsName(String userInput) throws InternalException, UserException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<PackageDetailsInfo> findByContainsDescription(String userInput)
			throws InternalException, UserException {
		throw new UnsupportedOperationException();
	}

}
