package apt.repo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import apt.api.PackageInfo;
import apt.conf.Configuration;
import apt.ebuild.EbuildFile;
import apt.ebuild.EbuildReader;
import apt.entity.PackageName;
import apt.exception.InternalException;
import apt.misc.FileHelper;
import apt.misc.Logger;
import apt.misc.NameDeterminer;
import apt.system.BinUtils;
import apt.system.OsType;
import apt.system.Path;

public class LocalRepoPackages {

    private String ebuildRepositoryDir;

    public LocalRepoPackages() {
	ebuildRepositoryDir = Configuration.ebuildRepositoryDir;
    }

//    public EbuildFile readExactlyOne(PackageName packageId) throws InternalException {
//	List<EbuildFile> retVal = readMergedEbuildsByCriteria(packageId, SearchCriteria.EXACTLY_PACKAGE_ID);
//	if (retVal.size() != 1) {
//	    return null;
//	} else {
//	    return retVal.get(0);
//	}
//    }
    
    /**
     * packageId = 7zip , 7zip-4.9 , app/7zip , app/7zip-4.9
     */
    public List<PackageInfo> readMergedEbuildsByCriteria(PackageName packageId, SearchCriteria criteria) throws InternalException {
	List<PackageInfo> packageInfos = readByCriteria(packageId, SearchCriteria.CONTAINS_NAME_ANY_VERSION);
	for (PackageInfo pi: packageInfos) {
	    if (pi.getCommonEbuild() != null) {
		// merge
    	    	for (EbuildFile ebuild: pi.getAvailablePackages()) {
    	    	    if (pi.getCommonEbuild().getHomepage() != null) {
    	    		ebuild.setHomepage(pi.getCommonEbuild().getHomepage());
    	    	    }
    	    	    if (pi.getCommonEbuild().getDescription() != null) {
	    		ebuild.setDescription(pi.getCommonEbuild().getDescription());
	    	    }
    	    	    // TODO: for other variables
    	    	}
	    }
	}
	return packageInfos;
	
    }

    /**
     * packageId = 7zip , 7zip-4.9 , app/7zip , app/7zip-4.9
     */
    private List<PackageInfo> readByCriteria(PackageName packageId, SearchCriteria criteria) throws InternalException {
	List<PackageInfo> retVal = new ArrayList<PackageInfo>();
	
	File ebuildRepoDir = new File(new Path(ebuildRepositoryDir, OsType.UNIX).getNativeValue());

	for (String category : ebuildRepoDir.list()) {
	    String categoryPath = ebuildRepoDir + "/" + category;
	    if (new File(categoryPath).isDirectory()) {

		for (String packageName : new File(categoryPath).list()) {
		    String packageNamePath = ebuildRepoDir + "/" + category + "/" + packageName;
		    if (new File(packageNamePath).isDirectory()) {
			
			PackageInfo packageInfo = new PackageInfo();
			for (String ebuildFilename : new File(packageNamePath).list()) {
			    String ebuildPath = ebuildRepoDir + "/" + category + "/" + packageName + "/" + ebuildFilename;
			    if (ebuildFilename.startsWith(packageName) && new File(ebuildPath).isFile() && new File(ebuildPath).getPath().endsWith(".ebuild")) {
				String version = NameDeterminer.parseCategoryNameVersion(ebuildFilename.replace(".ebuild", "")).getVersion();

				if (criteria == SearchCriteria.CONTAINS_NAME_ANY_VERSION) {
				    if ((!packageId.getCategory().isEmpty() && category.contains(packageId.getCategory()) || packageId.getCategory().isEmpty())
					    && (!packageId.getPackageName().isEmpty() && packageName.contains(packageId.getPackageName()) || packageId.getPackageName().isEmpty())) {
					
					PackageName concretePackageId = new PackageName(category, packageName, version);
					EbuildFile ebuild = new EbuildReader().readConcreteEbuild(concretePackageId);
					EbuildFile commonEbuild = new EbuildReader().readConcreteEbuild(concretePackageId);
					packageInfo.getAvailablePackages().add(ebuild);
					packageInfo.setCommonEbuild(commonEbuild);
				    }
				} else if (criteria == SearchCriteria.EXACTLY_NAME_ANY_VERSION) {
				    if ((!packageId.getCategory().isEmpty() && category.equals(packageId.getCategory()) || packageId.getCategory().isEmpty())
					    && (!packageId.getPackageName().isEmpty() && packageName.equals(packageId.getPackageName()))) {
					
					PackageName concretePackageId = new PackageName(category, packageName, version);
					EbuildFile ebuild = new EbuildReader().readConcreteEbuild(concretePackageId);
					EbuildFile commonEbuild = new EbuildReader().readConcreteEbuild(concretePackageId);
					packageInfo.getAvailablePackages().add(ebuild);
					packageInfo.setCommonEbuild(commonEbuild);
				    }
				}else if (criteria == SearchCriteria.EXACTLY_PACKAGE_ID) {
				    if ((!packageId.getCategory().isEmpty() && category.equals(packageId.getCategory()) || packageId.getCategory().isEmpty())
					    && (!packageId.getPackageName().isEmpty() && packageName.equals(packageId.getPackageName()))
					    && (!packageId.getVersion().isEmpty() && version.equals(packageId.getVersion()))) {
					
					PackageName concretePackageId = new PackageName(category, packageName, version);
					EbuildFile ebuild = new EbuildReader().readConcreteEbuild(concretePackageId);
					EbuildFile commonEbuild = new EbuildReader().readConcreteEbuild(concretePackageId);
					packageInfo.getAvailablePackages().add(ebuild);
					packageInfo.setCommonEbuild(commonEbuild);
				    }
				}
			    }
			}
			if (!packageInfo.getAvailablePackages().isEmpty()) {
			    retVal.add(packageInfo);
			}
		    }
		}
	    }
	}

	return retVal;
    }

    public void remove(PackageName packageId) throws InternalException {
	BinUtils.rm(ebuildRepositoryDir + FileHelper.getEbuildPath(packageId));
    }

}
