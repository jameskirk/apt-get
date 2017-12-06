package apt.system;

import apt.conf.Configuration;

public enum OsType {
    WINDOWS, UNIX;

    public boolean isCurrent() {
	return Configuration.currentOsType == this;
    }
}
