package emerge.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import emerge.ebuild.EbuildFile;
import emerge.misc.Logger;
import emerge.misc.NameDeterminer;
import emerge.repo.entity.InstalledPackageEntry;

public class PackageDetailsInfo {
    
    private InstalledPackageEntry installedPackage;
    
    private List<EbuildFile> availablePackages = new ArrayList<EbuildFile>();
    
    public PackageDetailsInfo(InstalledPackageEntry installedPackage, List<EbuildFile> availablePackages) {
	this.installedPackage = installedPackage;
	availablePackages.sort(new Comparator<EbuildFile>() {
	    @Override
	    public int compare(EbuildFile o1, EbuildFile o2) {
		return new NameDeterminer().compareVersions(o1.getPackageId().getVersion(), o2.getPackageId().getVersion());
	    }
	});
	this.availablePackages = availablePackages;
    }

    public InstalledPackageEntry getInstalledPackage() {
        return installedPackage;
    }

    public List<EbuildFile> getAvailablePackages() {
        return availablePackages;
    }
    
    public String getCategory() {
	if (installedPackage != null) {
	    return installedPackage.getPackageId().getCategory();
	} else {
	    return availablePackages.get(0).getPackageId().getCategory();
	}
    }
    
    public String getPackageName() {
	if (installedPackage != null) {
	    return installedPackage.getPackageId().getPackageName();
	} else {
	    return availablePackages.get(0).getPackageId().getPackageName();
	}
    }
    
    public List<String> getVersions() {
	List<String> retVal = new ArrayList<String>();
	if (installedPackage != null) {
	    retVal.add(installedPackage.getPackageId().getVersion());
	}
	
	availablePackages.stream().forEach(x -> retVal.add(x.getPackageId().getVersion()));
	return retVal;
    }
    
    
    @Override
    public String toString() {
	StringBuilder retVal = new StringBuilder();
	String whitespace = "          ";
	
	retVal.append(Logger.ANSI_GREEN + "* " + Logger.ANSI_RESET);
	if (installedPackage != null) {
	    retVal.append(installedPackage.getPackageId().getCategory() + "/" + installedPackage.getPackageId().getPackageName() + "\n");
	} else {
	    retVal.append(availablePackages.get(0).getPackageId().getCategory() + "/" + availablePackages.get(0).getPackageId().getPackageName()+ "\n");
	}
	
	if (!availablePackages.isEmpty()) {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Latest version available: "+ Logger.ANSI_RESET);
	    availablePackages.stream().forEach( x -> retVal.append(x.getPackageId().getVersion() + ", "));
	    retVal.append("\n");
	} else {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Latest version available: " + Logger.ANSI_RESET + "[ Not Available ]"+ "\n");
	}
	
	if (installedPackage != null) {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Latest version installed: " + Logger.ANSI_RESET + installedPackage.getPackageId().getVersion()+ "\n");
	} else {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Latest version installed: " + Logger.ANSI_RESET + "[ Not Installed ]"+ "\n");
	}

	if (!availablePackages.isEmpty()) {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Homepage:                 " 
		    + Logger.ANSI_RESET + availablePackages.get(0).getHomepage() + "\n");
	}
	return retVal.toString();
	
    }


}
