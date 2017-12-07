package apt.program;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import apt.conf.Configuration;
import apt.entity.AptGetKeyword;
import apt.entity.WindowsRegistryEntry;
import apt.exception.InternalException;
import apt.misc.FileHelper;
import apt.misc.Logger;
import apt.misc.WinRegistry;
import apt.system.OsType;
import apt.system.Path;
import apt.system.ProcessExecutor;

public class EInstall {
	
	ProgramContext programmContext = new ProgramContext();
	
	public static int main(String args[]) {
		try {
			EInstall eInstall = new EInstall();
			return eInstall.run(args);
		} catch (RuntimeException e) {
			return 1;
		}
	}
	
	public int run(String args[]) {
		ProgramContext programmContext = new ProgramContext();
		// find exe file
		String exeFileName = null;
		for (String file: new File(programmContext.getVariable(AptGetKeyword.WORKDIR)).list()) {
			if ((file.endsWith(".exe") || file.endsWith(".msi")) 
					&& new File(programmContext.getVariable(AptGetKeyword.WORKDIR)).isFile()) {
				if (exeFileName != null) {
					exeFileName = file;
				} else {
					throw new IllegalStateException("exe file duplicated " + exeFileName + " and " + file);
				}
			}
		}
		if (exeFileName == null) {
			throw new IllegalStateException("exe file not found");
		}
		
		// install
		if (exeFileName.endsWith(".exe")) {
			installExeWindows(exeFileName);
		} else if (exeFileName.endsWith(".msi")) {
			installMsiWindows(exeFileName);
		}
		
		return 0;
	}
	
	private void installExeWindows(String exeFileName) {
		// execute exe installer
		final String argSilentInstallExe = "/S ";
		String command = programmContext.getVariable(AptGetKeyword.WORKDIR) + "/" + exeFileName + ".exe" + " "
				+ argSilentInstallExe;

		System.out.println(command);
		int status = ProcessExecutor.executeUnixCommand(command);
		if (status != 0) {
			throw new IllegalStateException("status=" + status);
		}

		// check status in registry
		try {
			List<WindowsRegistryEntry> registryEntries = WinRegistry.getRegistryEntriesWindows();
			for (WindowsRegistryEntry ent : registryEntries) {
				if (ent.getDisplayName().startsWith(programmContext.getVariable(AptGetKeyword.REGISTRY_NAME))) {
					programmContext.setVariable(AptGetKeyword.INSTALL_REGISTRY_PRODUCT_CODE, ent.getProductCode());
				}
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void installMsiWindows(String exeFileName) {
		// execute msiexec installer
		String param = "/qn /log " + new Path(Configuration.msiexecLogPath, OsType.UNIX).getNativeValue();
		String command = "cd " + new Path(programmContext.getVariable(AptGetKeyword.WORKDIR), OsType.UNIX).getNativeValue()
				+ " && " + "msiexec /i" + " " + exeFileName + " " + param;

		int status = ProcessExecutor.executeNativeCommand(command);
		if (status != 0) {
			throw new IllegalStateException("status=" + status);
		}

		// check install status in msiexec.log
		String fileAsString = "";
		try {
			fileAsString = FileHelper.readFile(Configuration.msiexecLogPath, "UTF-16");
		} catch (InternalException e) {
			throw new IllegalStateException(e);
		}
		if ("".equals(fileAsString)) {
			Logger.error("uninstallation failed, msiexec log corrupted");
			throw new IllegalStateException("uninstallation failed, msiexec log corrupted");
		}

		final String productCodeKey = "ProductCode = ";
		Pattern productCodePattern = Pattern.compile("((" + productCodeKey + "){1})(.)+(\\n)");

		String productCode = FileHelper.findByPattern(fileAsString, productCodePattern);
		productCode = productCode.substring(productCodeKey.length(), productCode.length() - 1);
		Logger.debug("found Product Code from installer: " + productCode);

		final String statusKeyWindows = "success or error status: ";
		final Pattern statusPatternWindows = Pattern.compile("((" + statusKeyWindows + "){1})(\\d+)");
		String msiStatus = FileHelper.findByPattern(fileAsString, statusPatternWindows);
		msiStatus = msiStatus.substring(statusKeyWindows.length(), msiStatus.length());
		Logger.debug("installation msi status: " + msiStatus);
		if (!"0".equals(msiStatus)) {
			Logger.error("installation failed, msi status: " + msiStatus);
			throw new IllegalStateException("installation failed, msi status: " + msiStatus);
		}

		programmContext.setVariable(AptGetKeyword.INSTALL_REGISTRY_PRODUCT_CODE, productCode);
	}

}
