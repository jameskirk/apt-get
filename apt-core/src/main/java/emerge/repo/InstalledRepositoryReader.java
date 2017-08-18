package emerge.repo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.plaf.FileChooserUI;

import emerge.conf.Configuration;
import emerge.ebuild.EbuildFile;
import emerge.entity.PackageName;
import emerge.entity.WindowsRegistryEntry;
import emerge.exception.InternalException;
import emerge.misc.FileHelper;
import emerge.misc.Logger;
import emerge.misc.NameDeterminer;
import emerge.misc.WinRegistry;
import emerge.repo.entity.InstalledPackageEntry;
import emerge.repo.entity.InstalledPackageFile;
import emerge.system.OsType;
import emerge.system.Path;

public class InstalledRepositoryReader implements LocalRepositoryReader<InstalledPackageEntry> {

	private EbuildLocalRepositoryReader installedEbuildReader = new EbuildLocalRepositoryReader(
			Configuration.installedRepositoryDir);

	private EbuildLocalRepositoryReader centralEbuildReader = new EbuildLocalRepositoryReader();

	public InstalledRepositoryReader() {
	}

	@Override
	public void cache() throws InternalException {
		// TODO: set flag to false, its hack
		boolean forceCache = true;

		installedEbuildReader.cache();
		//List<EbuildFile> ebuildFiles = centralEbuildReader.readAll();
		List<EbuildFile> infoFiles = centralEbuildReader.readAllInfo();
		InstalledPackageFile file = new InstalledPackageFile();

		if (OsType.WINDOWS.isCurrent()) {
			List<InstalledPackageEntry> newEntries = new ArrayList<InstalledPackageEntry>();
			List<WindowsRegistryEntry> registryEntries = WinRegistry.getRegistryEntriesWindows();

			// find already installed
			initInstallPackageEntryWindows(infoFiles, registryEntries, newEntries);

			if (forceCache
					|| !new File(new Path(Configuration.installedDbPath, OsType.UNIX).getNativeValue()).exists()) {

				file.setEntries(newEntries);
				writeFile(file);

			} else {

				// installed.db exists
				List<InstalledPackageEntry> oldEntries = readAll();
				List<InstalledPackageEntry> entriesToAdd = new ArrayList<InstalledPackageEntry>();
				for (InstalledPackageEntry newEntry : newEntries) {
					boolean oldEntryFound = false;
					for (InstalledPackageEntry oldEntry : oldEntries) {
						if (newEntry.getPackageId().getCategory().equals(oldEntry.getPackageId().getCategory())
								&& newEntry.getPackageId().getPackageName()
										.equals(oldEntry.getPackageId().getPackageName())) {
							oldEntryFound = true;
						}
					}
					if (!oldEntryFound) {
						entriesToAdd.add(newEntry);
					}
				}
				oldEntries.addAll(entriesToAdd);
				file.setEntries(oldEntries);
				writeFile(file);
			}
		}

	}

	@Override
	public InstalledPackageEntry readExactlyOne(PackageName packageId) throws InternalException {
		List<InstalledPackageEntry> retVal = readByCriteria(packageId.toString(),
				SearchCriteria.CONTAINS_NAME_ANY_VERSION);
		if (retVal.size() != 1) {
			return null;
		} else {
			return retVal.get(0);
		}
	}

	@Override
	public List<InstalledPackageEntry> readByCriteria(String userInput, SearchCriteria criteria)
			throws InternalException {
		List<InstalledPackageEntry> retVal = new ArrayList<InstalledPackageEntry>();

		PackageName packageId = NameDeterminer.parseCategoryNameVersion(userInput);
		String category = packageId.getCategory();
		String packageName = packageId.getPackageName();
		String version = packageId.getVersion();
		if (criteria == SearchCriteria.CONTAINS_NAME_ANY_VERSION) {
			retVal.addAll(readAll().stream()
					.filter(x -> (category.isEmpty() || category.equals(x.getPackageId().getCategory()))
							&& (version.isEmpty() || version.equals(x.getPackageId().getVersion()))
							&& x.getPackageId().getPackageName().contains(packageName))
					.collect(Collectors.toList()));

		} else if (criteria == SearchCriteria.EXACTLY_PACKAGE_ID) {
			retVal.addAll(readAll().stream()
					.filter(x -> (category.isEmpty() || category.equals(x.getPackageId().getCategory()))
							&& (version.isEmpty() || version.equals(x.getPackageId().getVersion()))
							&& x.getPackageId().getPackageName().equals(packageName))
					.collect(Collectors.toList()));
		} else if (criteria == SearchCriteria.EXACTLY_NAME_ANY_VERSION) {
			retVal.addAll(readAll().stream()
					.filter(x -> (category.isEmpty() || category.equals(x.getPackageId().getCategory()))
							// && (version.isEmpty() ||
							// version.equals(x.getPackageId().getVersion()))
							&& x.getPackageId().getPackageName().equals(packageName))
					.collect(Collectors.toList()));
		}
		return retVal;
	}

