package apt.ebuild;

import java.util.ArrayList;
import java.util.List;

import apt.entity.Keyword;

public class InstalledEbuildFile extends EbuildFile {
    
    private String installDir;

    private Keyword installKeywords = new Keyword("");

    private List<Keyword> installUse = new ArrayList<Keyword>();

    private String installRegistryProductCode;

    public String getInstallDir() {
        return installDir;
    }

    public void setInstallDir(String installDir) {
        this.installDir = installDir;
    }

    public Keyword getInstallKeywords() {
        return installKeywords;
    }

    public void setInstallKeywords(Keyword installKeywords) {
        this.installKeywords = installKeywords;
    }

    public List<Keyword> getInstallUse() {
        return installUse;
    }

    public void setInstallUse(List<Keyword> installUse) {
        this.installUse = installUse;
    }

    public String getInstallRegistryProductCode() {
        return installRegistryProductCode;
    }

    public void setInstallRegistryProductCode(String installRegistryProductCode) {
        this.installRegistryProductCode = installRegistryProductCode;
    }
    

}
