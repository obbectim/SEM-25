package nl.tudelft.bejeweled.game;

import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Created by Jeroen on 15-9-2015.
 * Abstract Factory pattern for creating different types of Games.
 */
public class GameFactory {

    private SpriteStore spriteStore;

    /**
     * Constructor for GameFactory.
     * @param spriteStore The spritestore for the game.
     */
    public GameFactory(SpriteStore spriteStore) {
        this.spriteStore = spriteStore;
    }

    /**
     * Create a BejeweledGame instance.
     * @param framesPerSecond Frames per second the game will be refreshed.
     * @param windowTitle The title of the window.
     * @return BejeweledGame object.
     */
    public Game createBejeweledGame(int framesPerSecond, String windowTitle) {
        return new BejeweledGame(framesPerSecond, windowTitle, spriteStore);
    }
    
    /**
     * Create a CustomBoardBejeweledGame instance with a custom board.
     * @param framesPerSecond Frames per second the game will be refreshed.
     * @param windowTitle The title of the window.
     * @param boardLocation The location of the boardfile to be used 
     * @return BejeweledGame object.
     */
    public Game createBejeweledGame(int framesPerSecond, String windowTitle, String boardLocation) {
        return new BejeweledGame(framesPerSecond, windowTitle, spriteStore, boardLocation);
    }
}
