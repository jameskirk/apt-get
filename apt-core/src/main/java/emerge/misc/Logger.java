package emerge.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import emerge.conf.Configuration;

public class Logger {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Emerge");

    private static final Level defaultLevel = Configuration.loggerLevel;
    
    public static OutputStream loggerStream;
    
    public static final boolean ANSI_COLORS_DISABLED = true;
    
    public static final String ANSI_RESET = ANSI_COLORS_DISABLED ? "" : "\u001B[0m";
    public static final String ANSI_BLACK = ANSI_COLORS_DISABLED ? "" : "\u001B[30m";
    public static final String ANSI_RED = ANSI_COLORS_DISABLED ? "" : "\u001B[31m";
    public static final String ANSI_GREEN = ANSI_COLORS_DISABLED ? "" : "\u001B[32m";
    public static final String ANSI_YELLOW = ANSI_COLORS_DISABLED ? "" : "\u001B[33m";
    public static final String ANSI_BLUE = ANSI_COLORS_DISABLED ? "" : "\u001B[34m";
    public static final String ANSI_PURPLE = ANSI_COLORS_DISABLED ? "" : "\u001B[35m";
    public static final String ANSI_CYAN = ANSI_COLORS_DISABLED ? "" : "\u001B[36m";
    public static final String ANSI_WHITE = ANSI_COLORS_DISABLED ? "" : "\u001B[37m";

    static {
	try {
	    loggerStream = System.out;//new FileOutputStream(new File(Configuration.emergeLogPath));
	} catch (Throwable e) {
	    e.printStackTrace();
	}
	logger.setLevel(defaultLevel);
	logger.setUseParentHandlers(false);
	MyConsoleHandler handler = null;
	try {
	    handler = new MyConsoleHandler();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	handler.setLevel(defaultLevel);
	logger.addHandler(handler);
    }
    
    public static void user(String message) {
	System.out.println(message);
    }

    public static void error(String message) {
	logger.log(Level.SEVERE, message);
    }

    public static void info(String message) {
	logger.log(Level.INFO, message);
    }

    public static void debug(String message) {
	logger.log(Level.FINE, message);
    }

    private static class MyFormatter extends Formatter {
	@Override
	public String format(LogRecord record) {
	    return record.getMessage() + "\n";
	}
    }

    private static class MyConsoleHandler extends StreamHandler {
	
	private final StreamHandler stderrHandler;

	public MyConsoleHandler() throws FileNotFoundException {
	    super(loggerStream, new MyFormatter());
	    stderrHandler = new StreamHandler(loggerStream,
		    new MyFormatter());
	}

	@Override
	public void publish(LogRecord record) {
	    if (record.getLevel().intValue() <= Level.INFO.intValue()) {
		super.publish(record);
		super.flush();
	    } else {
		stderrHandler.publish(record);
		stderrHandler.flush();
	    }
	}
    }

}
