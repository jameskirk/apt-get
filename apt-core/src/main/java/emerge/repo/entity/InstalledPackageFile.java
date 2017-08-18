package emerge.repo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InstalledPackageFile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private List<InstalledPackageEntry> entries = new ArrayList<InstalledPackageEntry>();
    
    public List<InstalledPackageEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<InstalledPackageEntry> entry) {
        this.entries = entry;
    }

}
