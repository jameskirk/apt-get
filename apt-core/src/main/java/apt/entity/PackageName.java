package apt.entity;

import java.io.Serializable;

public class PackageName implements PackageIdentifiable, Serializable {
    
    private String category = "";
    
    private String name = "";
    
    private String version = "";
    
    private Keyword keyword = new Keyword("");

    public PackageName(String category, String name, String version) {
	this.category = category;
	this.name = name;
	this.version = version;
    }
    
    public PackageName(String category, String name, String version, Keyword keyword) {
	this.category = category;
	this.name = name;
	this.version = version;
	this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPackageName() {
        return name;
    }

    public void setPackageName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof PackageName)) {
	    return false;
	}
	PackageName packageId2 = (PackageName) obj;
        return category.equals(packageId2.getCategory())
        	&& name.equals(packageId2.getPackageName())
        	&& version.equals(packageId2.getVersion());
    }

    @Override
    public String toString() {
	StringBuilder string = new StringBuilder();
	if (!category.isEmpty()) {
	    string.append(category);
	    string.append("/");
	}
	string.append(name);
	if (version != null && !version.isEmpty()) {
	    string.append("-");
	    string.append(version);
	}
	return string.toString();
    }

}
