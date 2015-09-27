package nl.tudelft.bejeweled.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nl.tudelft.bejeweled.board.Board;
import nl.tudelft.bejeweled.sprite.Sprite;
import nl.tudelft.bejeweled.sprite.SpriteStore;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by Jeroen on 3-9-2015.
 * Class to handle game input and events
 */
public abstract class Game implements Serializable {

    /** All nodes to be displayed in the game window. */
    private Group sceneNodes;

    /** The game loop using JavaFX's <code>Timeline</code> API.*/
    private static Timeline gameLoop;

    /** Number of frames per second. */
    private final int framesPerSecond;

    /**
     * Title in the application window.
     */
    private final String windowTitle;

    /**
     * Keeps track  of the highest scores ever achieved.
     */
    private HighScore highScore;

    /**
     * The sprite manager.
     */
    private final SpriteStore spriteStore = new SpriteStore();

    /**
     * Constructor that is called by the derived class. This will
     * set the frames per second, title, and setup the game loop.
     * @param framesPerSecond - The number of frames per second the game will attempt to render.
     * @param windowTitle - The title displayed in the window.
     */
    public Game(int framesPerSecond, final String windowTitle) {
        this.framesPerSecond = framesPerSecond;
        this.windowTitle = windowTitle;

        // create and set timeline for the game loop
        buildAndSetGameLoop();
    }

    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop() {
        final Duration oneFrameAmt = Duration.seconds(1.0 / getFramesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(javafx.event.ActionEvent event) {

                    	//update board
                    	updateLogic();
                    	
                        // update actors
                        updateSprites();

                        // removed dead things
                        cleanupSprites();

                    }
                }); // oneFrame

        // sets the game world's game loop (Timeline)
        final Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.getKeyFrames().add(oneFrame);
        gameLoop.play();

        setGameLoop(gameLoop);
    }

    /**
     * Initialise the game world by update the JavaFX Stage.
     * @param board The board of the game.
     * @param gamePane The primary scene.
     * @param scoreLabel The label for the score.
     */
    public abstract void initialise(Board board, Pane gamePane, Label scoreLabel);

    /**Kicks off (plays) the Timeline objects containing one key frame
     * that simply runs indefinitely with each frame invoking a method
     * to update sprite objects, check for collisions, and cleanup sprite
     * objects.
     *
     */
    public void beginGameLoop() {
        getGameLoop().play();
    }
    
    /**
     * Updates the board.
     *
     */
    protected  void updateLogic() {
    	
    }
    

    /**
     * Updates each game sprite in the game world. This method will
     * loop through each sprite and passing it to the handleUpdate()
     * method. The derived class should override handleUpdate() method.
     *
     */
    protected void updateSprites() {
        for (Iterator<Sprite> i =  spriteStore.getAllSprites().iterator(); i.hasNext();) {
            handleUpdate(i.next());
        }
    }

    /** Updates the sprite object's information to position on the game surface.
     * @param sprite - The sprite to update.
     */
    protected void handleUpdate(Sprite sprite) {
        sprite.update();
    }

    /**
     * Sprites to be cleaned up.
     */
    protected void cleanupSprites() {
        spriteStore.cleanupSprites();
    }

    /**
     * Returns the frames per second.
     * @return int The frames per second.
     */
    protected int getFramesPerSecond() {
        return framesPerSecond;
    }

    /**
     * The game loop (Timeline) which is used to update, check collisions, and
     * cleanup sprite objects at every interval (fps).
     * @return Timeline An animation running indefinitely representing the game
     * loop.
     */
    protected static Timeline getGameLoop() {
        return gameLoop;
    }

    /**
     * The sets the current game loop for this game world.
     * @param gameLoop Timeline object of an animation running indefinitely
     * representing the game loop.
     */
    protected static void setGameLoop(Timeline gameLoop) {
        Game.gameLoop = gameLoop;
    }
    
    /**
     * The map of names and highscores.
     * @return a TreeMap<Integer, String> of the scores and corresponding player names
     */
    public TreeMap<Integer, String> getHighScores() {
    	return highScore.getHighScores();
    }

    /**
     * Returns the sprite manager containing the sprite objects to
     * manipulate in the game.
     * @return SpriteManager The sprite manager.
     */
    public SpriteStore getSpriteStore() {
        return spriteStore;
    }

    /**
     * All JavaFX nodes which are rendered onto the game surface(Scene) is
     * a JavaFX Group object.
     * @return Group The root containing many child nodes to be displayed into
     * the Scene area.
     */
    public Group getSceneNodes() {
        return sceneNodes;
    }

    /**
     * Sets the JavaFX Group that will hold all JavaFX nodes which are rendered
     * onto the game surface(Scene) is a JavaFX Group object.
     * @param sceneNodes The root container having many children nodes
     * to be displayed into the Scene area.
     */
    public void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

    /**
     *  Starts the game.
     */
    public abstract void start();

    /**
     *  Stops the game.
     */
    public abstract void stop();

    /** 
     * Shows a hint to a possible move.
     */
	public abstract void showHint();
    
    /**
     * Saves the current game.
     */
    public abstract void save();
    
    /**
     * Resume the previously saved game.
     */
    public abstract void resume();

    /**
     * Removes the save game if it exists.
     */
    public abstract void removeSaveGame();

    /**
     * Getter method for highScore.
     * @return The highscore object.
     */
    public HighScore getHighScore() {
        return highScore;
    }

    /**
     * Setter method for highScore.
     * @param highScore The Highscore object.
     */
    public void setHighScore(HighScore highScore) {
        this.highScore = highScore;
    }
}
