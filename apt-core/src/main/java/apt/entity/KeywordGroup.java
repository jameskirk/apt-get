package apt.entity;

import java.util.ArrayList;
import java.util.List;

public class KeywordGroup {
    
    private List<String> keywords = new ArrayList<String>();
    
    private String value;
    
    private List<KeywordGroup> innerKeywords;
    
    public KeywordGroup(List<String> keywords) {
	this.keywords = keywords;
    }

    public KeywordGroup(List<String> keywords, List<KeywordGroup> innerKeywords) {
	this.keywords = keywords;
	this.innerKeywords = innerKeywords;
    }
    
    public KeywordGroup(List<String> keywords, String value) {
	this.keywords = keywords;
	this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<KeywordGroup> getInnerKeywords() {
        return innerKeywords;
    }

    public void setInnerKeywords(List<KeywordGroup> innerKeywords) {
        this.innerKeywords = innerKeywords;
    }
    
    public String getUriExtention() {
	int i = value.lastIndexOf('.');
	if (i > 0) {
	    return value.substring(i + 1);
	} else {
	    return "";
	}
    }
    
    

}
