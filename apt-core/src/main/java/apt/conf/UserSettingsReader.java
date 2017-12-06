package apt.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import apt.entity.EmergeVariable;
import apt.entity.Keyword;
import apt.exception.InternalException;
import apt.misc.FileHelper;

public class UserSettingsReader {
    
    public UserSettings read() throws InternalException {
	String fileAsString = FileHelper.readFile(Configuration.makeConfPath);
	UserSettings settings = new UserSettings();
	settings.setProxy(readVariable(fileAsString, UserSettingsVariable.PROXY.name()));
	
	List<Keyword> processedKeywords = new ArrayList<Keyword>();
	readVariableList(fileAsString, UserSettingsVariable.ACCEPT_KEYWORDS.name()).stream().forEach(x -> processedKeywords.add(new Keyword(x)));
	settings.setAcceptKeywords(processedKeywords);
	
	List<Keyword> processedUse = new ArrayList<Keyword>();
	readVariableList(fileAsString, EmergeVariable.USE.name()).stream().forEach(x -> processedUse.add(new Keyword(x)));
	settings.setUse(processedUse);
	
	settings.setAcceptLicense(readVariableList(
		fileAsString, UserSettingsVariable.ACCEPT_LICENSE.name()));
	
	return settings;
    }
    
    private List<String> readVariableList(String fileAsString, String key) {
	List<String> retVal = new ArrayList<String>();
	Arrays.asList(readVariable(fileAsString, key)
		.split("( )+"))
		.stream().forEach(x -> retVal.add(x));
	return retVal;
    }
    
    private String readVariable(String fileAsString, String key) {
	Pattern p = Pattern.compile("(^(" + key + ")|((\n)(" + key + ")))((=\"){1})(.|\\s)*?([\"]{1})");
	String keyValue = FileHelper.findByPattern(fileAsString, p);
	if (keyValue == null) {
	    return "";
	}
	String value = keyValue.replace("\n" + key + "=\"", "").replace(key + "=\"", "").replace("\"", "");
	return value;
	
    }

}
