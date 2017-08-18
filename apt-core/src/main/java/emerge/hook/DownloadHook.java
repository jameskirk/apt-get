package emerge.hook;

import java.util.Map;
import java.util.Optional;

import emerge.ebuild.EbuildFile;
import emerge.entity.EmergeVariable;
import emerge.entity.KeywordGroup;
import emerge.exception.UserException;
import emerge.exception.InternalException;
import emerge.misc.Logger;
import emerge.system.BinUtils;
import emerge.util.ServiceLocator;

public class DownloadHook extends AbstractHook {

	public DownloadHook(Map<String, String> variableMap) {
		super(variableMap);
	}

	@Override
	public String getName() {
		return "edownload";
	}

	@Override
	public void execute() throws InternalException, UserException {

		// determine target uri
		EbuildFile ebuildFile = ServiceLocator.getInstance().getEbuildRepositoryReader().readExactlyOne(getPackageId());

		// we can recognize only x86 : url
		// do not support x86 -kde +gtk: url
		Optional<KeywordGroup> group = ebuildFile.getSrcUriList().stream()
				.filter(x -> x.getKeywords().contains(getVariable(EmergeVariable.INSTALL_KEYWORD))).findFirst();
		if (!group.isPresent()) {
			throw new UserException("SRC_URI not found");
		}

		// check buildDir
		if (!BinUtils.exists(getVariable(EmergeVariable.PORTAGE_BUILDDIR)).execSilent()) {
			BinUtils.mkdir(getVariable(EmergeVariable.PORTAGE_BUILDDIR)).exec();
		}

		// download file from http
		String uri = group.get().getValue();
		String destinationFile = getVariable(EmergeVariable.PORTAGE_BUILDDIR) + "/" + getVariable(EmergeVariable.P)
				+ "." + group.get().getUriExtention();

		String proxy = ServiceLocator.getInstance().getUserSettingReader().read().getProxy();
		BinUtils.wget(uri, destinationFile, proxy).exec();

	}

}
