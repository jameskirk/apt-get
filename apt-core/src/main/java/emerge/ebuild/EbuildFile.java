package emerge.ebuild;

import java.util.ArrayList;
import java.util.List;

import emerge.entity.Keyword;
import emerge.entity.KeywordGroup;
import emerge.entity.PackageName;

public class EbuildFile {

    private PackageName packageId;

    private String description;

    private String homepage;

    private String license;

    private List<Keyword> keywords = new ArrayList<Keyword>();

    private List<Keyword> iuse = new ArrayList<Keyword>();

    private List<KeywordGroup> requiredUse = new ArrayList<KeywordGroup>();

    private List<KeywordGroup> srcUriList = new ArrayList<KeywordGroup>();

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

    public List<KeywordGroup> getSrcUriList() {
	return srcUriList;
    }

    public void setSrcUriList(List<KeywordGroup> srcUriList) {
	this.srcUriList = srcUriList;
    }

    public List<Keyword> getKeywords() {
	return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
	this.keywords = keywords;
    }

    public List<Keyword> getIuse() {
	return iuse;
    }

    public void setIuse(List<Keyword> iuse) {
	this.iuse = iuse;
    }

    public List<KeywordGroup> getRequiredUse() {
	return requiredUse;
    }

    public void setRequiredUse(List<KeywordGroup> requiredUse) {
	this.requiredUse = requiredUse;
    }

    public String getRegistryName() {
	return registryName;
    }

    public void setRegistryName(String registryName) {
	this.registryName = registryName;
    }

    @Override
    public String toString() {
	StringBuilder pairToString = new StringBuilder();
	pairToString.append("KEYWORDS=");
	for (Keyword keyword : keywords) {
	    pairToString.append(keyword + " ");
	}
	if (!iuse.isEmpty()) {
	    pairToString.append("\nUSE=");
	    for (Keyword iuse1 : iuse) {
		pairToString.append(iuse1 + " ");
	    }
	}
	if (!requiredUse.isEmpty()) {
	    pairToString.append("\nREQUIRED_USE=");
	    for (KeywordGroup iuse1 : requiredUse) {
		pairToString.append(iuse1.getKeywords() + " ");
	    }
	}
	pairToString.append("\nSRC_URL=");
	for (KeywordGroup pair : srcUriList) {
	    pairToString.append(pair.getKeywords() + " " + pair.getValue() + "\n");
	}
	return "\nPACKAGE=" + packageId + "\nDESCRIPTION=" + description + "\nHOMEPAGE=" + homepage + "\nLICENCE=" + license + "\n" 
		+ pairToString + "REGISTRY_NAME="+ registryName + "\n";
    }

}
