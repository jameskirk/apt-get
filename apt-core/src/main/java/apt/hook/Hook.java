package apt.hook;

import apt.exception.InternalException;
import apt.exception.UserException;

public interface Hook {
    
    public void execute() throws InternalException, UserException;
    
    public String getName();

}
