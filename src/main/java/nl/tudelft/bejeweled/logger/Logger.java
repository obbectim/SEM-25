package nl.tudelft.bejeweled.logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class to log all events throughout runtime of the game. This logger can be enabled
 * by appending a "-logging enabled" in the commandline.  
 * 
 * @author janharms
 *
 */
public final class Logger {
	
	private static final String INFO = "INFO ";
	private static final String ERROR = "ERROR ";
	private static final String WARNING = "WARNING ";
	
	private static PrintWriter logFile;
	private static Format formatter;
	private static String filePath;
	private static boolean enabled;
	
	/**
	 * Empty private constructor.
	 */
	private Logger() { };
	
	/**
	 * Creates a new file with a time stamp in it's name.
	 */
	public static void enable() {
		
		if (enabled) {
			return;
		}
		
		enabled = true;
		
		formatter = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss.SSS");
		filePath = "log/LogFile_" + formatter.format(Calendar.getInstance().getTime()) + ".txt";
		File file = new File(filePath);
		
		try {
			if (!file.exists()) {
				if (!file.createNewFile()) {
					System.err.println("Could not create file");
					return;
				}
			}
			logFile = new PrintWriter(file, "UTF-8");
			logInfo("enabled logging");
		} catch	(IOException ex) {
			System.err.println("IOException caught: " + ex.getMessage());
		}
		
	}
	
	/**
	 * Disables logging of the game.
	 */
	public static void disable() {
		
		if (enabled) {
			logInfo("disabled logging");
			logFile.close();
			enabled = false;
		}
		
	}
	
	/**
	 * Base function to create an entry in the log file.
	 * 
	 * @param type Type of the message
	 * @param message Message text
	 */
	public static void log(String type, String message) {
		
		if (enabled) {
			logFile.println(type + formatter.format(Calendar.getInstance().getTime()) 
			+ ": " + message);
		}
		
	}
	
	/**
	 * Creates an INFO entry in the logFile.
	 * 
	 * @param message Message to save in the log
	 */
	public static void logInfo(String message) {
		log(INFO, message);
	}
	
	/**
	 * Creates an ERROR entry in the logFile.
	 * 
	 * @param message Message text
	 */
	public static void logError(String message) {
		log(ERROR, message);
	}
	
	/**
	 * Creates a WARNING entry in the logFile.
	 * 
	 * @param message Message to save in the log
	 */
	public static void logWarning(String message) {
		log(WARNING, message);
	}
	
	/**
	 * Get the path to the opened logFile.
	 * 
	 * @return Path to the current log file
	 */
	public static Path getLogFilePath() {
		return Paths.get(filePath);
	}
}
