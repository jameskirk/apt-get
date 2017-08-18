package emerge.test.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import emerge.conf.Configuration;
import emerge.conf.UserSettingsReader;
import emerge.conf.UserSettings;
import emerge.core.InstallHandler;
import emerge.entity.Keyword;
import emerge.exception.GenericException;
import emerge.misc.Logger;
import emerge.misc.NameDeterminer;

public class UseCheckTest {
    
    InstallHandler install;
    
    static int testNumber;

    // *** Keywords ***

    @Test
    public void checkKeywordsFlags1() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86"), Collections.emptyList()), Configuration.testEbuildIdExperimental + "_x86");
	install.checkFlags();
    }

    @Test(expected = GenericException.class)
    public void checkKeywordsFlags2() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86"), Collections.emptyList()), Configuration.testEbuildIdExperimental + "_mx86");
	install.checkFlags();
    }

    @Test
    public void checkKeywordsFlags3() throws Exception {
	initMocks(getUserSettings(Arrays.asList("~x86"), Collections.emptyList()), Configuration.testEbuildIdExperimental + "_mx86");
	install.checkFlags();
    }

    @Test
    public void checkKeywordsFlags4() throws Exception {
	initMocks(getUserSettings(Arrays.asList("~x86"), Collections.emptyList()), Configuration.testEbuildIdExperimental + "_x86");
	install.checkFlags();
    }

    @Test(expected = GenericException.class)
    public void checkKeywordsFlags5() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86", "amd64"), Collections.emptyList()), Configuration.testEbuildIdExperimental + "_mx86mamd64");
	install.checkFlags();
    }

    @Test
    public void checkKeywordsFlags6() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86", "~amd64"), Collections.emptyList()), Configuration.testEbuildIdExperimental + "_x86mamd64");
	install.checkFlags();
    }
    
    @Test
    public void checkKeywordsFlags7() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86", "amd64"), Collections.emptyList()), Configuration.testEbuildIdExperimental + "_x86amd64");
	install.checkFlags();
    }

    // *** USE flags

    @Test
    public void checkUseFlags1() throws Exception {
	System.out.println("USE tests:");
	initMocks(getUserSettings(Arrays.asList("x86"), Arrays.asList("installer")), Configuration.testEbuildIdExperimental + "_x86");
	install.checkFlags();
    }

    @Test
    public void checkUseFlags2() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86"), Arrays.asList("-installer")), Configuration.testEbuildIdExperimental + "_x86");
	install.checkFlags();
    }

    @Test(expected = GenericException.class)
    public void checkUseFlags3() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86"), Arrays.asList("installer")), Configuration.testEbuildIdExperimental + "_minstaller");
	install.checkFlags();
    }

    @Test
    public void checkUseFlags4() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86"), Arrays.asList("-installer")), Configuration.testEbuildIdExperimental + "_minstaller");
	install.checkFlags();
    }

    @Test
    public void checkUseFlags5() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86"), Arrays.asList("installer")), Configuration.testEbuildIdExperimental + "_pinstaller");
	install.checkFlags();
    }

    @Test(expected = GenericException.class)
    public void checkUseFlags6() throws Exception {
	initMocks(getUserSettings(Arrays.asList("x86"), Arrays.asList("-installer")), Configuration.testEbuildIdExperimental + "_pinstaller");
	install.checkFlags();
    }
    
    @Before
    public void before() {
	testNumber++;
	Logger.info("\nTest " + testNumber);
    }
    
    public UserSettings getUserSettings(List<String> acceptKeywords, List<String> use) {
	UserSettings settings = new UserSettings();
	settings.setAcceptLicense(Arrays.asList("*"));
	List<Keyword> parsedKeywords = new ArrayList<Keyword>();
	acceptKeywords.stream().forEach(x -> parsedKeywords.add(new Keyword(x)));
	settings.setAcceptKeywords(parsedKeywords);

	List<Keyword> parsedUse = new ArrayList<Keyword>();
	use.stream().forEach(x -> parsedUse.add(new Keyword(x)));
	settings.setUse(parsedUse);
	return settings;
    }

    private void initMocks(UserSettings userSettings, String packageId) throws Exception {
	install = Mockito.spy(new InstallHandler());

	Class<?> tClass = InstallHandler.class;
	Field f = tClass.getDeclaredField("packageId");
	f.setAccessible(true);
	f.set(install, NameDeterminer.parseCategoryNameVersion(packageId));

	UserSettingsReader readerMock = Mockito.mock(UserSettingsReader.class);

	Mockito.doReturn(userSettings).when(readerMock).read();
	Mockito.doReturn(readerMock).when(install).getUserSettingReader();
    }

}
