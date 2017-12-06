package apt.conf;

import java.util.List;

import apt.entity.Keyword;

public class UserSettings {
    
    private List<String> acceptLicense;
    
    private List<Keyword> acceptKeywords;
    
    private List<Keyword> use;
    
    private String proxy;

    public List<Keyword> getAcceptKeywords() {
        return acceptKeywords;
    }

    public void setAcceptKeywords(List<Keyword> acceptKeywords) {
        this.acceptKeywords = acceptKeywords;
    }

    public List<String> getAcceptLicense() {
        return acceptLicense;
    }

    public void setAcceptLicense(List<String> acceptLicense) {
        this.acceptLicense = acceptLicense;
    }

    public List<Keyword> getUse() {
        return use;
    }

    public void setUse(List<Keyword> use) {
        this.use = use;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }
    
    @Override
    public String toString() {
        return acceptLicense.toString() + acceptKeywords.toString() + proxy;
    }
    

}
