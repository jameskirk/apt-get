package emerge.api;

import java.util.ArrayList;
import java.util.List;

import emerge.conf.Configuration;
import emerge.core.ActionHandler;
import emerge.core.InstallHandler;
import emerge.core.RemoveHandler;
import emerge.ebuild.EbuildFile;
import emerge.entity.PackageDetailsInfo;
import emerge.entity.PackageName;
import emerge.exception.InternalException;
import emerge.exception.UserException;
import emerge.misc.Logger;
import emerge.repo.SearchCriteria;
import emerge.system.BinUtils;
import emerge.util.ServiceLocator;

public class AptGet {

    public void install(PackageName packageId) throws InternalException, UserException {
	try {
	    packageId = recognize(packageId, true, false);
	    AptGetInstall aptGetInstall = new AptGetInstall();
	    aptGetInstall.install(packageId);

	} catch (RuntimeException e) {
	    Logger.user("Installation failed");
	    throw new InternalException(e);
	}
    }

    public void remove(PackageName ebuildId) throws InternalException, UserException {
	throw new UnsupportedOperationException();
    }

    public void update(PackageName ebuildId) throws InternalException, UserException {
	throw new UnsupportedOperationException();
    }
    
    public List<PackageInfo> find(PackageName userInput) throws InternalException, UserException {
	try {
	    List<PackageInfo> retVal = new ArrayList<PackageInfo>();
	    AptGetInfo aptGetInfo = new AptGetInfo();
	    retVal = aptGetInfo.find(userInput);
	    return retVal;
	} catch (Exception e) {
	    Logger.user("Internal error, can not find package " + userInput);
	    throw new InternalException(e);
	}
    }

    public void info(PackageName ebuildId) throws InternalException, UserException {
	throw new UnsupportedOperationException();
    }

    private PackageName recognize(PackageName userInput, boolean useLatestVersion, boolean useInstalledVersion)
	    throws InternalException, UserException {
	List<PackageDetailsInfo> infos = findQuiet(userInput, SearchCriteria.EXACTLY_NAME_ANY_VERSION);
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

    public void sync() throws InternalException, UserException {
	try {
	    Logger.user(">>> Sync with central repository");
	    // TODO: download ebuild repo, clear local repo, install to local
	    String uri = "http://github.com/jameskirk/apt-get-repository/archive/master.zip";
	    String ebuildRepoDir = Configuration.ebuildRepositoryDir;
	    Logger.user(">>> Downloading files");
	    BinUtils.wget(uri, ebuildRepoDir + "/master.zip", ServiceLocator.getInstance().getUserSettingReader().read().getProxy()).exec();

	    // remove only dirs
	    BinUtils.rmForce(ebuildRepoDir + "/*/").exec();

	    BinUtils.unarchive(ebuildRepoDir + "/master.zip", ebuildRepoDir).exec();

	    BinUtils.mv(ebuildRepoDir + "/apt-get-repository-master/*", ebuildRepoDir).exec();

	    BinUtils.rmForce(ebuildRepoDir + "/apt-get-repository-master").exec();

	    ServiceLocator.getInstance().getEbuildRepositoryReader().cache();
	    // ServiceLocator.getInstance().getInstalledRepositoryReader().cache();
	    Logger.user("Synchronization successful");
	} catch (Exception e) {
	    Logger.user("Synchronization failed");
	    throw new InternalException(e);
	}

    }

    public void firstInit() throws InternalException, UserException {
	try {
	    Logger.user(">>> First initialization");
	    ServiceLocator.getInstance().getEbuildRepositoryReader().cache();
	    // ServiceLocator.getInstance().getInstalledRepositoryReader().cache();
	    Logger.user("Initialization successful");
	} catch (RuntimeException e) {
	    Logger.user("Initialization failed");
	    throw new InternalException(e);
	}
    }

    private List<PackageDetailsInfo> findQuiet(PackageName packageId, SearchCriteria criteria) throws InternalException, UserException {
	List<PackageDetailsInfo> retVal = new ArrayList<PackageDetailsInfo>();

	if (packageId == null) {
	    throw new UserException("entered package name :" + packageId + " is incorrect. Please enter package name in correct format");
	}

	if (criteria == SearchCriteria.CONTAINS_NAME_ANY_VERSION) {
	    // packageId.setVersion("");
	} else if (criteria == SearchCriteria.EXACTLY_PACKAGE_ID) {
	    // packageId.setVersion("");
	}
	// List<InstalledPackageEntry> entries =
	// ServiceLocator.getInstance().getInstalledRepositoryReader()
	// .readByCriteria(packageId.toString(), criteria);
	List<EbuildFile> ebuilds = ServiceLocator.getInstance().getEbuildRepositoryReader().readByCriteria(packageId, criteria);

	// for (InstalledPackageEntry entry : entries) {
	//
	// List<EbuildFile> ebuildInInfo = new ArrayList<EbuildFile>();
	// List<EbuildFile> toRemove = new ArrayList<EbuildFile>();
	// for (EbuildFile e : ebuilds) {
	// if
	// (entry.getPackageId().getCategory().equals(e.getPackageId().getCategory())
	// &&
	// entry.getPackageId().getPackageName().equals(e.getPackageId().getPackageName()))
	// {
	// toRemove.add(e);
	// ebuildInInfo.add(e);
	// }
	// }
	// ebuilds.removeAll(toRemove);
	// PackageDetailsInfo info = new PackageDetailsInfo(entry,
	// ebuildInInfo);
	// retVal.add(info);

	// }
	//
	// Map<String, List<EbuildFile>> map = new HashMap<String,
	// List<EbuildFile>>();
	// for (EbuildFile e : ebuilds) {
	// String key = e.getPackageId().getCategory() + "/" +
	// e.getPackageId().getPackageName();
	// if (map.get(key) == null) {
	// map.put(key, new ArrayList<EbuildFile>());
	// }
	// map.get(key).add(e);
	// }
	// for (List<EbuildFile> eList : map.values()) {
	// retVal.add(new PackageDetailsInfo(null, eList));
	// }
	return retVal;
    }

}
