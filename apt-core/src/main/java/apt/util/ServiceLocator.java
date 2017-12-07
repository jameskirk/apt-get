package apt.util;

import apt.conf.UserSettingsReader;

public class ServiceLocator {

    private static volatile ServiceLocator instance;
    
    private UserSettingsReader userSettingReader;
    
    public ServiceLocator() {
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

    public UserSettingsReader getUserSettingReader() {
        return userSettingReader;
    }
    

}
