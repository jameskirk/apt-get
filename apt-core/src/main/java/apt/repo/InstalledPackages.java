package apt.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import apt.ebuild.EbuildFile;
import apt.ebuild.InstalledEbuildFile;
import apt.entity.PackageName;
import apt.entity.WindowsRegistryEntry;
import apt.exception.InternalException;
import apt.exception.UserException;
import apt.misc.FileHelper;
import apt.misc.Logger;
import apt.misc.WinRegistry;

public class InstalledPackages {

    private List<InstalledEbuildFile> installedPackages = new ArrayList<InstalledEbuildFile>();

    public List<InstalledEbuildFile> getInstalledByRegistry(PackageName packageId, List<EbuildFile> ebuildFiles)
	    throws InternalException {
	List<WindowsRegistryEntry> registryEntries = WinRegistry.getRegistryEntriesWindows();
	installedPackages.clear();

	for (WindowsRegistryEntry registryEntry : registryEntries) {
	    for (EbuildFile ebuild : ebuildFiles) {
		if (ebuild.getPackageId().getCategory().equals(packageId.getCategory()) && ebuild.getPackageId().getPackageName().equals(packageId.getPackageName())
			&& ebuild.getRegistryName() != null && registryEntry.getDisplayName() != null) {
		    Pattern p = Pattern.compile(ebuild.getRegistryName());
		    if (FileHelper.isFindByPattern(registryEntry.getDisplayName(), p)) {
			InstalledEbuildFile entry = new InstalledEbuildFile();
			entry.setPackageId(ebuild.getPackageId());
			entry.getPackageId().setVersion(registryEntry.getDisplayVersion());
			entry.setInstallRegistryProductCode(registryEntry.getProductCode());
			if (!installedPackages.contains(entry)) {
			    installedPackages.add(entry);
			    // copy ebuild
			    Logger.info("entry from registry added to install.db: " + entry + "------" + registryEntry.getDisplayName()
				    + ebuild.getRegistryName());
			}
		    }
		}
	    }
	}
	return installedPackages;
    }

    public List<InstalledEbuildFile> getInstalledByManually(PackageName packageId, EbuildFile ebuildFile)
	    throws InternalException, UserException {
	return new ArrayList<>();
    }

}
