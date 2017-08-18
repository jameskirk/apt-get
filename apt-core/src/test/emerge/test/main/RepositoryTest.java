package emerge.test.main;

import org.junit.Test;

import emerge.api.RepositoryService;
import emerge.api.impl.RepositoryServiceImpl;
import emerge.exception.GenericException;

public class RepositoryTest {
    
    RepositoryService service = new RepositoryServiceImpl();
    
    
    @Test
    public void test0() throws GenericException {
	service.firstInit();
    }
    
    @Test
    public void test1() throws GenericException {
	service.sync();
    }
    
//    @Test
//    public void test2() throws GenericException {
//	service.find("p7zip");
//	//service.find("p7zip-4.65");
//	service.find("app-arch/p7zip");
//	service.find("app-arch/p7zip-4.65");
//	
//	service.find("p7zip-4.6");
//	service.find("app-archa/p7zip");
//	service.find("app-archa/p7zip-4.65");
//	service.find("p7zip-test-9.20_middleABC");
//	
//	service.find("p7zip-test");
//	service.find("p7zip-test-9.20");
//	service.find("p7zip-test-9.20_middle");
//    }
//    
}
