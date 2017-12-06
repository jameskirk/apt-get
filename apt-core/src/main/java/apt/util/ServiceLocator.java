package apt.util;

import apt.conf.UserSettingsReader;
import apt.ebuild.EbuildFile;
import apt.repo.EbuildLocalRepositoryReader;
import apt.repo.RepositoryReader;

public class ServiceLocator {

    private static volatile ServiceLocator instance;
    
    private RepositoryReader<EbuildFile> ebuildRepositoryReader;
    
    //private LocalRepositoryReader<InstalledPackageEntry> installedRepositoryReader;
    
    private UserSettingsReader userSettingReader;
    
    public ServiceLocator() {
	ebuildRepositoryReader = new EbuildLocalRepositoryReader();
	//installedRepositoryReader = new InstalledRepositoryReader();
	userSettingReader = new UserSettingsReader();
    }

    public static ServiceLocator getInstance() {
	ServiceLocator localInstance = instance;
	if (localInstance == null) {
	    synchronized (ServiceLocator.class) {
		localInstance = instance;
		if (localInstance == null) {
		    instance = localInstance = new ServiceLocator();
		}
	    }
	}
	return localInstance;
    }

    public RepositoryReader<EbuildFile> getEbuildRepositoryReader() {
        return ebuildRepositoryReader;
    }

//    public LocalRepositoryReader<InstalledPackageEntry> getInstalledRepositoryReader() {
//        return installedRepositoryReader;
//    }

    public UserSettingsReader getUserSettingReader() {
        return userSettingReader;
    }
    

}
