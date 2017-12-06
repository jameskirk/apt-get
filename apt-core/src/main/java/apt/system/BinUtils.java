package apt.system;

import apt.exception.UserException;


public class BinUtils {

    public static final String bash = "/bin/bash ";
    public static final String cd = "/bin/cd ";
    public static final String cp = "/bin/cp ";
    public static final String mv = "/bin/mv ";
    public static final String rm = "/bin/rm ";
    public static final String rmdir = "/bin/rmdir ";
    public static final String mkdir = "/bin/mkdir ";
    public static final String ln = "/bin/ln ";
    public static final String ls = "/bin/ls ";

    public static final String wget = "/bin/wget ";
    public static final String unzip = "/bin/unzip ";

    public static CommandCallback exists(String path) {
	return () -> {
	    if (ProcessExecutor.executeUnixCommand(" [ -d " + path + " ] || " + " [ -f " + path + " ] ") != 0) {
		throw new UserException();
	    }
	};
    }

    public static CommandCallback mkdir(String dirPath) {
	return () -> {
	    if (ProcessExecutor.executeUnixCommand(mkdir + " -p " + dirPath) != 0) {
		throw new UserException();
	    }
	};
    }

    public static CommandCallback wget(String uri, String destination, String proxy) {
	return () -> {
	    String proxyString = "";
	    if (proxy != null && !proxy.isEmpty()) {
		proxyString = " -e use_proxy=yes -e http_proxy=" + proxy + " -e https_proxy=" + proxy;
	    }
	    if (ProcessExecutor.executeUnixCommand(wget + uri + " -O " + destination + proxyString 
		    + " && /bin/chmod a+x "+ destination) != 0) {
		throw new UserException();
	    }
	};
    }

    public static CommandCallback mv(String source, String destination) {
	return () -> {
	    if (ProcessExecutor.executeUnixCommand(mv + source + " " + destination) != 0) {
		throw new UserException();
	    }
	};
    }

    public static CommandCallback rm(String path) {
	return () -> {
	    if (ProcessExecutor.executeUnixCommand(rm + path) != 0) {
		throw new UserException();
	    }
	};
    }

    public static CommandCallback rmForce(String path) {
	return () -> {
	    if (ProcessExecutor.executeUnixCommand(rm + "-rf " + path) != 0) {
		throw new UserException();
	    }
	};
    }
    
    public static CommandCallback ln(String source, String destination) {
	return () -> {
	    if (ProcessExecutor.executeUnixCommand(ln + source + " " + destination) != 0) {
		throw new UserException();
	    }
	};
    }
    
    public static CommandCallback unarchive(String source, String destination) {
	return () -> {
	    String extention = "";
	    int i = source.lastIndexOf('.');
	    if (i > 0) {
		extention = source.substring(i + 1);
	    }
	    String sourceWithoutExtention = source.substring(0, source.lastIndexOf(extention) - 1);
	    
	    String archivator;
	    String args;
	    if (BinUtils.exists(sourceWithoutExtention + ".zip").execSilent()) {
		archivator = BinUtils.unzip;
		args = " -d " + destination;
	    } else {
		throw new UserException();
	    }
	    if (ProcessExecutor.executeUnixCommand(archivator + source + args) != 0) {
		throw new UserException();
	    }
	};
    }

    public static interface CommandCallback {
	public void exec() throws UserException;
	
	default boolean execSilent() {
	    try {
		exec();
		return true;
	    } catch (UserException e) {
		return false;
	    }
	}

    }

}
