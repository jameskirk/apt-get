package apt.entity;

public enum AptGetKeyword {
	
	ACCEPT_PLATFORMS, USE, //make.conf
    
    DESCRIPTION, HOMEPAGE, SRC_URI, LICENSE, PLATFORMS, IUSE, DEFAULT_PATH, REGISTRY_NAME, 
    INSTALLER_ENABLE_MANY_PLATFORMS, INSTALLER_ENABLE_MANY_VERSIONS, // ebuild + installedEbuild
    
    INSTALL_USE, INSTALL_DIR, INSTALL_REGISTRY_PRODUCT_CODE, // installedEbuild
    
    x86, amd64, // platforms
	
    // variables
	P, // package name with version - p7zip-4.19
	PN, // package name - p7zip
	PV, // package version - 4.19
	CATEGORY, // category - app-arch
    PORTAGE_BUILDDIR, // build dir for package, /cygwin/c/emerge/package_repository/app-arch/p7zip
    WORKDIR; // build dir for package - ${PORTAGE_BUILDDIR}/work

}
