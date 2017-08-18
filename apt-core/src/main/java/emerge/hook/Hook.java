package emerge.hook;

import emerge.exception.UserException;
import emerge.exception.InternalException;

public interface Hook {
    
    public void execute() throws InternalException, UserException;
    
    public String getName();

}
