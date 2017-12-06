package emerge.api.impl;

import java.util.List;

import emerge.api.PackageService;
import emerge.api.RepositoryService;
import emerge.core.ActionHandler;
import emerge.core.InstallHandler;
import emerge.core.RemoveHandler;
import emerge.entity.PackageDetailsInfo;
import emerge.entity.PackageName;
import emerge.exception.UserException;
import emerge.exception.InternalException;
import emerge.misc.Logger;
import emerge.misc.NameDeterminer;
import emerge.repo.SearchCriteria;

public class PackageServiceImpl implements PackageService {

    private RepositoryService repositoryService;

    @Override
    public void install(PackageName packageName) throws InternalException, UserException {
	try {
	    // determine msi, exe, no_installer
	    ActionHandler handler = new InstallHandler();
	    handler.execute(recognize(packageName, true, false));

	} catch (RuntimeException e) {
	    Logger.user("Installation failed");
	    throw new InternalException(e);
	}
    }

    @Override
    public void remove(PackageName ebuildId) throws InternalException, UserException {
	try {

	    ActionHandler handler = new RemoveHandler();
	    handler.execute(recognize(ebuildId, false, true));

	} catch (RuntimeException e) {
	    throw new InternalException(e);
	}
    }

    @Override
    public void update(PackageName ebuildId) throws InternalException, UserException {
	throw new UnsupportedOperationException();
    }

    @Override
    public void info(PackageName ebuildId) throws InternalException, UserException {
	try {

	    PackageName packageId = ebuildId;
	    if (packageId == null) {
		throw new UserException("illegal package name:" + ebuildId);
	    }
	    packageId.setVersion("");

	    List<PackageDetailsInfo> infos = repositoryService.find(packageId);
	    if (infos.size() == 0) {
		Logger.info("package " + ebuildId + " not found");
		throw new UserException();
	    }
	    if (infos.size() > 1) {
		Logger.info("found several packages, please specify package name");
		throw new UserException();
	    }
	    PackageDetailsInfo info = infos.get(0);

	    Logger.info("* " + info.toString());

	} catch (RuntimeException e) {
	    throw new InternalException(e);
	}

    }

    private PackageName recognize(PackageName userInput, boolean useLatestVersion, boolean useInstalledVersion)
	    throws InternalException, UserException {
	List<PackageDetailsInfo> infos = repositoryService.findQuiet(userInput, SearchCriteria.EXACTLY_NAME_ANY_VERSION);
	if (infos.isEmpty()) {
	    Logger.user("package: " + userInput + " not found in repository");
	    new UserException();
	}
	if (infos.size() > 1) {
	    Logger.user("please specify package name in special format, e.g 'app-arch/p7zip'");
	    infos.stream().forEach(x -> Logger.debug(x.toString()));
	    throw new UserException();
	}
	String version = userInput.getVersion();
	if (useLatestVersion && "".equals(version)) {
	    List<String> versions = infos.get(0).getVersions();
	    version = versions.get(versions.size() - 1);
	}
	if (useInstalledVersion && "".equals(version)) {
	    if (infos.get(0).getInstalledPackage() == null) {
		Logger.user("package is not installed:" + userInput);
		throw new UserException();
	    }
	    version = infos.get(0).getInstalledPackage().getPackageId().getVersion();
	}
	return new PackageName(infos.get(0).getCategory(), infos.get(0).getPackageName(), version);
    }

    public void setRepositoryService(RepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

}
