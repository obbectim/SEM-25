package nl.tudelft.bejeweled;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.board.Board;
import nl.tudelft.bejeweled.board.BoardFactory;
import nl.tudelft.bejeweled.game.BejeweledGame;
import nl.tudelft.bejeweled.gui.BejeweledGui;
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Created by Jeroen on 1-9-2015.
 * Class that launches the game
 */
public class Launcher extends Application {

	private static final int FPS_LIMIT = 60;

    private SpriteStore spriteStore;

    private BoardFactory boardFactory;
	
    /**
     *  The current game.
     */
    private BejeweledGame game;

    /**
     * The GUI for the Bejeweled game.
     */
    private BejeweledGui bejeweledGui;

    /**
     * The main method which starts the launcher.
     * @param args Command line arguments which do not currently effect anything.
     */
    public static void main(String[] args) {
    	
    	// Check if logging should be enabled
    	if (args.length > 1) {
    		if (args[0].equals("-logging")) {
    			
    			if (args[1].equals("enabled") || args[1].equals("yes") || args[1].equals("1")
    					|| args[1].equals("true")) {
    				
    				Logger.enable();
    			}
    		}
    	}
    	
        Application.launch(Launcher.class, (java.lang.String[]) null);
        
        Logger.disable();
    }

    @Override
    public void start(Stage theStage) {
        new Launcher().launchGame(theStage);
    }

    /**
     * Loads the gui and launches the game.
     * @param theStage The primary stage to draw the GUI on
     */
    public void launchGame(Stage theStage) {
        spriteStore = new SpriteStore();
        game = new BejeweledGame(FPS_LIMIT, "Bejeweled", spriteStore);

        boardFactory = getBoardFactory();
        Group sceneNodes = new Group();
        Board board = makeBoard(sceneNodes);
        game.setSceneNodes(sceneNodes);

        // initialise the gui and map start/stop buttons
        bejeweledGui = new BejeweledGui(game, theStage);

        // initialise the game
        game.initialise(board, bejeweledGui.getBoardPane(), bejeweledGui.getScoreLabel());

        // begin game loop
        game.beginGameLoop();
    }

    /**
     * Creates the board from a set of sceneNodes.
     * @param sceneNodes Groupd of javafx sceneNodes
     * @return returns a board with the given nodes
     */
    public Board makeBoard(Group sceneNodes) {
        return boardFactory.generateBoard(sceneNodes);
    }

    /**
     * @return A new board factory using the sprite store from
     */
    protected BoardFactory getBoardFactory() {
        return new BoardFactory(getSpriteStore());
    }

    /**
     * Get function for the game.
     * @return returns a handle to the game
     */
    public BejeweledGame getGame() { 
    	return game; 
    }

    /**
     * Get function for the SpriteStore.
     * @return returns a handle to the SpriteStore
     */
    protected SpriteStore getSpriteStore() {
        return spriteStore;
    }
}
