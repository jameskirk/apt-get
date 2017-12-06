package apt.misc;

import apt.entity.PackageName;

public class NameDeterminer {
    
    public String getCategory(String packageId) {
	if (packageId.charAt(0) == '/') {
	    packageId = packageId.substring(1);
	}
	String category = packageId.split("/")[0];
	return category;
    }
    
    public String getNameWithoutVersion(String packageId) {
	if (packageId.charAt(0) == '/') {
	    packageId = packageId.substring(1);
	}
	String packageNameWithVersion = packageId.split("/")[1];
	String packageName = packageNameWithVersion.split("(-)(\\d)")[0];
	return packageName;
    }
    
    public String getVersion(String packageId) {
	if (packageId.charAt(0) == '/') {
	    packageId = packageId.substring(1);
	}
	String packageNameWithVersion = packageId.split("/")[1];
	String packageName = packageNameWithVersion.split("(-)(\\d)")[0];
	String version = packageNameWithVersion.replace(packageName + "-", "");
	return version;
    }
    
    public int compareVersions(String version1, String version2) {
//	verion1 = verion1.replaceAll("([a-zA-Z]+)$", "");
//	version2 = version2.replaceAll("([a-zA-Z]+)$", "");
	version1 = version1.replaceAll("((_)[a-zA-Z0-9]+)$", "");
	version2 = version2.replaceAll("((_)[a-zA-Z0-9]+)$", "");
	String[] vals1 = version1.split("\\.");
	String[] vals2 = version2.split("\\.");
	int i = 0;
	// set index to first non-equal ordinal or length of shortest version
	// string
	while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
	    i++;
	}
	// compare first non-equal ordinal number
	if (i < vals1.length && i < vals2.length) {
	    int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
	    return Integer.signum(diff);
	}
	// the strings are equal or one string is a substring of the other
	// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
	else {
	    return Integer.signum(vals1.length - vals2.length);
	}
    }
    
    public static PackageName parseCategoryNameVersion(String userInput) {
	if (userInput.length() == 0) {
	    return null;
	}
	if (userInput.charAt(0) == '/') {
	    userInput = userInput.substring(1);
	}
	String category = "";
	String packageName = "";
	String version = "";
	String packageNameWithVersion;
	
	String tempSplitting[] = userInput.split("/");
	if (tempSplitting.length == 0) {
	    return null;
	} else if (tempSplitting.length == 1 && !tempSplitting[0].isEmpty()) {
	    packageNameWithVersion = userInput;
	} else if (tempSplitting.length == 2) {
	    category = tempSplitting[0];
	    packageNameWithVersion = tempSplitting[1];
	} else {
	    return null;
	}
	
	int lastMinus = packageNameWithVersion.lastIndexOf("-");
	if (lastMinus == -1) {
	    packageName = packageNameWithVersion;
	} else if (lastMinus == 0) {
	    return null;
	} else if (lastMinus == packageNameWithVersion.length() - 1) {
	    return null;
	}
	else {
	    packageName = packageNameWithVersion.substring(0, lastMinus);
	    version = packageNameWithVersion.substring(lastMinus + 1);
	    if (version.isEmpty()){
		return null;
	    }
	    if (!Character.isDigit(version.charAt(0))) {
		packageName = packageNameWithVersion;
		version = "";
	    }
	    
	    
	}
	
	return new PackageName(category, packageName, version);
    }
    
}
