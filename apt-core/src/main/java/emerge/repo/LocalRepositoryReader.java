package emerge.repo;

import java.util.List;

import emerge.entity.PackageName;
import emerge.exception.InternalException;

public interface LocalRepositoryReader<T> {
    
    public void cache() throws InternalException;
    
    public T readExactlyOne(PackageName packageId) throws InternalException;
    
    public List<T> readByCriteria(String userInput, SearchCriteria criteria) throws InternalException;
    
    public List<T> readAll() throws InternalException;
    
    public void saveOrUpdate(T t) throws InternalException;
    
    public void remove(PackageName packageId) throws InternalException;

}
