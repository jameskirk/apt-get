package apt.api;

import java.util.ArrayList;
import java.util.List;

import apt.ebuild.EbuildFile;
import apt.entity.Keyword;
import apt.entity.PackageName;
import apt.exception.InternalException;
import apt.exception.UserException;
import apt.repo.EbuildLocalRepositoryReader;
import apt.repo.SearchCriteria;
import apt.repo.entity.InstalledPackageEntry;

public class AptGetInfo {

    private EbuildLocalRepositoryReader ebuildLocalRepositoryReader = new EbuildLocalRepositoryReader();

    public void info(PackageName packageId) throws UserException, InternalException {

    }

    public List<PackageInfo> find(PackageName packageIdForSearch) throws InternalException, UserException {
	List<PackageInfo> retVal = new ArrayList<PackageInfo>();
	List<EbuildFile> ebuilds = ebuildLocalRepositoryReader.readByCriteria(packageIdForSearch,
		SearchCriteria.CONTAINS_NAME_ANY_VERSION);
	List<EbuildFile> commonEbuilds = ebuildLocalRepositoryReader.readAllCommonEbuilds();

	// calculate available
	for (EbuildFile ebuild : ebuilds) {
	    PackageName ebuildPackageId = ebuild.getPackageId();
	    PackageInfo packageInfo = findPackageInfo(retVal, ebuildPackageId);
	    packageInfo.getAvailablePackages().add(ebuild.getPackageId());
	}
	for (EbuildFile ebuild : commonEbuilds) {
	    PackageInfo packageInfo = findPackageInfo(retVal, ebuild.getPackageId());
	    packageInfo.setHomepage(ebuild.getHomepage());
	    packageInfo.getAvailablePackages().add(ebuild.getPackageId());
	}

	InstalledPackages installedPackages = new InstalledPackages();
	// calculate installed by windows installer
	for (EbuildFile commonEbuild : commonEbuilds) {
	    PackageName ebuildPackageName = commonEbuild.getPackageId();
	    List<InstalledPackageEntry> installedList = installedPackages.getInstalledByRegistry(ebuildPackageName, commonEbuilds);
	    for (InstalledPackageEntry installed : installedList) {
		PackageInfo packageInfo = findPackageInfo(retVal, ebuildPackageName);
		packageInfo.setHomepage(commonEbuild.getHomepage());
		packageInfo.getInstalledPackages().add(new PackageName(ebuildPackageName.getCategory(), ebuildPackageName.getPackageName(),
			installed.getPackageId().getVersion(), new Keyword("x86")));
	    }
	}

	// calculate installed by manually
	for (EbuildFile commonEbuild : commonEbuilds) {
	    PackageName ebuildPackageName = commonEbuild.getPackageId();
	    List<InstalledPackageEntry> installedList = installedPackages.getInstalledByManually(ebuildPackageName, commonEbuild,
		    new Keyword("x86-noinstaller"));
	    for (InstalledPackageEntry installed : installedList) {
		PackageInfo packageInfo = findPackageInfo(retVal, ebuildPackageName);
		packageInfo.setHomepage(commonEbuild.getHomepage());
		packageInfo.getInstalledPackages().add(new PackageName(ebuildPackageName.getCategory(), ebuildPackageName.getPackageName(),
			installed.getPackageId().getVersion(), new Keyword("x86-noinsaller")));
	    }
	}
	return retVal;

    }

    private PackageInfo findPackageInfo(List<PackageInfo> packageInfos, PackageName ebuildPackageId) {
	PackageInfo retVal = new PackageInfo();
	boolean packageInfoContains = false;
	for (PackageInfo packageInfo : packageInfos) {
	    if (packageInfo.getPackageName().getCategory().equals(ebuildPackageId.getCategory())
		    && packageInfo.getPackageName().getPackageName().equals(ebuildPackageId.getPackageName())) {
		packageInfoContains = true;
		return packageInfo;
	    }
	}
	if (!packageInfoContains) {
	    packageInfos.add(retVal);
	    retVal.setPackageName(new PackageName(ebuildPackageId.getCategory(), ebuildPackageId.getPackageName(), null));
	}
	return retVal;
    }

}
