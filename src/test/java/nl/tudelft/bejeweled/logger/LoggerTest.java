package nl.tudelft.bejeweled.logger;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Class used for testing the Logger class.
 * @author janharms
 *
 */
public class LoggerTest {

	private final int logFileLines = 4;
	
	/**
	 * Test the info logging function.
	 * @throws IOException Exception when the file is not available
	 */
	@Test
	public void testInfoLogging() throws IOException {
		String message = "Hello World!";
		
		Logger.enable();
		// Create a logFile
		Logger.logInfo(message);
		Path logFilePath = Logger.getLogFilePath();
		Logger.close();

	    List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(1);
	    
		assertTrue(lines.size() > 0 && lines.size() < logFileLines);
		assertEquals("INFO", firstLine.substring(0, "INFO".length()));
		assertEquals(message, firstLine.substring(firstLine.length() - message.length()));
		
		Files.delete(logFilePath);
	}
	
	/**
	 * Test the error logging function.
	 * @throws IOException Exception when the file is not available
	 */
	@Test
	public void testErrorLogging() throws IOException {
		String message = "Hakuna Matata";
		
		Logger.enable();
		// Create a logFile
		Logger.logError(message);
		Path logFilePath = Logger.getLogFilePath();
		Logger.close();

	    List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(1);
	    
		assertTrue(lines.size() > 0 && lines.size() < logFileLines);
		assertEquals("ERROR", firstLine.substring(0, "ERROR".length()));
		assertEquals(message, firstLine.substring(firstLine.length() - message.length()));
		
		Files.delete(logFilePath);
	}
	
	/**
	 * Test the error warning function.
	 * @throws IOException Exception when the file is not available
	 */
	@Test
	public void testWarningLogging() throws IOException {
		String message = "Hello JavaFX";
		
		Logger.enable();
		Logger.logWarning(message);
		Path logFilePath = Logger.getLogFilePath();
		Logger.close();

	    List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(1);
	    
		assertTrue(lines.size() > 0 && lines.size() < logFileLines);
		assertEquals("WARNING", firstLine.substring(0, "WARNING".length()));
		assertEquals(message, firstLine.substring(firstLine.length() - message.length()));
		
		Files.delete(logFilePath);
	}
	
	
	/**
	 * Test enabling and disabling of logging.
	 * @throws IOException Exception when the file is not available
	 */
	@Test
	public void testEnableDisable() throws IOException {
		String message1 = "enabled logging";
		String message2 = "disabled logging";
		
		Logger.enable();
		Path logFilePath = Logger.getLogFilePath();
		Logger.disable();
		
		List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(0);
	    String secondLine = lines.get(1);
	    
		assertTrue(lines.size() > 0 && lines.size() < logFileLines);
		assertEquals(message1, firstLine.substring(firstLine.length() - message1.length()));
		assertEquals(message2, secondLine.substring(secondLine.length() - message2.length()));
		
		Files.delete(logFilePath);
	}
}
