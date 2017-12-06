package apt.console;

import emerge.api.PackageService;
import emerge.api.RepositoryService;
import emerge.api.impl.AptServiceFacade;
import emerge.exception.InternalException;
import emerge.exception.UserException;
import emerge.misc.NameDeterminer;

public class Main {

    AptServiceFacade facade;
    RepositoryService repositoryService;
    PackageService packageService;

    public void init() {
	facade = new AptServiceFacade();
	repositoryService = facade.getRepositoryService();
	packageService = facade.getPackageService();
    }

    public static void main(String args[]) {
	Main main = new Main();
	main.init();
	main.executeCommand(args);
    }

    public void executeCommand(String args[]) {
	try {
	    if (args.length == 0) {
		System.out.println("commands:\n sync \n init \n install p7zip \n info p7zip");
	    }
	    if (args.length == 1) {
		if ("sync".equals(args[0])) {
		    repositoryService.sync();
		} else if ("init".equals(args[0])) {
		    repositoryService.firstInit();
		} else {
		    System.out.println("unknown command");
		}
	    }

	    if (args.length >= 2) {
		if ("install".equals(args[0])) {
		    packageService.install(NameDeterminer.parseCategoryNameVersion(args[1]));
		} else if ("remove".equals(args[0])) {
		    packageService.remove(NameDeterminer.parseCategoryNameVersion(args[1]));
		} else if ("info".equals(args[0])) {
		    packageService.info(NameDeterminer.parseCategoryNameVersion(args[1]));
		} else if ("search".equals(args[0])) {
		    repositoryService.find(NameDeterminer.parseCategoryNameVersion(args[1]));
		} else {
		    System.out.println("unknown command");
		}
	    }
	} catch (InternalException e) {
	    System.out.println("error " + e);
	    e.printStackTrace();
	} catch (UserException e) {
	}
    }

}
