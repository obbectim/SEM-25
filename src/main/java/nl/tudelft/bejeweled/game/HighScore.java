package nl.tudelft.bejeweled.game;

import java.util.TreeMap;

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
public class HighScore {

	private TreeMap<Integer, String> highscores;
	private static final String highscoreFile = "highscores.xml";
	private boolean loaded = false;
	
	/**
	 * Constructor which creates a new empty TreeMap.
	 */
	public HighScore() {
		highscores = new TreeMap<Integer, String>();
	}
	
	/**
	 * Loads the highscore file and fills the TreeMap with the entries found.
	 */
	public void loadHighScores() {
		if (!loaded) {
			Logger.logInfo("Loading HighScores");
		}
	}
	
	private void saveHighScores() {
		if (!loaded) {
			return;
		}
		Logger.logInfo("Saving to file");
	}
	
	/**
	 * Adds a score to the list of highscores and saves to file.
	 * @param score Score to add to the highscore list
	 * @param name Name of the player that achieved the highscore
	 */
	public void addHighScore(int score, String name) {
		if (!loaded) {
			return;
		}
		Logger.logInfo("Adding score to the highscores");
	}
	
	/**
	 * Checks if the given score is as good or better than the top 5 scores.
	 * @param score Score to be checked for a highscore
	 * @return Returns the place of the score, returns 0 if the score is no high score
	 */
	public int isHighScore(int score) {
		if (!loaded) {
			return -1;
		}
		Logger.logInfo("Checking whether the score is a highscore");
		return 0;
	}
	
	/**
	 * Returns the current list of highscores.
	 * @return
	 */
	public TreeMap<Integer, String> getHighScores() {
		return highscores;
	}
}
