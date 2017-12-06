package apt.repo.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryCacheFile implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Map<String, List<String>> categoryPackageMap = new HashMap<String, List<String>>();
    
    // <category + packageName , list<versions>>
    private Map<String, List<String>> categoryAndPackageVersionMap = new HashMap<String, List<String>>();

    public Map<String, List<String>> getCategoryPackageMap() {
        return categoryPackageMap;
    }

    public void setCategoryPackageMap(Map<String, List<String>> categoryPackageMap) {
        this.categoryPackageMap = categoryPackageMap;
    }

    public Map<String, List<String>> getCategoryAndPackageVersionMap() {
        return categoryAndPackageVersionMap;
    }

    public void setCategoryAndPackageVersionMap(Map<String, List<String>> categoryAndPackageVersionMap) {
        this.categoryAndPackageVersionMap = categoryAndPackageVersionMap;
    }
    
    

}
