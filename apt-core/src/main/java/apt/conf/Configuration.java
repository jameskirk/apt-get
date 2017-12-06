package apt.conf;

import java.io.File;
import java.util.logging.Level;

import apt.system.OsType;
import apt.system.Path;

/**
 * Contains only UNIX-format paths
 */
public class Configuration {

    // ONLY NATIVE PATHs
    static {
	currentOsType = OsType.WINDOWS;
	/*emergeProgramDir = new Path(System.getProperty("user.dir")).getValue(OsType.UNIX)
		+ "/"
		+ "../work/emerge";*/
    }

    /** PLEASE DEFINT PATH TO CYGWIN, NATIVE **/
    public static final String cygwinPath = "C:/cygwin64";
    
    public static final String C = "C:/";

    /**
     * PLEASE DEFINE PATH WHEN PROGRAM EMERGE INSTALLED !!! may be
     * C:/dev/Emerge/work/emerge
     **/
    // public static final String emergeProgramDir = cygwinPath +
    // "/usr/share/emerge";
    public static final String emergeProgramDir = System.getProperty("user.home").replace("\\", "/") + "/.apt-get";//"C:/Users/igolovin/Dropbox/Project/Emerge/work/emerge";
    //public static final String emergeProgramDir;

    /** PLEASE DEFINE LOGGER LEVEL **/
    // public static final Level loggerLevel = Level.INFO;
    public static final Level loggerLevel = Level.INFO;
    
    public static final String makeConfPath = Configuration.emergeProgramDir + "/" + "make.conf";
    
    public static final String emergeLogPath = Configuration.emergeProgramDir + "/" + "emerge.log";

    public static final String msiexecLogPath = emergeProgramDir + "/" + "msiexec.log";

    public static final String installedRepositoryDir = emergeProgramDir + "/" + "installed_repository";
    
    public static final String installedDbPath = installedRepositoryDir + "/" + "installed.db";

    public static final String ebuildRepositoryDir = emergeProgramDir + "/" + "repository";

    public static final String packageRepositoryDir = emergeProgramDir + "/" + "package_repository";
    
    public static final OsType currentOsType;

    public static final String testEbuildId = "app-arch/p7zip-9.20";

    public static final String testEbuildIdExperimental = "app-arch/p7zip-test-9.20";

}
