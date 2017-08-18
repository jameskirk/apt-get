package emerge.core;

import emerge.entity.PackageName;
import emerge.exception.UserException;
import emerge.exception.InternalException;

public interface ActionHandler {
    
    public void execute(PackageName packageId) throws InternalException, UserException;

}
