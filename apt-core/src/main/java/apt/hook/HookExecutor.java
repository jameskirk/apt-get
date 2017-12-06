package apt.hook;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import apt.exception.InternalException;
import apt.exception.UserException;

public class HookExecutor {
    
    public void executeInstall(Map<String, String> variableMap) throws InternalException, UserException {
	List<Hook> hooks = Arrays.asList(
		new DownloadHook(variableMap),
		new UnpackHook(variableMap),
		new InstallHook(variableMap));
	
	for (Hook hook : hooks) {
	    hook.execute();
	}
    }

}
