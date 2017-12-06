package apt.repo;

import java.util.List;

import apt.entity.PackageName;
import apt.exception.InternalException;

public interface RepositoryReader<T> {
    
    public void cache() throws InternalException;
    
    public T readExactlyOne(PackageName packageId) throws InternalException;
    
    public List<T> readByCriteria(PackageName packageId, SearchCriteria criteria) throws InternalException;
    
    public List<T> readAll() throws InternalException;
    
    public void saveOrUpdate(T t) throws InternalException;
    
    public void remove(PackageName packageId) throws InternalException;

}
