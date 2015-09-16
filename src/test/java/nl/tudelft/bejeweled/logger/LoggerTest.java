package nl.tudelft.bejeweled.logger;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class LoggerTest {

	/**
	 * Test the info logging function.
	 * @throws IOException Exception when the file is not available
	 */
	@Test
	public void testInfoLogging() throws IOException {
		Logger.enable();
		// Create a logFile
		Logger.logInfo("Hello World!");
		Path logFilePath = Logger.getLogFilePath();
		Logger.close();

	    List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(1);
	    
		assertTrue(lines.size() > 0 && lines.size() < 4);
		assertEquals("INFO", firstLine.substring(0, 4));
		assertEquals("Hello World!", firstLine.substring(firstLine.length()-12, firstLine.length()));
		
		Files.delete(logFilePath);
	}
	
	/**
	 * Test the error logging function.
	 * @throws IOException Exception when the file is not available
	 */
	@Test
	public void testErrorLogging() throws IOException {
		Logger.enable();
		// Create a logFile
		Logger.logError("Hakuna Matata");
		Path logFilePath = Logger.getLogFilePath();
		Logger.close();

	    List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(1);
	    
		assertTrue(lines.size() > 0 && lines.size() < 4);
		assertEquals("ERROR", firstLine.substring(0, 5));
		assertEquals("Hakuna Matata", firstLine.substring(firstLine.length()-13, firstLine.length()));
		
		Files.delete(logFilePath);
	}
	
	/**
	 * Test the error warning function.
	 * @throws IOException Exception when the file is not available
	 */
	@Test
	public void testWarningLogging() throws IOException {
		Logger.enable();
		Logger.logWarning("Hello JavaFX");
		Path logFilePath = Logger.getLogFilePath();
		Logger.close();

	    List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(1);
	    
		assertTrue(lines.size() > 0 && lines.size() < 4);
		assertEquals("WARNING", firstLine.substring(0, 7));
		assertEquals("Hello JavaFX", firstLine.substring(firstLine.length()-12, firstLine.length()));
		
		Files.delete(logFilePath);
	}
	
	
	/**
	 * Test enabling and disabling of logging.
	 */
	@Test
	public void testEnableDisable() throws IOException {
		Logger.enable();
		Path logFilePath = Logger.getLogFilePath();
		Logger.disable();
		
		List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(0);
	    String secondLine = lines.get(1);
	    
		assertTrue(lines.size() > 0 && lines.size() < 4);
		assertEquals("enabled logging", firstLine.substring(firstLine.length()-15, firstLine.length()));
		assertEquals("disabled logging", secondLine.substring(secondLine.length()-16, secondLine.length()));
		
		Files.delete(logFilePath);
	}
}
