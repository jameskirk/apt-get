package emerge.core;

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
import emerge.exception.UserException;
import emerge.exception.InternalException;
import emerge.hook.HookExecutor;
import emerge.misc.FileHelper;
import emerge.misc.Logger;
import emerge.misc.NameDeterminer;
import emerge.repo.SearchCriteria;
import emerge.repo.entity.InstalledPackageEntry;
import emerge.system.OsType;
import emerge.system.Path;
import emerge.util.ServiceLocator;

public class InstallHandler implements IInstallHandler {

	protected PackageName packageId;

	protected Keyword keywordForInstall;

	protected List<Keyword> useForInstall = new ArrayList<Keyword>();

	protected String installDir;

	// must contain only UNIX path
	protected Map<String, String> variableMap = new HashMap<String, String>();

	@Override
	public final void execute(PackageName packageId) throws InternalException, UserException {
		Logger.debug("install " + packageId);
		this.packageId = packageId;
		if (this.packageId == null) {
			throw new UserException("illegal package name:" + packageId);
		}

		checkIfInstalled();
		checkFlags();
		install();
		writeInInstalledDbFile();

		Logger.info("package " + packageId + " installed successfully");
	}

	@Override
	public void checkIfInstalled() throws InternalException, UserException {
//		List<InstalledPackageEntry> entry = ServiceLocator.getInstance().getInstalledRepositoryReader()
//				.readByCriteria(packageId.toString(), SearchCriteria.EXACTLY_NAME_ANY_VERSION);
//		if (entry.size() >= 1) {
//			Logger.user("package " + entry.get(0).getPackageId() + " already installed");
//			throw new UserException();
//		}
	}

	@Override
	public void checkFlags() throws InternalException, UserException {
		// compare keywords and use from userSettings and ebuildFile
		UserSettings userSettings = getUserSettingReader().read();
		EbuildFile ebuildFile = getEbuildReader().readConcreteEbuild(packageId);

		// check keywords
		List<Keyword> keywordsToUnmask = new ArrayList<Keyword>();
		for (Keyword acceptKeyword : userSettings.getAcceptKeywords()) {
			Optional<Keyword> keyword = ebuildFile.getKeywords().stream()
					.filter(k -> acceptKeyword.getValue().equals(k.getValue())).findFirst();
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
			throw new UserException(
					"can not install package " + packageId + ", please unmask keywords: " + keywordsToUnmask);
		}

		// check use, iuse
		List<Keyword> useToUnmask = new ArrayList<Keyword>();
		for (Keyword iuse : ebuildFile.getIuse()) {
			Optional<Keyword> use = userSettings.getUse().stream().filter(u -> iuse.getValue().equals(u.getValue()))
					.findFirst();
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
			throw new UserException(
					"can not install package " + packageId + " , please set USE to " + useToUnmask);
		}

		// check requiredUse
		Logger.debug("keyword for installation: " + keywordForInstall + "; USE for installation: " + useForInstall);

	}

	@Override
	public void install() throws InternalException, UserException {
		variableMap.put(EmergeVariable.CATEGORY.name(), packageId.getCategory());
		variableMap.put(EmergeVariable.PN.name(), packageId.getPackageName());
		variableMap.put(EmergeVariable.PV.name(), packageId.getVersion());
		variableMap.put(EmergeVariable.P.name(), packageId.getPackageName() + "-" + packageId.getVersion());

		variableMap.put(EmergeVariable.PORTAGE_BUILDDIR.name(),
				Configuration.packageRepositoryDir + FileHelper.getEbuildPathFirst(packageId));

		variableMap.put(EmergeVariable.WORKDIR.name(),
				variableMap.get(EmergeVariable.PORTAGE_BUILDDIR.name()) + "/work");

		if (OsType.WINDOWS.isCurrent()) {
			String nativeInstallDir = "C:/Program\\ Files\\ \\(x86\\)";
			variableMap.put(EmergeVariable.INSTALLDIR.name(),
					new Path(nativeInstallDir).getValue(OsType.UNIX) + "/" + packageId.getPackageName());
		}

		variableMap.put(EmergeVariable.INSTALL_KEYWORD.name(), keywordForInstall.getValue());
		variableMap.put(EmergeVariable.NAME_IN_REGISTRY.name(),
				getEbuildReader().readConcreteEbuild(packageId).getRegistryName());

		HookExecutor hookExecutor = new HookExecutor();
		hookExecutor.executeInstall(variableMap);
	}

	@Override
	public void writeInInstalledDbFile() throws InternalException, UserException {
		InstalledPackageEntry entry = new InstalledPackageEntry();
		entry.setPackageId(packageId);
		entry.setKeyword(keywordForInstall);
		entry.setUse(useForInstall);
		entry.setInstallDir(variableMap.get(EmergeVariable.INSTALLDIR.name()));

		if (OsType.WINDOWS.isCurrent()) {
			entry.setProductCode(variableMap.get(EmergeVariable.WINDOWS_PRODUCT_CODE.name()));
		}

		//ServiceLocator.getInstance().getInstalledRepositoryReader().saveOrUpdate(entry);
	}

	public UserSettingsReader getUserSettingReader() {
		return new UserSettingsReader();
	}

	public EbuildReader getEbuildReader() throws InternalException {
		return new EbuildReader();
	}

}
