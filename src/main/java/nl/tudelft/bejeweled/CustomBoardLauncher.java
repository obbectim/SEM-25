package nl.tudelft.bejeweled;

import nl.tudelft.bejeweled.game.Game;
import nl.tudelft.bejeweled.game.GameFactory;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Created by Jeroen on 12-9-2015.
 * Extension of the launcher class to load custom boards from text.
 * Primarily used for testing purposes.
 */
public class CustomBoardLauncher extends Launcher {

    /**
     * The location of the board.
     */
    private String boardLocation = "/board.txt";

    /**
     * Function to set the location of a custom board.
     * @param newLocation the location of the board template file.
     */
    public void setLocation(String newLocation) {
        boardLocation = newLocation;
    }

    /**
     * Gets the location of the board file for the board.
     * @return The board location.
     */
    public String getLocation() {
        return boardLocation;
    }

    /**
     * Creates the board the game wil use from file.
     * @param sceneNodes The group containing the jewel nodes.
     * @return The board.
     */
    
   @Override
    /**
     * Makes a Game object.
     * @param framesPerSecond The refresh rate for the animations.
     * @param windowTitle The window Title.
     * @param spriteStore The sprite store.
     * @return An instantiated game object.
     */
    public Game makeGame(int framesPerSecond, String windowTitle, SpriteStore spriteStore) {
        GameFactory gf = new GameFactory(spriteStore);
        return gf.createBejeweledGame(framesPerSecond, windowTitle, boardLocation);
    }
}
