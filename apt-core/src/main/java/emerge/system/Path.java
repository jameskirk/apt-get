package emerge.system;

import emerge.conf.Configuration;

public class Path {

    private String value;

    private OsType osType;
    
    private static OsType currentOsType = Configuration.currentOsType;

    public Path(String value) {
	this.value = value;
	osType = currentOsType;
    }

    public Path(String value, OsType type) {
	this.value = value;
	this.osType = type;
    }
    
    public static String getNativeDelimiter() {
	if (currentOsType == OsType.WINDOWS) {
	    return "\\";
	} else if (currentOsType == OsType.UNIX) {
	    return "/";
	}
	throw new IllegalStateException("not delimiter for OS: " + currentOsType);
    }
    
    public String getNativeValue() {
	return getValue(currentOsType);
    }

    public String getValue(OsType targetType) {
	
	if (osType == targetType) {
	    return value;
	}
	
	if (osType == OsType.WINDOWS && targetType == OsType.UNIX) {
	    // value = C:\Program Files (x86) result = /cygdrive/c/Program\ Files\ \(x86\ )
	    String result;
	    if (isAbsolute()) {
		result = "/cygdrive/" + value.substring(0, 1).toLowerCase() + value.substring(1);
		result = result.replace(":", "");
	    } else {
		result = value;
	    }
	    result = result.replace("\\", "/").replace("(", "\\(").replace(")", "\\)").replace(" ", "\\ ");
	    return result;

	} else if (osType == OsType.UNIX && targetType == OsType.WINDOWS) {
	    // value = /cygdrive/c/Program\ Files\ \(x86\ ) OR /bin/ls

	    String result;
	    if (isAbsolute()) {
		if (value.startsWith("/cygdrive/")) {
		    result = value.substring("/cygdrive/".length());
		    result = result.substring(0, 1).toUpperCase() + ":" + result.substring(1);
		} else {
		    result = Configuration.cygwinPath + value; // /bin/ls
		}
	    } else {
		result = value;
	    }
	    result = result.replace("\\ ", " ").replace("\\(", "(").replace("\\)", ")").replace("/", "\\");
	    return result;
	    
	}
	
	throw new IllegalStateException("can not calculate path from " + osType + " to " + targetType);

    }
    
    public boolean isAbsolute() {
	if (osType == OsType.WINDOWS) {
	    return ":".equals(value.substring(1, 2));
	} else if (osType == OsType.UNIX) {
	    return "/".equals(value.substring(0, 1));
	}
	throw new IllegalStateException("can not calculate absolute path: " + value);
    }

}
