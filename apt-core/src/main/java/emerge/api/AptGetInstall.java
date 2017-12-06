package emerge.api;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import emerge.conf.Configuration;
import emerge.conf.UserSettings;
import emerge.conf.UserSettingsReader;
import emerge.ebuild.EbuildFile;
import emerge.ebuild.EbuildReader;
import emerge.entity.EmergeVariable;
import emerge.entity.Keyword;
import emerge.entity.PackageName;
import emerge.exception.InternalException;
import emerge.exception.UserException;
import emerge.hook.HookExecutor;
import emerge.misc.FileHelper;
import emerge.misc.Logger;
import emerge.repo.EbuildLocalRepositoryReader;
import emerge.system.OsType;
import emerge.system.Path;

public class AptGetInstall {

    private EbuildLocalRepositoryReader ebuildLocalRepositoryReader = new EbuildLocalRepositoryReader();

    private UserSettingsReader userSettingsReader = new UserSettingsReader();

    private EbuildReader ebuildReader = new EbuildReader();

    public void install(PackageName packageId) throws UserException, InternalException {
	Logger.debug("install " + packageId);
	if (packageId == null) {
	    throw new UserException("illegal package name:" + packageId);
	}

	ebuildLocalRepositoryReader.cache(); // optional
	List<EbuildFile> commonEbuilds = ebuildLocalRepositoryReader.readAllCommonEbuilds();
	InstalledPackages installedPackages = new InstalledPackages();
	// 1a.
	if (installedPackages.isInstalledByRegistry(packageId, commonEbuilds)) {
	    throw new UserException("package is already installed by windows installer");
	}
	// 2.
	Map.Entry<Keyword, List<Keyword>> flagsForInstall = checkFlags(packageId);
	// 1b.
	EbuildFile ebuild = ebuildLocalRepositoryReader.readExactlyOne(packageId);
	if (installedPackages.isInstalledByDefaultPath(packageId, ebuild, flagsForInstall.getKey())) {
	    throw new UserException("package is already installed by manually");
	}
	// 3.
	install(packageId, flagsForInstall.getKey());

	Logger.info("package " + packageId + " installed successfully");
    }

    private Map.Entry<Keyword, List<Keyword>> checkFlags(PackageName packageId) throws InternalException, UserException {
	Keyword keywordForInstall = null;
	List<Keyword> useForInstall = new ArrayList<Keyword>();

	// compare keywords and use from userSettings and ebuildFile
	UserSettings userSettings = userSettingsReader.read();
	EbuildFile ebuildFile = ebuildReader.readConcreteEbuild(packageId);

	// check keywords
	List<Keyword> keywordsToUnmask = new ArrayList<Keyword>();
	for (Keyword acceptKeyword : userSettings.getAcceptKeywords()) {
	    Optional<Keyword> keyword = ebuildFile.getKeywords().stream().filter(k -> acceptKeyword.getValue().equals(k.getValue()))
		    .findFirst();
	    if (keyword.isPresent()) {
		if (acceptKeyword.isMasked()) {
		    // ok
		    keywordForInstall = new Keyword(acceptKeyword.getValue());
		    break;
		} else if (!acceptKeyword.isMasked() && !keyword.get().isMasked()) {
		    // ok
		    keywordForInstall = new Keyword(acceptKeyword.getValue());
		    break;
		} else if (!acceptKeyword.isMasked() && keyword.get().isMasked()) {
		    // error, masked in ebuild
		    keywordsToUnmask.add(keyword.get());
		}
	    }
	}
	if (keywordForInstall == null) {
	    throw new UserException("can not install package " + packageId + ", please unmask keywords: " + keywordsToUnmask);
	}

	// check use, iuse
	List<Keyword> useToUnmask = new ArrayList<Keyword>();
	for (Keyword iuse : ebuildFile.getIuse()) {
	    Optional<Keyword> use = userSettings.getUse().stream().filter(u -> iuse.getValue().equals(u.getValue())).findFirst();
	    if (iuse.isPlus()) {
		if (use.isPresent() && use.get().isNoPrefix()) {
		    // ok
		    useForInstall.add(new Keyword(iuse.getValue()));
		} else {
		    // error
		    useToUnmask.add(new Keyword(iuse.getValue()));
		}
	    } else if (iuse.isMinus()) {
		if (!use.isPresent() || (use.isPresent() && use.get().isMinus())) {
		    // ok
		    Keyword temp = new Keyword(iuse.getValue());
		    temp.setMinus(true);
		    useForInstall.add(temp);
		} else {
		    // error
		    Keyword temp = new Keyword(iuse.getValue());
		    temp.setMinus(true);
		    useToUnmask.add(temp);
		}
	    } else if (iuse.isNoPrefix()) {
		if (!use.isPresent() || (use.isPresent() && use.get().isMinus())) {
		    // ok
		    Keyword temp = new Keyword(iuse.getValue());
		    temp.setMinus(true);
		    useForInstall.add(temp);
		} else {
		    // ok
		    useForInstall.add(new Keyword(iuse.getValue()));
		}
	    }
	}
	if (!useToUnmask.isEmpty()) {
	    throw new UserException("can not install package " + packageId + " , please set USE to " + useToUnmask);
	}

	// check requiredUse
	Logger.debug("keyword for installation: " + keywordForInstall + "; USE for installation: " + useForInstall);
	return new AbstractMap.SimpleEntry<Keyword, List<Keyword>>(keywordForInstall, useForInstall);
    }

    private void install(PackageName packageId, Keyword keywordForInstall) throws InternalException, UserException {
	Map<String, String> variableMap = new HashMap<String, String>();
	variableMap.put(EmergeVariable.CATEGORY.name(), packageId.getCategory());
	variableMap.put(EmergeVariable.PN.name(), packageId.getPackageName());
	variableMap.put(EmergeVariable.PV.name(), packageId.getVersion());
	variableMap.put(EmergeVariable.P.name(), packageId.getPackageName() + "-" + packageId.getVersion());

	variableMap.put(EmergeVariable.PORTAGE_BUILDDIR.name(),
		Configuration.packageRepositoryDir + FileHelper.getEbuildPathFirst(packageId));

	variableMap.put(EmergeVariable.WORKDIR.name(), variableMap.get(EmergeVariable.PORTAGE_BUILDDIR.name()) + "/work");

	if (OsType.WINDOWS.isCurrent()) {
	    String nativeInstallDir = "C:/Program\\ Files\\ \\(x86\\)";
	    variableMap.put(EmergeVariable.INSTALLDIR.name(),
		    new Path(nativeInstallDir).getValue(OsType.UNIX) + "/" + packageId.getPackageName());
	}

	variableMap.put(EmergeVariable.INSTALL_KEYWORD.name(), keywordForInstall.getValue());
	variableMap.put(EmergeVariable.NAME_IN_REGISTRY.name(), ebuildReader.readConcreteEbuild(packageId).getRegistryName());

	HookExecutor hookExecutor = new HookExecutor();
	hookExecutor.executeInstall(variableMap);
    }

}
