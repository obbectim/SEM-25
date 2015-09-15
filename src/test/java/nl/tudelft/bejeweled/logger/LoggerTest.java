package nl.tudelft.bejeweled.logger;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class LoggerTest {

	@Test
	public void testLogging() throws IOException {
		// Create a logFile
		Logger.log("INFO ", "Hello World!");
		Path logFilePath = Logger.getLogFilePath();
		Logger.close();
		
		BufferedReader reader = null;
		
		reader = Files.newBufferedReader(logFilePath);
		String line = reader.readLine();
		
		assertTrue(line.length() > 0);
		assertEquals("INFO", line.substring(0, 4));
		assertEquals("Hello World!", line.substring(line.length()-11, line.length()));
	}

}