	@Override
	public List<InstalledPackageEntry> readAll() throws InternalException {
		InstalledPackageFile installedPackageFile = readFile();
		return installedPackageFile.getEntries();
	}

	@Override
	public void saveOrUpdate(InstalledPackageEntry t) throws InternalException {
		List<EbuildFile> ebuildFiles = installedEbuildReader.readByCriteria(t.getPackageId().toString(),
				SearchCriteria.EXACTLY_PACKAGE_ID);
		Logger.debug("saveOrUpdate, packageId: " + t.getPackageId() + " , ebuildFile " + ebuildFiles);
		if (ebuildFiles.size() > 1) {
			throw new InternalException("can not write in install repo, found same ebuilds");
		}

		List<InstalledPackageEntry> entries = readFile().getEntries();
		Optional<InstalledPackageEntry> entry = entries.stream()
				.filter(x -> x.getPackageId().equals(ebuildFiles.get(0).getPackageId())).findFirst();

		if (entry.isPresent()) {
			entries.remove(entry.get());
		}
		entries.add(t);

		InstalledPackageFile file = readFile();
		file.setEntries(entries);
		writeFile(file);
	}

	@Override
	public void remove(PackageName packageId) throws InternalException {
		InstalledPackageFile file = readFile();
		List<InstalledPackageEntry> installedPackageFiles = file.getEntries();

		installedPackageFiles = installedPackageFiles.stream().filter(x -> !packageId.equals(x.getPackageId()))
				.collect(Collectors.toList());
		file.setEntries(installedPackageFiles);

		writeFile(file);

		installedEbuildReader.remove(packageId);
	}

	public Serializer getFileReader() {
		return new Serializer();
	}

	private InstalledPackageFile readFile() throws InternalException {
		return getFileReader().read(InstalledPackageFile.class, Configuration.installedDbPath);
	}

	private void writeFile(InstalledPackageFile file) throws InternalException {
		getFileReader().write(InstalledPackageFile.class, Configuration.installedDbPath, file);
	}

	private void initInstallPackageEntryWindows(List<EbuildFile> ebuildFiles,
			List<WindowsRegistryEntry> registryEntries, List<InstalledPackageEntry> entries) {
		List<WindowsRegistryEntry> registryEntryToRemove = new ArrayList<WindowsRegistryEntry>();
		// find with version
		for (EbuildFile ebuild : ebuildFiles) {
			for (WindowsRegistryEntry registryEntry : registryEntries) {
				if (ebuild.getRegistryName() != null && registryEntry.getDisplayName() != null) {
					Pattern p = Pattern.compile(ebuild.getRegistryName());
					  if (FileHelper.isFindByPattern(registryEntry.getDisplayName(), p)) {
						  InstalledPackageEntry entry = new InstalledPackageEntry();
							entry.setPackageId(ebuild.getPackageId());
							entry.getPackageId().setVersion(registryEntry.getDisplayVersion());
							entry.setProductCode(registryEntry.getProductCode());
							entries.add(entry);
							registryEntryToRemove.add(registryEntry);
							// copy ebuild
							Logger.info("entry from registry added to install.db: " + entry + "------" + registryEntry.getDisplayName() + ebuild.getRegistryName());
							break;
					}
				}
			}
		}

		registryEntries.removeAll(registryEntryToRemove);
		for (EbuildFile ebuild : ebuildFiles) {
			for (WindowsRegistryEntry registryEntry : registryEntries) {
				if (ebuild.getRegistryName() != null
						&& registryEntry.getDisplayName().startsWith(ebuild.getRegistryName())
						&& registryEntry.getDisplayVersion() != null
						&& registryEntry.getDisplayVersion().startsWith(ebuild.getPackageId().getVersion())) {
					InstalledPackageEntry entry = new InstalledPackageEntry();
					PackageName packageId = ebuild.getPackageId();
					packageId.setVersion(""); // or parse version in registry
					entry.setPackageId(packageId);
					entry.setProductCode(registryEntry.getProductCode());
					entries.add(entry);
					registryEntryToRemove.add(registryEntry);
					Logger.debug("entry without version from registry added to install.db: " + entry);
				}
			}
		}
	}

}
