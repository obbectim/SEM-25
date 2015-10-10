package nl.tudelft.bejeweled;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.tudelft.bejeweled.board.BoardFactory;
import nl.tudelft.bejeweled.game.Game;
import nl.tudelft.bejeweled.game.GameFactory;
import nl.tudelft.bejeweled.gui.BejeweledGui;
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.SpriteStore;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
/**
 * Created by Jeroen on 1-9-2015.
 * Class that launches the game
 */
public class Launcher extends Application {

    /** The frames per second limit of the game. */
	private static final int FPS_LIMIT = 60;

    /** The Window Title. */
    private static final String WINDOW_TITLE = "Bejeweled";

    /** The SpriteStore. */
    private static final SpriteStore SPRITE_STORE = new SpriteStore();

    /**  The current game. */
    private Game game;

    /** The GUI for the Bejeweled game. */
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
        Group sceneNodes = new Group();
        game = makeGame(FPS_LIMIT, WINDOW_TITLE, SPRITE_STORE);
        game.setSceneNodes(sceneNodes);

        // initialise the gui and map start/stop buttons
        bejeweledGui = new BejeweledGui(game, theStage);

        // initialise the game
        game.initialise(bejeweledGui.getBoardPane(), bejeweledGui.getScoreLabel(),
        		bejeweledGui.getLevelLabel());
        if (saveGameExists()) {
            Optional<ButtonType> result = showYesNoDialog("Saved State Available",
                    "Would you like to resume the previous game?");
            if ((result.isPresent()) && (result.get() == ButtonType.YES)) {
                game.resume();
            }
            else {
                game.removeSaveGame();
            }
        }
        game.beginGameLoop();
        
        theStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            	game.save();
            	theStage.close();
            }
        });
    }

    /**
     * Makes a Game object.
     * @param framesPerSecond The refresh rate for the animations.
     * @param windowTitle The window Title.
     * @param spriteStore The sprite store.
     * @return An instantiated game object.
     */
    public Game makeGame(int framesPerSecond, String windowTitle, SpriteStore spriteStore) {
        GameFactory gf = new GameFactory(spriteStore);
        return gf.createBejeweledGame(framesPerSecond, windowTitle);
    }

    /**
     * Show Dialog with yes/no buttons.
     * @param title Title of the dialog.
     * @param text Context text of the dialog.
     * @return The button type clicked by the user.
     */
    public Optional<ButtonType> showYesNoDialog(String title, String text) {
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.getDialogPane().getButtonTypes().clear();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
        dialog.setTitle(title);
        dialog.setContentText(text);

        return dialog.showAndWait();
    }

    /**
     * Checks if save game is available.
     * @return True if and only if save game files are both present.
     */
    public boolean saveGameExists() {
        File saveFile = new File("save.mine");
        return saveFile.exists();
    }

    /**
     * Get a freshly created boardfactory.
     * @return A new board factory using the sprite store from
     */
    protected BoardFactory getBoardFactory() {
        return new BoardFactory(getSpriteStore());
    }

    /**
     * Get function for the game.
     * @return returns a handle to the game
     */
    public Game getGame() { 
    	return game; 
    }

    /**
     * Get function for the SpriteStore.
     * @return returns a handle to the SpriteStore
     */
    protected SpriteStore getSpriteStore() {
        return SPRITE_STORE;
    }
}
