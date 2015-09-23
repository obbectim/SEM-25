package nl.tudelft.bejeweled.game;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing the HighScore class.
 * @author jan
 *
 */
public class HighScoreTest {

	private HighScore highScore;
	private final int currentScore = 1000;
	private final int newScore = 1001;
	private Path highScores = Paths.get("highscores.xml");
	private Path testHighScores = Paths.get("test_highscores.xml");
	private Path backupHighScores = Paths.get("highscores.xml.backup");
	
	/**
	 * Initializes the highScore object.
	 * @throws JAXBException If something unexpected happens during creation of HighScore class
	 * @throws IOException If moving the files fails
	 */
	@Before
	public void init() throws JAXBException, IOException {
		highScore = new HighScore();
		
		// backup current highscores.xml
		if (Files.exists(highScores)) {
			Files.move(highScores, backupHighScores, StandardCopyOption.REPLACE_EXISTING);
		}
		// copy testHighscores.xml to highscores.xml
		Files.copy(testHighScores, highScores, StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * Move back the files.
	 * @throws IOException
	 */
	@After
	public void close() throws IOException {
		// backup current highscores.xml
		if (Files.exists(backupHighScores)) {
			Files.move(backupHighScores, highScores, StandardCopyOption.REPLACE_EXISTING);
		}
		else {
			Files.delete(highScores);
		}
		
	}
	
	/**
	 * Tests loading of the HighScore xml file.
	 * @throws JAXBException If something unexpected happens during loading of the highscore file
	 */
	@Test
	public void testLoadEmptyHighScore() throws JAXBException {
		highScore.loadHighScores();
		
		assertTrue(highScore.isLoaded());
		assertTrue(highScore.getHighScores().containsValue("Jan"));
		assertTrue(highScore.getHighScores().containsKey(currentScore));
	}
	
	/**
	 * Tests adding of new HighScores.
	 * @throws JAXBException If anything unexpected happens during unmarshalling in addHighScore
	 * @throws IOException If anything unexpected happens during copying the test_highscore file
	 */
	@Test
	public void testAddHighScore() throws JAXBException, IOException {
		highScore.loadHighScores();
		
		// add a new highScore
		highScore.addHighScore(newScore, "Jan");
		
		File highScoreFile = new File("highscores.xml");
		assertTrue(highScoreFile.exists());
		assertTrue(highScoreFile.length() > 0);
		
		HighScore newHighScore = new HighScore();
		newHighScore.loadHighScores();
		assertTrue(newHighScore.getHighScores().containsValue("Jan"));
		assertTrue(newHighScore.getHighScores().containsKey(newScore));
		assertTrue(newHighScore.getHighScores().size() <= 5);
		
		// copy testHighscores.xml to highscores.xml
		Files.copy(testHighScores, highScores, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Tests the check for having a high score.
	 * @throws JAXBException If anything unexpected happens during loadHighScore
	 */
	@Test
	public void testIsHighScore() throws JAXBException {
		highScore.loadHighScores();
		final int lowScore = 999;
		
		assertEquals(highScore.isHighScore(newScore), 5);
		assertEquals(highScore.isHighScore(currentScore), 5);
		assertEquals(highScore.isHighScore(lowScore), 0);
	}
}
