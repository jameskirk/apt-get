package apt.util;

import apt.conf.UserSettingsReader;
import apt.ebuild.EbuildFile;
import apt.repo.LocalRepoPackages;
import apt.repo.RepoPackages;

public class ServiceLocator {

    private static volatile ServiceLocator instance;
    
    private RepoPackages<EbuildFile> ebuildRepositoryReader;
    
    //private LocalRepositoryReader<InstalledPackageEntry> installedRepositoryReader;
    
    private UserSettingsReader userSettingReader;
    
    public ServiceLocator() {
	ebuildRepositoryReader = new LocalRepoPackages();
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

    public RepoPackages<EbuildFile> getEbuildRepositoryReader() {
        return ebuildRepositoryReader;
    }

//    public LocalRepositoryReader<InstalledPackageEntry> getInstalledRepositoryReader() {
//        return installedRepositoryReader;
//    }

    public UserSettingsReader getUserSettingReader() {
        return userSettingReader;
    }
    

}
