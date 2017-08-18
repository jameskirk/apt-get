package emerge.system;

import emerge.conf.Configuration;

public enum OsType {
    WINDOWS, UNIX;

    public boolean isCurrent() {
	return Configuration.currentOsType == this;
    }
}
