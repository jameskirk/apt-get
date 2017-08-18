//package emerge.test.main;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import emerge.api.PackageService;
//import emerge.api.impl.PackageServiceImpl;
//import emerge.api.impl.RepositoryServiceImpl;
//import emerge.conf.Configuration;
//import emerge.exception.ExecutionException;
//import emerge.exception.GenericException;
//
//public class InstallTest {
//
//    PackageService packageService = new PackageServiceImpl();
//    String eBuild = Configuration.testEbuildId;
//    
//    @Test
//    public void testIntall() throws GenericException {
//	System.out.println("\n  ******\n");
//	// force remove
////	try {
////	    packageService.remove(eBuild);
////	} catch (ExecutionException e) {
////	    System.out.println("removed");
////	}
//	packageService.install("p7zip");
//    }
//
//    //@Test
//    public void testIntallTwoTimes() throws GenericException {
//	System.out.println("\n  ******\n");
//	// force remove
//	try {
//	    packageService.remove(eBuild);
//	} catch (ExecutionException e) {
//	}
//	
//	boolean exception = false;
//	packageService.install(eBuild);
//	try {
//
//	    packageService.install(eBuild);
//	} catch (ExecutionException e) {
//	    exception = true;
//	}
//	Assert.assertEquals(true, exception);
//    }
//    
//    //@Test
//    public void testInfo() throws GenericException {
//	System.out.println("\n  ******\n");
//	packageService.info(eBuild);
//    }
//    
//    @Before
//    public void before() {
//	((PackageServiceImpl)packageService).setRepositoryService(new RepositoryServiceImpl());
//    }
//
//    
//
//
//
//}
