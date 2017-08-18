package emerge.api;

import java.util.List;

import emerge.entity.PackageDetailsInfo;
import emerge.entity.PackageName;
import emerge.exception.UserException;
import emerge.exception.InternalException;
import emerge.repo.SearchCriteria;

public interface RepositoryService {
    
    public List<PackageDetailsInfo> find(PackageName userInput) throws InternalException, UserException;
    
    public List<PackageDetailsInfo> findQuiet(PackageName userInput, SearchCriteria criteria) throws InternalException, UserException;
    
    public List<PackageDetailsInfo> findByContainsName(String userInput) throws InternalException, UserException;
    
    public List<PackageDetailsInfo> findByContainsDescription(String userInput) throws InternalException, UserException;
    
    public void sync() throws InternalException, UserException;
    
    public void firstInit() throws InternalException, UserException;

}
