package nl.tudelft.bejeweled.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import nl.tudelft.bejeweled.logger.Logger;

/**
 * Class to handle the HighScore system. 
 * 
 * This class will keep track of a list of the 5 highest scores achieved. It will connect
 * the scores to names given for each score. It will save the scores in a xml file. The checking
 * and adding of the scores will be handled by the Game class that holds a HighScore object.
 * 
 * @author jan
 *
 */
@XmlRootElement
public class HighScore {

	@XmlElement
	private TreeMap<Integer, String> highscores;
	private static final String highscoreFile = "highscores.xml";
	private boolean loaded = false;
	private final JAXBContext jc;
	
	/**
	 * Returns the state of the HighScore handler.
	 * @return returns true if the HighScore is loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Constructor which creates a new empty TreeMap.
	 * @throws JAXBException If anything unexpected happens during JAXBContext creation
	 */
	public HighScore() throws JAXBException {
		highscores = new TreeMap<Integer, String>();
		jc = JAXBContext.newInstance(HighScore.class);
	}
	
	/**
	 * Loads the highscore file and fills the TreeMap with the entries found.
	 * @throws JAXBException If something unexpected happens during unmarshalling of the file
	 */
	public void loadHighScores() throws JAXBException {
		if (!loaded) {
			Logger.logInfo("Loading HighScores");
		}
		
		Unmarshaller um = jc.createUnmarshaller();
		try {
			HighScore hs = (HighScore)um.unmarshal(new FileReader(highscoreFile));
			highscores = hs.getHighScores();
		}
		catch (FileNotFoundException ex) {
			System.err.println("FileNotFoundException: " + ex.getMessage());
		}
		
		loaded = true;
	}
	
	/**
	 * Saves the highScores in the specified xml file.
	 * @throws JAXBException Throws exception if marshalling fails
	 */
	private void saveHighScores() throws JAXBException {
		if (!loaded) {
			return;
		}
		
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(this, new File(highscoreFile));
		
		Logger.logInfo("Saving to file");
	}
	
	/**
	 * Adds a score to the list of highscores and saves to file.
	 * @param score Score to add to the highscore list
	 * @param name Name of the player that achieved the highscore
	 * @throws JAXBException Throws exception if saveHighScores fails
	 */
	public void addHighScore(int score, String name) throws JAXBException {
		if (!loaded) {
			return;
		}
		
		highscores.put(score, name);
		
		saveHighScores();
		Logger.logInfo("Adding score to the highscores");
	}
	
	/**
	 * Checks if the given score is as good or better than the top 5 scores.
	 * @param score Score to be checked for a highscore
	 * @param name Name of the player that achieved the high score
	 * @return Returns the place of the score, returns 0 if the score is no high score
	 */
	public int isHighScore(int score) {
		if (!loaded) {
			return -1;
		}
		Logger.logInfo("Checking whether the score is a highscore");
		
		if (score < highscores.firstKey()) {
			return 0;
		}
		
		int place = 6;
		for(Map.Entry<Integer,String> entry : highscores.entrySet()) {
			Integer key = entry.getKey();
			String value = entry.getValue();
			if (score < key) {
				break;
			}
			place--;
		}
		
		return place;
	}
	
	/**
	 * Returns the current list of highscores.
	 * @return
	 */
	public TreeMap<Integer, String> getHighScores() {
		return highscores;
	}
}
