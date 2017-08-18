package emerge.api;

import emerge.entity.PackageName;
import emerge.exception.UserException;
import emerge.exception.InternalException;

public interface PackageService {
    
    public void install(PackageName userInput) throws InternalException, UserException;
    
    public void remove(PackageName userInput) throws InternalException, UserException;
    
    public void update(PackageName userInput) throws InternalException, UserException;
    
    public void info(PackageName userInput) throws InternalException, UserException;
    
}
