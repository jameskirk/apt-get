package emerge.test.misc;

import org.junit.Assert;
import org.junit.Test;

import emerge.system.OsType;
import emerge.system.Path;

public class PathTest {
    
    @Test
    public void testAll() {
	String winP = "C:\\Program Files (x86)";
	String winUnixP = "/cygdrive/c/Program\\ Files\\ \\(x86\\)";
	Assert.assertEquals(winP, new Path(winP).getValue(OsType.WINDOWS));
	Assert.assertEquals(winUnixP, new Path(winP).getValue(OsType.UNIX));
	
	Assert.assertEquals(winP, new Path(winUnixP, OsType.UNIX).getValue(OsType.WINDOWS));
	Assert.assertEquals(winUnixP, new Path(winUnixP, OsType.UNIX).getValue(OsType.UNIX));
	
	winP = "C:\\cygwin64\\bin\\pr (amd64)";
	winUnixP = "/cygdrive/c/cygwin64/bin/pr\\ \\(amd64\\)";
	Assert.assertEquals(winP, new Path(winP).getValue(OsType.WINDOWS));
	Assert.assertEquals(winUnixP, new Path(winP).getValue(OsType.UNIX));
	
	Assert.assertEquals(winP, new Path(winUnixP, OsType.UNIX).getValue(OsType.WINDOWS));
	Assert.assertEquals(winUnixP, new Path(winUnixP, OsType.UNIX).getValue(OsType.UNIX));
	
    }

}
