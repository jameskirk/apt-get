package apt.api;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import apt.conf.Configuration;
import apt.ebuild.EbuildFile;
import apt.entity.Keyword;
import apt.entity.PackageName;
import apt.entity.WindowsRegistryEntry;
import apt.exception.InternalException;
import apt.exception.UserException;
import apt.misc.FileHelper;
import apt.misc.Logger;
import apt.misc.WinRegistry;
import apt.repo.entity.InstalledPackageEntry;

public class InstalledPackages {

    private List<InstalledPackageEntry> installedPackages = new ArrayList<InstalledPackageEntry>();

    public boolean isInstalledByRegistry(PackageName packageId, List<EbuildFile> ebuildFiles) throws InternalException {
	List<WindowsRegistryEntry> registryEntries = WinRegistry.getRegistryEntriesWindows();
	installedPackages.clear();

	for (WindowsRegistryEntry registryEntry : registryEntries) {
	    for (EbuildFile ebuild : ebuildFiles) {
		if (ebuild.getRegistryName() != null && registryEntry.getDisplayName() != null) {
		    Pattern p = Pattern.compile(ebuild.getRegistryName());
		    if (FileHelper.isFindByPattern(registryEntry.getDisplayName(), p)) {
			InstalledPackageEntry entry = new InstalledPackageEntry();
			entry.setPackageId(ebuild.getPackageId());
			entry.getPackageId().setVersion(registryEntry.getDisplayVersion());
			entry.setProductCode(registryEntry.getProductCode());
			if (!installedPackages.contains(entry)) {
			    installedPackages.add(entry);
			    // copy ebuild
			    Logger.info("entry from registry added to install.db: " + entry + "------" + registryEntry.getDisplayName()
				    + ebuild.getRegistryName());
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }

    public List<InstalledPackageEntry> getInstalledByRegistry(PackageName packageId, List<EbuildFile> ebuildFiles)
	    throws InternalException {
	List<WindowsRegistryEntry> registryEntries = WinRegistry.getRegistryEntriesWindows();
	installedPackages.clear();

	for (WindowsRegistryEntry registryEntry : registryEntries) {
	    for (EbuildFile ebuild : ebuildFiles) {
		if (ebuild.getPackageId().getCategory().equals(packageId.getCategory()) && ebuild.getPackageId().getPackageName().equals(packageId.getPackageName())
			&& ebuild.getRegistryName() != null && registryEntry.getDisplayName() != null) {
		    Pattern p = Pattern.compile(ebuild.getRegistryName());
		    if (FileHelper.isFindByPattern(registryEntry.getDisplayName(), p)) {
			InstalledPackageEntry entry = new InstalledPackageEntry();
			entry.setPackageId(ebuild.getPackageId());
			entry.getPackageId().setVersion(registryEntry.getDisplayVersion());
			entry.setProductCode(registryEntry.getProductCode());
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

    public boolean isInstalledByDefaultPath(PackageName packageId, EbuildFile ebuildFile, Keyword keywordForInstall)
	    throws InternalException, UserException {
	String defaultPath = ebuildFile.getDefaultPathCalculated(keywordForInstall);
	if (!new File(Configuration.C + defaultPath).exists()) {
	    return false;
	} else {
	    return true;
	}
    }

    public List<InstalledPackageEntry> getInstalledByManually(PackageName packageId, EbuildFile ebuildFile, Keyword keywordForInstall)
	    throws InternalException, UserException {
	installedPackages.clear();
	String defaultPath = ebuildFile.getDefaultPathCalculated(keywordForInstall);
	String programFilesPath = defaultPath.substring(0, defaultPath.lastIndexOf("/"));
	String programName = defaultPath.substring(defaultPath.lastIndexOf("/") + 1, defaultPath.length());
	File file = new File(Configuration.C + programFilesPath);
	String[] directories = file.list(new FilenameFilter() {
	    @Override
	    public boolean accept(File current, String name) {
		if (name.startsWith(programName)) {
		    return new File(current, name).isDirectory();
		} else {
		    return false;
		}
	    }
	});
	for (String dir : directories) {
	    String version = dir.substring(programName.length(), dir.length());
	    if (!version.isEmpty()) {
        	    InstalledPackageEntry entry = new InstalledPackageEntry();
        	    entry.setPackageId(ebuildFile.getPackageId());
        	    entry.getPackageId().setVersion(version);
	    }
	}
	return installedPackages;
    }

}
