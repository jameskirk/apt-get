package apt.console;

import apt.api.AptGet;
import apt.exception.InternalException;
import apt.exception.UserException;
import apt.misc.NameDeterminer;

public class AptGetConsole {

    private AptGet aptGet = new AptGet();

    public static void main(String args[]) {
	AptGetConsole main = new AptGetConsole();
	main.executeCommand(args);
    }

    public void executeCommand(String args[]) {
	try {
	    if (args.length == 0) {
		System.out.println("commands:\n sync \n init \n install p7zip \n info p7zip");
	    }
	    if (args.length == 1) {
		if ("sync".equals(args[0])) {
		    aptGet.sync();
		} else if ("init".equals(args[0])) {
		    aptGet.firstInit();
		} else {
		    System.out.println("unknown command");
		}
	    }

	    if (args.length >= 2) {
		if ("install".equals(args[0])) {
		    aptGet.install(NameDeterminer.parseCategoryNameVersion(args[1]));
		} else if ("remove".equals(args[0])) {
		    aptGet.remove(NameDeterminer.parseCategoryNameVersion(args[1]));
		} else if ("info".equals(args[0])) {
		    aptGet.info(NameDeterminer.parseCategoryNameVersion(args[1]));
		} else if ("search".equals(args[0])) {
		    aptGet.find(NameDeterminer.parseCategoryNameVersion(args[1]));
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
