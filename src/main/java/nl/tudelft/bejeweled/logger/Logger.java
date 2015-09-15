package nl.tudelft.bejeweled.logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class to log all events throughout runtime of the game.
 * 
 * @author janharms
 *
 */
public final class Logger {
	
	private static final String INFO = "INFO ";
	private static final String ERROR = "ERROR ";
	private static final String WARNING = "WARNING ";
	
	private static BufferedWriter logFile;
	private static Format formatter;
	
	/**
	 * Creates a new file with a time stamp in it's name.
	 * 
	 * @throws IOException Throws an exception if the file cannot be opened for some reason
	 */
	private Logger() {
		
		formatter = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss");
		String fileName = "LogFile_" + formatter.format(Calendar.getInstance().getTime());
		Path filePath = Paths.get(fileName);
		logFile = null;
		
		try {
			logFile = Files.newBufferedWriter(filePath);
		} catch	(IOException ex) {
			System.err.println("IOException caught: " + ex.getMessage());
		}
		
	}
	
	/**
	 * Base function to create an entry in the log file.
	 * 
	 * @param type Type of the message
	 * @param message Message text
	 */
	public static void log(String type, String message) {
		
		try {
			logFile.write(type + formatter.format(Calendar.getInstance().getTime()) + message);
		} catch (IOException ex) {
			System.err.println("IOException caught: " + ex.getMessage());
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
	 * Closes the logFile at the end of the program.
	 */
	public static void close() {
		
		try {
			logFile.close();
		} catch (IOException ex) {
			System.err.println("IOException caught: " + ex.getMessage());
		}
		
	}
}
