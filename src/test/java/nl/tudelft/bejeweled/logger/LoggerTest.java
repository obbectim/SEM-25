package nl.tudelft.bejeweled.logger;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class LoggerTest {

	@Test
	public void testInfoLogging() throws IOException {
		Logger.enable();
		// Create a logFile
		Logger.log("INFO ", "Hello World!");
		Path logFilePath = Logger.getLogFilePath();
		Logger.close();

	    List<String> lines = Files.lines(logFilePath).collect(
	            Collectors.toList());
		
	    String firstLine = lines.get(0);
	    
		assertTrue(lines.size() > 0 && lines.size() < 3);
		assertEquals("INFO", firstLine.substring(0, 4));
		assertEquals("Hello World!", firstLine.substring(firstLine.length()-12, firstLine.length()));
		
		Files.delete(logFilePath);
	}

}
