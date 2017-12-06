package emerge.ebuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import emerge.conf.Configuration;
import emerge.entity.Keyword;
import emerge.entity.KeywordGroup;
import emerge.entity.PackageName;
import emerge.exception.InternalException;
import emerge.misc.FileHelper;

public class EbuildReader {

	public EbuildReader() {
	}

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
					fileAsString = FileHelper.readFile(localRepository + FileHelper.getEbuildPathFirst(ebuildId) + "/common.ebuild");
				}

				ebuildFile.setDescription(readVariable(fileAsString, EbuildKeyword.DESCRIPTION.name())
						.replace(EbuildKeyword.DESCRIPTION.name() + "=\"", "").replace("\"", ""));
				ebuildFile.setHomepage(readVariable(fileAsString, EbuildKeyword.HOMEPAGE.name())
						.replace(EbuildKeyword.HOMEPAGE.name() + "=\"", "").replace("\"", ""));
				ebuildFile.setLicense(readVariable(fileAsString, EbuildKeyword.LICENSE.name())
						.replace(EbuildKeyword.LICENSE.name() + "=\"", "").replace("\"", ""));

				if (readVariable(fileAsString, EbuildKeyword.KEYWORDS.name()) != "") {
				String keywordsValue = readVariable(fileAsString, EbuildKeyword.KEYWORDS.name())
						.replace(EbuildKeyword.KEYWORDS.name() + "=\"", "").replace("\"", "");
				List<Keyword> processedKeywords = new ArrayList<Keyword>();
				Arrays.asList(keywordsValue.split("( )+")).stream().forEach(x -> processedKeywords.add(new Keyword(x)));
				ebuildFile.setKeywords(processedKeywords);
				}

				if (readVariable(fileAsString, EbuildKeyword.IUSE.name()) != "") {
				String iuseValue = readVariable(fileAsString, EbuildKeyword.IUSE.name())
						.replace(EbuildKeyword.IUSE.name() + "=\"", "").replace("\"", "");
				List<Keyword> processedIuse = new ArrayList<Keyword>();
				Arrays.asList(iuseValue.split("( )+")).stream().forEach(x -> processedIuse.add(new Keyword(x)));
				ebuildFile.setIuse(processedIuse);
				}
				// TODO: REQUIRED_USE

				String srcUri = readVariable(fileAsString, EbuildKeyword.SRC_URI.name())
						.replace(EbuildKeyword.SRC_URI.name() + "=\"", "").replace("\"", "");
				srcUri = srcUri.replaceAll("^\\s+|\\s+$", "");
				if (!srcUri.contains(" ")) {
					// link
					ebuildFile.getSrcUriList().add(new KeywordGroup(Collections.emptyList(), srcUri));
				} else {
					// x86 : link
					// amd64 : link2
					for (String srcUriItem : readSrcUri(srcUri)) {
						String useString = srcUriItem.split("\\?")[0].replace("\t", "");
						List<String> useList = Arrays.asList(useString.split("( )+"));
						String uri = srcUriItem.substring(srcUriItem.split("\\?")[0].length() + 1).replaceAll("\\s+", "");

						ebuildFile.getSrcUriList().add(new KeywordGroup(useList, uri));
					}
				}
				
				
				String defaultUrl = readVariable(fileAsString, EbuildKeyword.DEFAULT_PATH.name())
					.replace(EbuildKeyword.DEFAULT_PATH.name() + "=\"", "").replace("\"", "");
				defaultUrl = defaultUrl.replaceAll("^\\s+|\\s+$", "");
        			if (!defaultUrl.contains("?")) {
        				// link
        				ebuildFile.getDefaultPath().add(new KeywordGroup(Collections.emptyList(), defaultUrl));
        			} else {
        				// x86 : link
        				// amd64 : link2
        				for (String srcUriItem : readSrcUri(defaultUrl)) {
        					String useString = srcUriItem.split("\\?")[0].replace("\t", "");
        					List<String> useList = Arrays.asList(useString.split("( )+"));
        					String uri = srcUriItem.substring(srcUriItem.split("\\?")[0].length() + 1).replaceAll("\\s+", "");
        
        					ebuildFile.getDefaultPath().add(new KeywordGroup(useList, uri));
        				}
        			}

				// optional
				String registryName = readVariable(fileAsString, EbuildKeyword.NAME_IN_REGISTRY.name());
				if (registryName != null) {
					ebuildFile.setRegistryName(
							registryName.replace(EbuildKeyword.NAME_IN_REGISTRY.name() + "=\"", "").replace("\"", ""));
				}
				return ebuildFile;
	}

	private String readVariable(String fileAsString, String variable) {
		Pattern p = Pattern.compile("((" + variable + "){1})((=\"){1})(.|\\s)+?([\"]{1})");
		return FileHelper.findByPattern(fileAsString, p) == null ? "" : FileHelper.findByPattern(fileAsString, p) ;
	}

	private List<String> readSrcUri(String srcUriValue) {
		Pattern p = Pattern.compile("((.)+)(\\s+)(\\?{1})(\\s+)(\\S+)");
		return FileHelper.findAllByPattern(srcUriValue, p) == null ? new ArrayList<>() : FileHelper.findAllByPattern(srcUriValue, p);
	}

}
