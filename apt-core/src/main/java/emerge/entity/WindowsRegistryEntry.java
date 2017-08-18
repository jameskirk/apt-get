package emerge.entity;

public class WindowsRegistryEntry {
    
    private String displayName;
    
    private String productCode;
    
    private String displayVersion;

    public WindowsRegistryEntry(String displayName, String productCode, String displayVersion) {
	this.displayName = displayName;
	this.productCode = productCode;
	this.displayVersion = displayVersion;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDisplayVersion() {
        return displayVersion;
    }

    public void setDisplayVersion(String displayVersion) {
        this.displayVersion = displayVersion;
    }
    
    
    @Override
    public String toString() {
        return "displayName=" + displayName + " , productCode=" + productCode + " , displayVersion=" + displayVersion;
    }
    

}
