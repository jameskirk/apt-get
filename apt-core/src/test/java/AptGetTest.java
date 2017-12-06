import java.util.List;

import org.junit.Test;

import emerge.api.AptGet;
import emerge.api.PackageInfo;
import emerge.entity.PackageName;
import emerge.exception.InternalException;
import emerge.exception.UserException;

public class AptGetTest {
    
    private AptGet aptGet = new AptGet();
    
    @Test
    public void testSearch() throws InternalException, UserException {
	List<PackageInfo> infos = aptGet.find(new PackageName("app-arch", "p7zip" , ""));
	infos.forEach(e -> System.out.println(e));
    }

}
