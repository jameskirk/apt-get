package emerge.hook;

import java.util.Map;

import emerge.entity.EmergeVariable;
import emerge.entity.PackageName;

public abstract class AbstractHook implements Hook {
    
    // must contain only UNIX path
    protected Map<String, String> variableMap;
    
    public AbstractHook(Map<String, String> variableMap) {
	this.variableMap = variableMap;
    }
    
    protected String getVariable(EmergeVariable variable) {
	return variableMap.get(variable.name());
    }
    
    protected PackageName getPackageId() {
	return new PackageName(getVariable(EmergeVariable.CATEGORY), 
		getVariable(EmergeVariable.PN), getVariable(EmergeVariable.PV));
    }

}
