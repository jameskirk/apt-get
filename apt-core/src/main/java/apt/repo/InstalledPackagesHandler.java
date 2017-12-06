package apt.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import apt.ebuild.EbuildFile;
import apt.entity.WindowsRegistryEntry;
import apt.exception.InternalException;
import apt.misc.FileHelper;
import apt.misc.Logger;
import apt.misc.WinRegistry;
import apt.repo.entity.InstalledPackageEntry;
import apt.repo.entity.InstalledPackageFile;

public class InstalledPackagesHandler {
    
    private EbuildLocalRepositoryReader centralEbuildReader = new EbuildLocalRepositoryReader();
    
    public void init() throws InternalException {
	List<EbuildFile> infoFiles = centralEbuildReader.readAllCommonEbuilds();
	
	InstalledPackageFile file = new InstalledPackageFile();

	List<InstalledPackageEntry> installedEntries = new ArrayList<InstalledPackageEntry>();
	List<WindowsRegistryEntry> registryEntries = WinRegistry.getRegistryEntriesWindows();

	// find already installed
	initInstallPackageEntryWindows(infoFiles, registryEntries, installedEntries);
    }
    
    private void initInstallPackageEntryWindows(List<EbuildFile> ebuildFiles, List<WindowsRegistryEntry> registryEntries,
	    List<InstalledPackageEntry> entries) {
	List<WindowsRegistryEntry> registryEntryToRemove = new ArrayList<WindowsRegistryEntry>();
	// find with version
	for (WindowsRegistryEntry registryEntry : registryEntries) {
	    for (EbuildFile ebuild : ebuildFiles) {
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
			Logger.info("entry from registry added to install.db: " + entry + "------" + registryEntry.getDisplayName()
				+ ebuild.getRegistryName());
			break;
		    }
		}
	    }
	}
    }
    
}
