import java.util.List;

import org.junit.Test;

import apt.api.AptGet;
import apt.api.PackageInfo;
import apt.entity.PackageName;
import apt.exception.InternalException;
import apt.exception.UserException;

public class AptGetTest {
    
    private AptGet aptGet = new AptGet();
    
    @Test
    public void testSearch() throws InternalException, UserException {
	List<PackageInfo> infos = aptGet.find(new PackageName("app-arch", "p7zip" , ""));
	infos.forEach(e -> System.out.println(e));
    }

}
