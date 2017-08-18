package emerge.test.misc;

import org.junit.Assert;
import org.junit.Test;

import emerge.conf.Configuration;
import emerge.exception.GenericException;
import emerge.system.BinUtils;
import emerge.system.ProcessExecutor;

public class ProcessExecutorTest {
    
    @Test
    public void execUnixCmd() throws GenericException {
	int status = ProcessExecutor.executeUnixCommand(BinUtils.ls);
	Assert.assertEquals(0, status);
	
	status = ProcessExecutor.executeUnixCommand(BinUtils.ls);
	Assert.assertNotEquals(0, status);
	
	status = ProcessExecutor.executeUnixCommand(BinUtils.ls + " -l   -a  ");
	Assert.assertEquals(0, status);
	
	status = ProcessExecutor.executeUnixCommand("[ -d " 
		+ Configuration.ebuildRepositoryDir + " ]");
	Assert.assertEquals(0, status);
	
	
	
    }

}
