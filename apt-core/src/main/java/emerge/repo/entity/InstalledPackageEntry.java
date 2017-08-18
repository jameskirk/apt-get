package emerge.repo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import emerge.ebuild.EbuildFile;
import emerge.entity.Keyword;
import emerge.entity.PackageName;

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
