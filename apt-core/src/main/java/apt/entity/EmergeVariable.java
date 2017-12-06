package apt.entity;

public enum EmergeVariable {
    
    P, // package name with version - p7zip-4.19
    
    PN, // package name - p7zip
    
    PV, // package version - 4.19
    
    CATEGORY, // category - app-arch
    
    SRC_URI, // src_uri
    
    PORTAGE_BUILDDIR, // build dir for package, /cygwin/c/emerge/package_repository/app-arch/p7zip
    
    WORKDIR, // build dir for package - ${PORTAGE_BUILDDIR}/work
    
    INSTALLDIR, // install dir for package - /cygwin/c/Program Files/p7zip
    
    USE,
    
    INSTALL_KEYWORD, // keyword for installation
    
    WINDOWS_PRODUCT_CODE,
    
    NAME_IN_REGISTRY;
    
    
    
    @Override
    public String toString() {
	return this.name();
    };

}
