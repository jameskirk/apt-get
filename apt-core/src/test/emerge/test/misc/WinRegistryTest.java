package emerge.test.misc;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Test;

import emerge.misc.WinRegistry;

public class WinRegistryTest {
    
    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	List<String> val = WinRegistry.readStringSubKeys(WinRegistry.HKEY_LOCAL_MACHINE, "Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall");
	
	for (String v: val) {
	    //System.out.println(v);
	    String displayName = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\" + v, "DisplayName");
	    String displayVersion = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\" + v, "DisplayVersion");
	    if (displayName != null && displayName.contains("7-Zip")) {
		System.out.println(displayName + " ProductCode=" + v + " DisplayVersion=" + displayVersion);
	    }
	}
    }
    
}
