package emerge.test.misc;

import org.junit.Assert;
import org.junit.Test;

import emerge.entity.PackageName;
import emerge.misc.NameDeterminer;

public class NameDeterminerTest {
    
    private NameDeterminer determiner = new NameDeterminer();
    
    private final String packageId = "app-arch/7-zip-1.2.53";
    
    @Test
    public void test1() {
	Assert.assertEquals("app-arch", determiner.getCategory(packageId));
	Assert.assertEquals("app-arch", determiner.getCategory("/" + packageId));
    }
    
    @Test
    public void test2() {
	Assert.assertEquals("7-zip", determiner.getNameWithoutVersion(packageId));
	Assert.assertEquals("7-zip-install", determiner.getNameWithoutVersion("app-arch/7-zip-install-1.2.53"));
    }
    
    @Test
    public void test3() {
	Assert.assertEquals("1.2.53", determiner.getVersion(packageId));
	Assert.assertEquals("1.2.53", determiner.getVersion("app-arch/7-zip-install-1.2.53"));
    }
    
    @Test
    public void test4() {
	Assert.assertEquals(1, determiner.compareVersions("2", "1"));
	Assert.assertEquals(1, determiner.compareVersions("2.1", "2.0"));
	Assert.assertEquals(1, determiner.compareVersions("2.10", "2.2"));
	Assert.assertEquals(1, determiner.compareVersions("2.1.315", "2.1.314"));
	Assert.assertEquals(0, determiner.compareVersions("2", "2"));
	Assert.assertEquals(0, determiner.compareVersions("2.23", "2.23"));
	Assert.assertEquals(0, determiner.compareVersions("2.23.134", "2.23.134"));
	Assert.assertEquals(-1, determiner.compareVersions("1", "2"));
	Assert.assertEquals(-1, determiner.compareVersions("2.0", "2.1"));
	Assert.assertEquals(-1, determiner.compareVersions("2.2", "2.10"));
	Assert.assertEquals(-1, determiner.compareVersions("2.1.314", "2.1.315"));
    }
    
    @Test
    public void test5() {
	Assert.assertEquals(new PackageName("app-arch", "7-zip" , "1.2.53"), NameDeterminer.parseCategoryNameVersion(packageId));
	Assert.assertEquals(new PackageName("app-arch", "7-zip" , "1.2.53"), NameDeterminer.parseCategoryNameVersion("/" + packageId));
	
	Assert.assertEquals(new PackageName("", "7-zip" , ""), NameDeterminer.parseCategoryNameVersion("7-zip"));
	Assert.assertEquals(new PackageName("", "7-zip" , "7.1"), NameDeterminer.parseCategoryNameVersion("7-zip-7.1"));
	Assert.assertEquals(new PackageName("", "7-zip-7.1vs-ds" , ""), NameDeterminer.parseCategoryNameVersion("7-zip-7.1vs-ds"));
	Assert.assertEquals(new PackageName("", "7-zip-7.1vs-ds" , "2.3.13"), NameDeterminer.parseCategoryNameVersion("7-zip-7.1vs-ds-2.3.13"));
	
	Assert.assertEquals(new PackageName("app-arch", "7-zip-7.1vs-ds" , "2.3.13"), NameDeterminer.parseCategoryNameVersion("app-arch/7-zip-7.1vs-ds-2.3.13"));
	
	Assert.assertEquals(null, NameDeterminer.parseCategoryNameVersion(""));
	Assert.assertEquals(null, NameDeterminer.parseCategoryNameVersion("/"));
	Assert.assertEquals(null, NameDeterminer.parseCategoryNameVersion("//")); 
	Assert.assertEquals(null, NameDeterminer.parseCategoryNameVersion("/s/s/d"));
	Assert.assertEquals(null, NameDeterminer.parseCategoryNameVersion("/app//7z"));
	
    }

}
