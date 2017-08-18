//package emerge.test.repo;
//
//
//import java.util.Arrays;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import emerge.entity.Keyword;
//import emerge.entity.PackageId;
//import emerge.exception.InternalException;
//import emerge.repo.EbuildLocalRepositoryReader;
//import emerge.repo.InstalledRepositoryReader;
//import emerge.repo.entity.InstalledPackageEntry;
//
//public class EbuildRepositoryTest {
//    
//    @Test
//    public void testEbuildRepo() throws InternalException {
//	EbuildLocalRepositoryReader reader = new EbuildLocalRepositoryReader();
//	reader.cache();
//	
//	System.out.println("\n FROM CACHE :\n" + reader.readAll());
//	Assert.assertEquals(3,  reader.readByCriteria(new PackageId("", "p7zip", "")).size());
//	Assert.assertEquals(1,  reader.readByCriteria(new PackageId("", "p7zip", "9.20")).size());
//	Assert.assertEquals(3,  reader.readByCriteria(new PackageId("app-arch", "p7zip", "")).size());
//	Assert.assertEquals(1,  reader.readByCriteria(new PackageId("app-arch", "p7zip", "9.20")).size());
//	Assert.assertEquals(0,  reader.readByCriteria(new PackageId("app-arch1", "p7zip", "")).size());
//	Assert.assertEquals(0,  reader.readByCriteria(new PackageId("app-arch", "p7zip", "9.223")).size());
//    }
//    
//    @Test
//    public void testInstallRepo() throws InternalException {
//	InstalledRepositoryReader reader = new InstalledRepositoryReader();
//	reader.cache();
//	
//	System.out.println("\n FROM CACHE :\n" + reader.readAll());
//	
//	Assert.assertEquals(3,  reader.readAll().size());
//	reader.remove(new PackageId("app-arch", "p7zip", "4.65"));
//	
//	Assert.assertEquals(2,  reader.readAll().size());
//	
//	InstalledPackageEntry entry = new InstalledPackageEntry();
//	entry.setPackageId(new PackageId("app-arch", "p7zip", "4.65"));
//	entry.setUse(Arrays.asList(new Keyword("test")));
//	reader.saveOrUpdate(entry);
//	
//	Assert.assertEquals(3,  reader.readAll().size());
//	Assert.assertEquals("test",  reader.readByCriteria(
//		new PackageId("app-arch", "p7zip", "4.65")).get(0).getUse().get(0).getValue());
//	
//	
//    }
//
//}
