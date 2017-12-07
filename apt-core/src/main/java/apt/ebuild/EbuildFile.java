package apt.ebuild;

import java.util.ArrayList;
import java.util.List;

import apt.entity.KeywordExpression;
import apt.entity.PackageName;

public class EbuildFile {

    private PackageName packageId;

    private String description;

    private String homepage;

    private String license;
    
    private KeywordExpression srcUri = new KeywordExpression();
    
    private KeywordExpression platforms = new KeywordExpression();

    private KeywordExpression iuse = new KeywordExpression();
    
    private String registryName;

	public PackageName getPackageId() {
		return packageId;
	}

	public void setPackageId(PackageName packageId) {
		this.packageId = packageId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	
	public KeywordExpression getSrcUri() {
		return srcUri;
	}

	public void setSrcUri(KeywordExpression srcUri) {
		this.srcUri = srcUri;
	}

	public KeywordExpression getPlatforms() {
		return platforms;
	}

	public void setPlatforms(KeywordExpression platforms) {
		this.platforms = platforms;
	}

	public KeywordExpression getIuse() {
		return iuse;
	}

	public void setIuse(KeywordExpression iuse) {
		this.iuse = iuse;
	}

	public String getRegistryName() {
		return registryName;
	}

	public void setRegistryName(String registryName) {
		this.registryName = registryName;
	}
    

}
