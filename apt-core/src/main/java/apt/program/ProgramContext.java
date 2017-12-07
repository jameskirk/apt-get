package apt.program;

import java.util.HashMap;
import java.util.Map;

import apt.entity.AptGetKeyword;

public class ProgramContext {
	
	private Map<String, String> variablesMap = new HashMap<>();

	public Map<String, String> getVariablesMap() {
		return variablesMap;
	}
	
	public String getVariable(AptGetKeyword keyword) {
		return variablesMap.get(keyword.toString());
	}

	public void setVariable(AptGetKeyword keyword, String value) {
		variablesMap.put(keyword.toString(), value);
	}

}
