import java.util.List;

import org.junit.Test;

import apt.api.AptGet;
import apt.entity.PackageName;
import apt.entity.ProgrammInfo;
import apt.exception.InternalException;
import apt.exception.UserException;

public class AptGetTest {
    
    private AptGet aptGet = new AptGet();
    
    @Test
    public void testSearch() throws InternalException, UserException {
	List<ProgrammInfo> infos = aptGet.find(new PackageName("app-arch", "p7zip" , ""));
	infos.forEach(e -> System.out.println(e));
    }

}
