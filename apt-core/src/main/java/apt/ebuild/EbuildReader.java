package apt.ebuild;

import java.util.regex.Pattern;

import apt.conf.Configuration;
import apt.entity.AptGetKeyword;
import apt.entity.Keyword;
import apt.entity.KeywordExpression;
import apt.entity.PackageName;
import apt.exception.InternalException;
import apt.misc.FileHelper;

public class EbuildReader {

	/**
	 * ebuildId can be /app-arch/p7zip-2.5.0-r2.ebuild
	 * 
	 * @param ebuildId
	 */
	public EbuildFile readConcreteEbuild(PackageName ebuildId) throws InternalException {
		return readAny(ebuildId, true);
	}

	public EbuildFile readCommonEbuild(PackageName ebuildId) throws InternalException {
		return readAny(ebuildId, false);
	}

	private EbuildFile readAny(PackageName ebuildId, boolean isEbuild) throws InternalException {
		// read path to local ebuild repository from conf
		String localRepository = Configuration.ebuildRepositoryDir;

		EbuildFile ebuildFile = new EbuildFile();
		ebuildFile.setPackageId(ebuildId);
		String fileAsString;
		if (isEbuild) {
			fileAsString = FileHelper.readFile(localRepository + FileHelper.getEbuildPath(ebuildId) + ".ebuild");
		} else {
			fileAsString = FileHelper
					.readFile(localRepository + FileHelper.getEbuildPathFirst(ebuildId) + "/common.ebuild");
		}
		
		ebuildFile.setDescription(readStringVariable(fileAsString, AptGetKeyword.DESCRIPTION.toString()));
		ebuildFile.setHomepage(readStringVariable(fileAsString, AptGetKeyword.HOMEPAGE.toString()));
		ebuildFile.setLicense(readStringVariable(fileAsString, AptGetKeyword.LICENSE.toString()));
		ebuildFile.setRegistryName(readStringVariable(fileAsString, AptGetKeyword.REGISTRY_NAME.toString()));
		
		ebuildFile.setSrcUri(readKeywordExpressionVariable(fileAsString, AptGetKeyword.SRC_URI.toString()));
		ebuildFile.setPlatforms(readKeywordExpressionVariable(fileAsString, AptGetKeyword.PLATFORMS.toString()));
		ebuildFile.setIuse(readKeywordExpressionVariable(fileAsString, AptGetKeyword.IUSE.toString()));

		return ebuildFile;
	}

	private String readStringVariable(String fileAsString, String variable) {
		Pattern p = Pattern.compile("((" + variable + "){1})((=\"){1})(.|\\s)+?([\"]{1})");
		String nameAndValue = FileHelper.findByPattern(fileAsString, p) == null ? "" : FileHelper.findByPattern(fileAsString, p);
		return nameAndValue.replace(AptGetKeyword.HOMEPAGE.name() + "=\"", "").replace("\"", "");
	}
	
	private KeywordExpression readKeywordExpressionVariable(String fileAsString, String variable) {
		KeywordExpression retVal = new KeywordExpression();
		String value = readStringVariable(fileAsString, variable);
		if (value.contains("?")) {
			for (String line: value.split("\n")) {
				String ifCondition = line.split("\\?")[0].replace("\t", "").trim();
				String ifBodyCondition = line.split("\\?")[1].replace("\t", "").trim();
				String[] keywordsS = ifCondition.split(" ");
				for (String s: keywordsS) {
					retVal.getKeywordsIfCondition().add(new Keyword(s));
				}
				String[] keywordsS2 = ifBodyCondition.split(" ");
				retVal.getKeywordsIfBody().add(new KeywordExpression());
				for (String s: keywordsS2) {
					retVal.getKeywordsIfBody().get(0).getKeywords().add(new Keyword(s));
				}
			}
		} else {
			String[] keywordsS = value.split(" ");
			for (String s: keywordsS) {
				retVal.getKeywords().add(new Keyword(s));
			}
		}
		return retVal;
	}

}
