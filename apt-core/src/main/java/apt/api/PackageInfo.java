package apt.api;

import java.util.ArrayList;
import java.util.List;

import apt.entity.PackageName;
import apt.misc.Logger;

public class PackageInfo {
    
    private PackageName packageName;
    
    private List<PackageName> availablePackages = new ArrayList<>();
    
    private List<PackageName> installedPackages = new ArrayList<>();
    
    public PackageName getPackageName() {
        return packageName;
    }

    public void setPackageName(PackageName packageName) {
        this.packageName = packageName;
    }

    public List<PackageName> getAvailablePackages() {
        return availablePackages;
    }

    public void setAvailablePackages(List<PackageName> availablePackages) {
        this.availablePackages = availablePackages;
    }

    public List<PackageName> getInstalledPackages() {
        return installedPackages;
    }

    public void setInstalledPackages(List<PackageName> installedPackages) {
        this.installedPackages = installedPackages;
    }
    
    public String getHomepage() {
        return "";
    }

    @Override
    public String toString() {
	StringBuilder retVal = new StringBuilder();
	String whitespace = "          ";
	
	retVal.append(Logger.ANSI_GREEN + "* " + Logger.ANSI_RESET);
	    retVal.append(packageName.getCategory() + "/" + packageName.getPackageName() + "\n");
	
	if (!availablePackages.isEmpty()) {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Latest version available: "+ Logger.ANSI_RESET);
	    availablePackages.stream().forEach( x -> retVal.append(x.getVersion() + ", "));
	    retVal.append("\n");
	} else {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Latest version available: " + Logger.ANSI_RESET + "[ Not Available ]"+ "\n");
	}
	
	if (!installedPackages.isEmpty()) {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Latest version installed: " + Logger.ANSI_RESET + installedPackages.get(installedPackages.size() - 1).getVersion()+ "\n");
	} else {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Latest version installed: " + Logger.ANSI_RESET + "[ Not Installed ]"+ "\n");
	}

	if (!availablePackages.isEmpty()) {
	    retVal.append(whitespace + Logger.ANSI_GREEN + "Homepage:                 " 
		    + Logger.ANSI_RESET + getHomepage() + "\n");
	}
	return retVal.toString();
	
    }
    
    

}
