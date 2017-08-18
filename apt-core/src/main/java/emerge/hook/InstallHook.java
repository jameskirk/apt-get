package emerge.hook;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import emerge.conf.Configuration;
import emerge.entity.EmergeVariable;
import emerge.entity.WindowsRegistryEntry;
import emerge.exception.UserException;
import emerge.exception.InternalException;
import emerge.misc.FileHelper;
import emerge.misc.Logger;
import emerge.misc.WinRegistry;
import emerge.system.BinUtils;
import emerge.system.OsType;
import emerge.system.Path;
import emerge.system.ProcessExecutor;

public class InstallHook extends AbstractHook  {

	public static final String statusKeyWindows = "success or error status: ";

	public static final Pattern statusPatternWindows = Pattern.compile("((" + statusKeyWindows + "){1})(\\d+)");

	public InstallHook(Map<String, String> variableMap) {
		super(variableMap);
	}

	@Override
	public String getName() {
		return "einstall";
	}

	@Override
	public void execute() throws InternalException, UserException {
		// recognize file type and execute handler
		String archivedPackagePathFirst = getVariable(EmergeVariable.WORKDIR) + "/" + getVariable(EmergeVariable.P);
		if (OsType.WINDOWS.isCurrent()) {
			if (BinUtils.exists(archivedPackagePathFirst + ".msi").execSilent()) {
				installMsiWindows();
			} else if (BinUtils.exists(archivedPackagePathFirst + ".exe").execSilent()) {
				installExeWindows();
			} else {
				installCopying();
			}
		} else if (OsType.UNIX.isCurrent()) {
			installCopying();
		}
	}

	private void installMsiWindows() throws InternalException, UserException {
		// execute msiexec installer
		String param = "/qn /log " + new Path(Configuration.msiexecLogPath, OsType.UNIX).getNativeValue();
		String command = "cd " + new Path(getVariable(EmergeVariable.PORTAGE_BUILDDIR), OsType.UNIX).getNativeValue()
				+ " && " + "msiexec /i " + getVariable(EmergeVariable.P) + ".msi " + param;

		int status = ProcessExecutor.executeNativeCommand(command);
		// new Path(getVariable(EmergeVariable.PORTAGE_BUILDDIR),
		// PathType.UNIX).getNativeValue() );
		if (status != 0)
			throw new UserException();

		// check install status in msiexec.log
		String fileAsString = "";
		try {
			fileAsString = FileHelper.readFile(Configuration.msiexecLogPath, "UTF-16");
		} catch (InternalException e) {
			throw new UserException(e);
		}
		if ("".equals(fileAsString)) {
			Logger.error("uninstallation failed, msiexec log corrupted");
			throw new UserException();
		}

		String productCodeKey = "ProductCode = ";
		Pattern productCodePattern = Pattern.compile("((" + productCodeKey + "){1})(.)+(\\n)");

		String productCode = FileHelper.findByPattern(fileAsString, productCodePattern);
		productCode = productCode.substring(productCodeKey.length(), productCode.length() - 1);
		Logger.debug("found Product Code from installer: " + productCode);

		String msiStatus = FileHelper.findByPattern(fileAsString, statusPatternWindows);
		msiStatus = msiStatus.substring(statusKeyWindows.length(), msiStatus.length());
		Logger.debug("installation msi status: " + msiStatus);
		if (!"0".equals(msiStatus)) {
			Logger.error("installation failed, msi status: " + msiStatus);
			throw new UserException();
		}

		// init variable WINDOWS_PRODUCT_CODE
		variableMap.put(EmergeVariable.WINDOWS_PRODUCT_CODE.name(), productCode);
	}

	private void installCopying() throws InternalException, UserException {
		// check installDir
		if (!BinUtils.exists(getVariable(EmergeVariable.INSTALLDIR)).execSilent()) {
			BinUtils.mkdir(getVariable(EmergeVariable.INSTALLDIR)).exec();
		}

		// copy files buildDir -> installDir
		BinUtils.mv(getVariable(EmergeVariable.WORKDIR) + "/*", getVariable(EmergeVariable.INSTALLDIR)).exec();
	}

	private void installExeWindows() throws InternalException, UserException {
		// execute exe installer
		String param = "/S ";
		String command = getVariable(EmergeVariable.PORTAGE_BUILDDIR) + "/" + getVariable(EmergeVariable.P) + ".exe "
				+ param;

		System.out.println(command);
		int status = ProcessExecutor.executeUnixCommand(command);
		if (status != 0)
			throw new UserException();

		// check status in registry
		List<WindowsRegistryEntry> registryEntries = WinRegistry.getRegistryEntriesWindows();
		for (WindowsRegistryEntry ent : registryEntries) {
			if (ent.getDisplayName().startsWith(getVariable(EmergeVariable.NAME_IN_REGISTRY))) {
				variableMap.put(EmergeVariable.WINDOWS_PRODUCT_CODE.name(), ent.getProductCode());
			}
		}
		/*
		 * String uninstall64 =
		 * "Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall";
		 * try { List<String> val =
		 * WinRegistry.readStringSubKeys(WinRegistry.HKEY_LOCAL_MACHINE,
		 * uninstall64);
		 * 
		 * for (String v : val) { // System.out.println(v); String displayName =
		 * WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, uninstall64
		 * +"\\"+ v, "DisplayName"); String registryName; String productCode;
		 * try { registryName =
		 * ServiceLocator.getInstance().getEbuildRepositoryReader().
		 * readExactlyOne(getPackageId()).getRegistryName(); } catch
		 * (InternalException e) { throw new ExecutionException(e); } if
		 * (registryName == null) { throw new ExecutionException(); } if
		 * (displayName != null && displayName.contains(registryName)) {
		 * productCode = v; Logger.debug("read from registry: displayName=" +
		 * displayName + " productCode=" + productCode);
		 * variableMap.put(EmergeVariable.WINDOWS_PRODUCT_CODE.name(),
		 * productCode); } } } catch (IllegalArgumentException |
		 * IllegalAccessException | InvocationTargetException e) {
		 * Logger.debug(e.getMessage()); throw new ExecutionException(e); }
		 */
	}

}
