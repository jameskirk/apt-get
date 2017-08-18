package emerge.test.misc;

import org.junit.Assert;
import org.junit.Test;

import emerge.system.BinUtils;

public class MiscTest {
    
    @Test
    public void test() {
	Assert.assertEquals(true, BinUtils.exists("/cygdrive/C/Temp").execSilent());
	Assert.assertNotEquals(false, BinUtils.exists("/cygdrive/C/Temp111").execSilent());
	
	Assert.assertEquals(true, BinUtils.exists("/cygdrive/C").execSilent() && BinUtils.exists("/cygdrive/C").execSilent());
	Assert.assertEquals(false, BinUtils.exists("/cygdrive/C").execSilent() && BinUtils.exists("/cygdrive/C/Temp2").execSilent());
	Assert.assertEquals(false, BinUtils.exists("/cygdrive/C/Temp2").execSilent() && BinUtils.exists("/cygdrive/C").execSilent());
	Assert.assertEquals(false, BinUtils.exists("/cygdrive/C/Temp2").execSilent() && BinUtils.exists("/cygdrive/C/Temp2").execSilent());

	Assert.assertEquals(true, BinUtils.exists("/cygdrive/C").execSilent() || BinUtils.exists("/cygdrive/C").execSilent());
	Assert.assertEquals(true, BinUtils.exists("/cygdrive/C").execSilent() || BinUtils.exists("/cygdrive/C/Temp2").execSilent());
	Assert.assertEquals(true, BinUtils.exists("/cygdrive/C/Temp2").execSilent() || BinUtils.exists("/cygdrive/C").execSilent());
	Assert.assertEquals(false, BinUtils.exists("/cygdrive/C/Temp2").execSilent() || BinUtils.exists("/cygdrive/C/Temp2").execSilent());
	
	
    }

}
