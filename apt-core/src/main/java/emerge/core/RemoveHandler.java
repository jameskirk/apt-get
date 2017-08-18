package emerge.core;

import emerge.conf.Configuration;
import emerge.entity.PackageName;
import emerge.exception.UserException;
import emerge.exception.InternalException;
import emerge.hook.InstallHook;
import emerge.misc.FileHelper;
import emerge.misc.Logger;
import emerge.misc.NameDeterminer;
import emerge.repo.entity.InstalledPackageEntry;
import emerge.system.BinUtils;
import emerge.system.OsType;
import emerge.system.Path;
import emerge.system.ProcessExecutor;
import emerge.util.ServiceLocator;

public class RemoveHandler implements IRemoveHandler {

    protected PackageName packageId;
    
    protected InstalledPackageEntry installedEntry;
    
    public RemoveHandler() {
    }
    
    @Override
    public final void execute(PackageName packageId) throws InternalException, UserException {
	Logger.debug("removing " + packageId);
	if (this.packageId == null) {
	    throw new UserException("illegal package name:" + packageId);
	}
	
	checkIfInstalled();
	remove();
	writeInInstalledDbFile();
    }
    
    public void checkIfInstalled() throws InternalException, UserException {
	InstalledPackageEntry entry = ServiceLocator.getInstance().getInstalledRepositoryReader().readExactlyOne(packageId);
	if (entry == null) {
	    throw new UserException("package " + packageId + " is not found in db file, can not remove");
	}
	installedEntry = entry;
    }
    
    public void remove() throws InternalException, UserException {
	if (OsType.WINDOWS.isCurrent()) {
	    if (installedEntry.getProductCode() != null && !installedEntry.getProductCode().isEmpty()) {
		removeMsiWin();
		return;
	    }
	}
	removeCopying();
    }

    public void writeInInstalledDbFile() throws InternalException, UserException {
	ServiceLocator.getInstance().getInstalledRepositoryReader().remove(packageId);
    }
    
    private void removeCopying() throws InternalException, UserException {
	String command =  BinUtils.rm + installedEntry.getInstallDir() + " -rf";

	ProcessExecutor.executeUnixCommand(command);
	
	// check state
	if (ProcessExecutor.executeUnixCommand("[ -f " + installedEntry.getInstallDir() + " ]") == 0) {
	    throw new UserException("uninstallation failed, can not remove directory " + installedEntry.getInstallDir());
	}
    }
    
    private void removeMsiWin() throws InternalException, UserException {
	String param = " /qn /log " + new Path(Configuration.msiexecLogPath, OsType.UNIX).getNativeValue();
	String command = "msiexec /x " + installedEntry.getProductCode() + param;

	Logger.debug("executing: " + command);
	ProcessExecutor.executeNativeCommand(command);
	
	// check state
	String fileAsString = FileHelper.readFile(Configuration.msiexecLogPath, "UTF-16");
	if ("".equals(fileAsString)) {
	    throw new UserException("uninstallation failed, msiexec log corrupted");
	}
	String status = FileHelper.findByPattern(fileAsString, InstallHook.statusPatternWindows);
	status = status.substring(InstallHook.statusKeyWindows.length(), status.length());
	Logger.debug("removing msi status: " + status);

	if (!"0".equals(status)) {
	    throw new UserException("uninstallation failed, status: " + status);
	}
    }

}
