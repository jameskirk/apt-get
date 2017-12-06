package apt.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import apt.conf.Configuration;
import apt.misc.Logger;

public class ProcessExecutor {
    
    public static String rootUnixPathFromWindows = Configuration.cygwinPath;
    
    public static String rootWindowsPathFromUnix = "/cygdrive";
    
    
    public static int executeNativeCommand(String command) {
	Logger.debug(command);
	try {
	    ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
	    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
	    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
	    
	    Process process = processBuilder.start();

	    while (process.isAlive()) {
		try {
		    Thread.sleep(500);
		} catch (InterruptedException e) {
		    Logger.debug("executed command was interrupted: " + e.getMessage());
		    return 1;
		}
	    }
	    return process.exitValue();
	} catch (IOException e) {
	    Logger.debug("can not execute command: " +  command + ". Reason: " + e.getMessage());
	    return 1;
	}
    }
    
    public static int executeUnixCommand(String command) {
	Logger.debug(command);
	try {
	    ProcessBuilder processBuilder = new ProcessBuilder(rootUnixPathFromWindows + "/" + BinUtils.bash, "-c", command);
	    processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
	    processBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
	    
	    Process process = processBuilder.start();
	    
	    toStream(process.getInputStream(), Logger.loggerStream);
	    toStream(process.getErrorStream(), Logger.loggerStream);

	    while (process.isAlive()) {
		try {
		    Thread.sleep(100);
		} catch (InterruptedException e) {
		    Logger.debug("executed command was interrupted: " + e.getMessage());
		    return 1;
		}
	    }
	    return process.exitValue();
	} catch (IOException e) {
	    Logger.debug("can not execute command: " +  command + ". Reason: " + e.getMessage());
	    return 1;
	}
    }
    
    public static String toUnixPath(String windowsPath) {
	if (windowsPath.contains(":")) {
	    return rootWindowsPathFromUnix + "/" + windowsPath.replace(":", "");
	} else  {
	    return windowsPath;
	}
    }
    
    public static void toStream(InputStream input, OutputStream output) throws IOException {
	byte[] buffer = new byte[1024]; // Adjust if you want
	int bytesRead;
	while ((bytesRead = input.read(buffer)) != -1) {
	    output.write(buffer, 0, bytesRead);
	}
    }

}
