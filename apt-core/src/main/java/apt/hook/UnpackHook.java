package apt.hook;

import java.util.Map;

import apt.entity.EmergeVariable;
import apt.exception.InternalException;
import apt.exception.UserException;
import apt.misc.Logger;
import apt.system.BinUtils;
import apt.system.ProcessExecutor;

public class UnpackHook extends AbstractHook {

    public UnpackHook(Map<String, String> variableMap) {
	super(variableMap);
    }

    @Override
    public String getName() {
	return "eunpack";
    }

    @Override
    public void execute() throws InternalException, UserException {
	String packageNameVersion = getVariable(EmergeVariable.P);
	String buildDir = getVariable(EmergeVariable.PORTAGE_BUILDDIR);
	String workDir = getVariable(EmergeVariable.WORKDIR);

	// clean workDir
	if (!BinUtils.exists(workDir).execSilent()) {
	    BinUtils.mkdir(workDir).exec();
	}
	BinUtils.rmForce(workDir + "/*").exec();

	// recognize archive type
	String archivator;
	String archivatorFlags;
	String downloadedFilePath = buildDir + "/" + packageNameVersion;
	if (BinUtils.exists(downloadedFilePath + ".zip").execSilent()) {
	    archivator = BinUtils.unzip;
	    archivatorFlags = " -d " + workDir;
	    downloadedFilePath = downloadedFilePath + ".zip";
	} else if (BinUtils.exists(downloadedFilePath + ".msi").execSilent()) {
	    BinUtils.ln(downloadedFilePath + ".msi", workDir + "/" + packageNameVersion + ".msi").exec();
	    Logger.debug("crealed hard links, msi file");
	    return;
	} else if (BinUtils.exists(downloadedFilePath + ".exe").execSilent()) {
	    BinUtils.ln(downloadedFilePath + ".exe", workDir + "/" + packageNameVersion + ".exe").exec();
	    Logger.debug("crealed hard links,  exe file");
	    return;
	} else {
	    Logger.error("downloaded package not found or unsupported archival type");
	    throw new UserException();
	}

	// execute archiver
	int status = ProcessExecutor.executeUnixCommand(archivator + downloadedFilePath + archivatorFlags);
	if (status != 0)
	    throw new UserException();

    }

}
