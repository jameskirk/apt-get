package apt.repo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.Object;

import apt.ebuild.EbuildFile;
import apt.entity.Keyword;
import apt.entity.PackageName;

public class InstalledPackageEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    private PackageName packageId;
    private EbuildFile ebuildFile;
    private Keyword keyword;
    private List<Keyword> use = new ArrayList<Keyword>();
    private String productCode;
    private String installDir;

    public InstalledPackageEntry() {
    }

    public PackageName getPackageId() {
	return packageId;
    }

    public void setPackageId(PackageName packageId) {
	this.packageId = packageId;
    }

    public EbuildFile getEbuildFile() {
	return ebuildFile;
    }

    public void setEbuildFile(EbuildFile ebuildFile) {
	this.ebuildFile = ebuildFile;
    }

    public Keyword getKeyword() {
	return keyword;
    }

    public void setKeyword(Keyword keyword) {
	this.keyword = keyword;
    }

    public String getProductCode() {
	return productCode;
    }

    public void setProductCode(String productCode) {
	this.productCode = productCode;
    }

    public String getInstallDir() {
	return installDir;
    }

    public void setInstallDir(String installDir) {
	this.installDir = installDir;
    }

    public List<Keyword> getUse() {
	return use;
    }

    public void setUse(List<Keyword> use) {
	this.use = use;
    }
    
    public boolean equals(Object obj) {
	if (!(obj instanceof InstalledPackageEntry)) {
	    return false;
	}
	InstalledPackageEntry installedPackage = (InstalledPackageEntry) obj;
	if (!this.getPackageId().equals(installedPackage.getPackageId())) {
	    return false;
	}
	if (this.getKeyword() != null && !this.getKeyword().equals(installedPackage.getKeyword())) {
	    return false;
	}
	if (this.getKeyword() == null && this.getKeyword() != null) {
	    return false;
	}
	return true;
    }
    
    @Override
    public String toString() {
	StringBuilder pairToString = new StringBuilder();
	if (keyword != null) {
	    pairToString.append("KEYWORDS=" + keyword + " ");
	}
	if (use != null && !use.isEmpty()) {
	    pairToString.append("\t USE=");
	    for (Keyword iuse1 : use) {
		pairToString.append(iuse1 + " ");
	    }
	}
	return "packageId=" + packageId + "\t" + pairToString + "\n";
    }

}
