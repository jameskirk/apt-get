package apt.core;

import apt.entity.PackageName;
import apt.exception.InternalException;
import apt.exception.UserException;

public interface ActionHandler {
    
    public void execute(PackageName packageId) throws InternalException, UserException;

}
