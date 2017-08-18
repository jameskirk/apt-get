package emerge.test.misc;


import org.junit.Test;

import emerge.conf.Configuration;
import emerge.conf.UserSettings;
import emerge.conf.UserSettingsReader;
import emerge.ebuild.EbuildReader;
import emerge.exception.GenericException;
import emerge.exception.InternalException;
import emerge.misc.NameDeterminer;

public class EbuildReaderTest {
    
    @Test
    public void testUserSettings() throws InternalException {
	UserSettingsReader reader = new UserSettingsReader();
	UserSettings settings = reader.read();
	System.out.println(settings);
    }
    
    @Test
    public void testSimple() throws GenericException {
	EbuildReader reader = new EbuildReader();
	System.out.println(reader.read(NameDeterminer.parseCategoryNameVersion(Configuration.testEbuildId)));
    }
    
}
