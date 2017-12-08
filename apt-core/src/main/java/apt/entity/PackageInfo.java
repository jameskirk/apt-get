package apt.entity;

import java.util.List;

import apt.ebuild.EbuildFile;
import apt.ebuild.InstalledEbuildFile;

public class PackageInfo {
    
    private PackageName packageName;
    
    private List<EbuildFile> availablePackages;
    
    private EbuildFile commonEbuild;
    
    private List<InstalledEbuildFile> installedPackages;

    public PackageName getPackageName() {
        return packageName;
    }

    public void setPackageName(PackageName packageName) {
        this.packageName = packageName;
    }

    public List<EbuildFile> getAvailablePackages() {
        return availablePackages;
    }

    public void setAvailablePackages(List<EbuildFile> availablePackages) {
        this.availablePackages = availablePackages;
    }

    public List<InstalledEbuildFile> getInstalledPackages() {
        return installedPackages;
    }

    public void setInstalledPackages(List<InstalledEbuildFile> installedPackages) {
        this.installedPackages = installedPackages;
    }

    public EbuildFile getCommonEbuild() {
        return commonEbuild;
    }

    public void setCommonEbuild(EbuildFile commonEbuild) {
        this.commonEbuild = commonEbuild;
    }
    
    

}
