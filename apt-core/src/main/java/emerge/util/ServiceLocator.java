package emerge.util;

import emerge.conf.UserSettingsReader;
import emerge.ebuild.EbuildFile;
import emerge.repo.EbuildLocalRepositoryReader;
import emerge.repo.InstalledRepositoryReader;
import emerge.repo.LocalRepositoryReader;
import emerge.repo.entity.InstalledPackageEntry;

public class ServiceLocator {

    private static volatile ServiceLocator instance;
    
    private LocalRepositoryReader<EbuildFile> ebuildRepositoryReader;
    
    private LocalRepositoryReader<InstalledPackageEntry> installedRepositoryReader;
    
    private UserSettingsReader userSettingReader;
    
    public ServiceLocator() {
	ebuildRepositoryReader = new EbuildLocalRepositoryReader();
	installedRepositoryReader = new InstalledRepositoryReader();
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

    public LocalRepositoryReader<EbuildFile> getEbuildRepositoryReader() {
        return ebuildRepositoryReader;
    }

    public LocalRepositoryReader<InstalledPackageEntry> getInstalledRepositoryReader() {
        return installedRepositoryReader;
    }

    public UserSettingsReader getUserSettingReader() {
        return userSettingReader;
    }
    

}
