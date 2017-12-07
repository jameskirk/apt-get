package apt.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import apt.conf.Configuration;
import apt.ebuild.InstalledEbuildFile;
import apt.entity.PackageName;
import apt.exception.InternalException;
import apt.exception.UserException;
import apt.misc.Logger;
import apt.repo.InstalledPackages;
import apt.repo.LocalRepoPackages;
import apt.repo.SearchCriteria;
import apt.system.BinUtils;
import apt.util.ServiceLocator;

public class AptGet {
    
    private LocalRepoPackages localRepoPakcages = new LocalRepoPackages();
    
    public AptGet() {
    	initConfigurationFiles();
    }
    
    public void install(PackageName packageId) throws InternalException, UserException {
	throw new UnsupportedOperationException();
    }

    public void remove(PackageName ebuildId) throws InternalException, UserException {
	throw new UnsupportedOperationException();
    }

    public void update(PackageName ebuildId) throws InternalException, UserException {
	throw new UnsupportedOperationException();
    }
    
    public List<ProgrammInfo> find(PackageName userInput) throws InternalException, UserException {
	List<ProgrammInfo> retVal = new ArrayList<ProgrammInfo>();
	List<PackageInfo> packageInfos = findQuiet(userInput, SearchCriteria.CONTAINS_NAME_ANY_VERSION);
	for (PackageInfo pi : packageInfos) {
	    retVal.add(new ProgrammInfo(pi));
	}
	return retVal;
    }

    public void info(PackageName ebuildId) throws InternalException, UserException {
	throw new UnsupportedOperationException();
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

	    Logger.user("Synchronization successful");
	} catch (Exception e) {
	    Logger.user("Synchronization failed");
	    throw new InternalException(e);
	}

    }

    private List<PackageInfo> findQuiet(PackageName packageId, SearchCriteria searchCriteria) throws InternalException, UserException {
	try {
	    List<PackageInfo> packageInfos = localRepoPakcages.readMergedEbuildsByCriteria(packageId,
		    searchCriteria);

	    InstalledPackages installedPackages = new InstalledPackages();
	    // calculate installed by windows installer
	    for (PackageInfo pi : packageInfos) {
		PackageName ebuildPackageName = pi.getAvailablePackages().get(0).getPackageId();
		List<InstalledEbuildFile> installedRegistryList = installedPackages.getInstalledByRegistry(ebuildPackageName,
			pi.getAvailablePackages());
		pi.getInstalledPackages().addAll(installedRegistryList);
		List<InstalledEbuildFile> installedManuallyList = installedPackages.getInstalledByManually(ebuildPackageName,
			pi.getAvailablePackages().get(0));
		pi.getInstalledPackages().addAll(installedManuallyList);
		
	    }
	    return packageInfos;
	    
	} catch (Exception e) {
	    Logger.user("Internal error, can not find package " + packageId);
	    throw new InternalException(e);
	}
    }
    
    private void initConfigurationFiles() {
	if (!new File(Configuration.emergeProgramDir).exists()) {
		Logger.info("creating .apt-get dir");
		new File(Configuration.emergeProgramDir).mkdir();
	}
	if (!new File(Configuration.ebuildRepositoryDir).exists()) {
		Logger.info("creating .apt-get/repository dir");
		new File(Configuration.ebuildRepositoryDir).mkdir();
	}
	if (!new File(Configuration.installedRepositoryDir).exists()) {
		Logger.info("creating .apt-get/installedRepositoryDir dir");
		new File(Configuration.installedRepositoryDir).mkdir();
	}
	if (!new File(Configuration.makeConfPath).exists()) {
		Logger.info("creating .apt-get/make.conf");
		InputStream ddlStream = this.getClass().getClassLoader().getResourceAsStream("make.conf");
		try (FileOutputStream fos = new FileOutputStream(Configuration.makeConfPath);) {
			byte[] buf = new byte[2048];
			int r;
			while (-1 != (r = ddlStream.read(buf))) {
				fos.write(buf, 0, r);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    }
    
    
}
