package nl.tudelft.bejeweled.game;

import java.util.Optional;

import javax.xml.bind.JAXBException;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.SpriteStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.File;


/**
 * Created by Jeroen on 6-9-2015.
 * Bejeweled Game class.
 */
public class BejeweledGame extends Game implements Serializable, SessionObserver {
	public static final int GRID_WIDTH = 8;
    public static final int GRID_HEIGHT = 8;
    public static final int SPRITE_WIDTH = 64;
    public static final int SPRITE_HEIGHT = 64;

    private static final String SAVE_FILE = "save.mine";

    private Session session;

	private SpriteStore spriteStore;

    private Pane gamePane;

    private Label scoreLabel;
	private Label levelLabel;


    /**
     * The constructor for the bejeweled game.
     * @param framesPerSecond - The number of frames per second the game will attempt to render.
     * @param windowTitle - The title displayed in the window.
     * @param spriteStore - The spriteStore.
    */
    public BejeweledGame(int framesPerSecond, String windowTitle, SpriteStore spriteStore) {
        super(framesPerSecond, windowTitle);
        this.spriteStore = spriteStore;
        try {
        	setHighScore(new HighScore());
        
        	getHighScore().loadHighScores();
        }
        catch (JAXBException ex) {
        	ex.printStackTrace();
        	Logger.logError("HighScore system encountered an error");
        }
    }

    /**
     *  Starts the game.
     */
    @Override
    public void start() {
    	Logger.logInfo("Game started");
    	//Clean up existing sprites
    	gamePane.getChildren().remove(sceneNodes);
        spriteStore.removeAllSprites();    
        
        sceneNodes = new Group();
        session = new Session(spriteStore, sceneNodes);
        session.addObserver(this);
        updateLevel();
        updateScore();
        
        // Create the group that holds all the jewel nodes and create a game scene
        gamePane.getChildren().add(new Scene(getSceneNodes(),
                gamePane.getWidth(),
                gamePane.getHeight()).getRoot());

    
        removeSaveGame();
    }

    /**
     *  Stops the game.
     */
    @Override
    public void stop() {
        if (session != null) {
        	session.lockBoard();

        Logger.logInfo("Final score: " + session.getScore());
        int place = getHighScore().isHighScore(session.getScore());   
        if (place >  0) {
        	Optional<String> result = showTextInputDialog("Enter your name",
                    "Congratulations, you achieved a highscore." + " Please enter your name:");
			result.ifPresent(name -> {
				try {
                    getHighScore().addHighScore(session.getScore(), name);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
        }
        Logger.logInfo("Game stopped");
        removeSaveGame();
        }
    }


    /**
     * Initialise the game world by update the JavaFX Stage.
     * @param gamePane The primary scene.
     */
    @Override
    public void initialise(Pane gamePane, Label scoreLabel, Label levelLabel) {
        this.gamePane = gamePane;
        this.scoreLabel = scoreLabel;
        this.levelLabel = levelLabel;
    
        // draw and stretch the board background
        gamePane.setStyle("-fx-background-image: url('/board.png'); -fx-background-size: cover;");

        // set callback for clicking on the board
        gamePane.addEventFilter(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        if (session != null && event.getTarget() instanceof Pane) {
                        	session.getBoard().boardClicked(true);
                        }
                    }
                }
        );
    }
    
    /**
     * Updates all game logic, At this point this consists of updating 
     * the board that is loaded for the game if the game is in progress.
     */
    protected void updateLogic() {
    	if (session != null) {
        	session.update();
    	}
    }
    /*
            	Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                
            }
        });
        ft.play();
    */
        
    @Override
    public void showHint() {
    	if (session.getBoard() != null) {
    		session.getBoard().showHint();
    	}
    }
    
    
    @SuppressWarnings("restriction")
	@Override
    public void save() { 
    	if (session != null) {
    		if (session.getBoard().isLocked()) {
    			return;
    		}
	    	session.lockBoard();
	        try {
	        	//Before writing, convert the board to a serializable state
	        	session.getBoard().setState(session.getBoard().convertGrid());
	        	
	            OutputStream file = new FileOutputStream(SAVE_FILE);
	            OutputStream buffer = new BufferedOutputStream(file);
	            ObjectOutput output = new ObjectOutputStream(buffer);
	            output.writeObject(session);
	            output.flush();
	            output.close();
	       
	        } catch (IOException e) {
	            e.printStackTrace();
	        }      
	    //    gamePane.getChildren().remove(getSceneNodes());
	     //   spriteStore.removeAllSprites();
	        Logger.logInfo("Game saved");
    	}
    }
    
    @Override
    public void resume() {
    	 File saveFile = new File(SAVE_FILE);
         if (!saveFile.exists()) {
             return;
         }
    	//Clean up existing sprites
    	gamePane.getChildren().remove(getSceneNodes());
        spriteStore.removeAllSprites();
      
    	session = readSessionFromFile(SAVE_FILE);
        session.addObserver(this);
       //Restore the grid from its serialized form
       session.getBoard().makeGrid(getSceneNodes()); 
       session.unlockBoard();
       session.setSceneNodes(getSceneNodes());
       updateLevel();
       updateScore();

       saveFile.delete(); 
       Logger.logInfo("Game resumed");       
       scoreLabel.setText(Integer.toString(session.getScore()));
       gamePane.getChildren().add(new Scene(getSceneNodes(), gamePane.getWidth(), 
                                           gamePane.getHeight()).getRoot());
    }

    /**
     * Read a session from a file. The file must be a session saved in a compatible serialized form.
     * @param fileName the file to read from.
     * @return the saved session.
     */
    public Session readSessionFromFile(String fileName) {
    	Session session = null;
        InputStream file;       
       try {
           file = new FileInputStream(fileName);
           InputStream buffer = new BufferedInputStream(file);
           ObjectInput input = new ObjectInputStream(buffer);
           session = (Session) input.readObject();
           input.close();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }
       return session;
    }
    
    /**
     * Shows a text input dialog.
     * @param title Title of the dialog.
     * @param text Context text of the dialog.
     * @return The input by the user.
     */
    private Optional<String> showTextInputDialog(String title, String text) {
        Dialog<String> dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(text);

        return dialog.showAndWait();
    }

    /**
     * Removes the save game if it exists.
     */
    @Override
    public void removeSaveGame() {
        File saveFile = new File(SAVE_FILE);
        if (saveFile.exists() && saveFile.delete()) {
            Logger.logInfo("Save files deleted.");
        }
    }

    
    /**
     * Returns the current session.
     * @return the current session.
     */
    public Session getSession() {
		return session;
	}

    /**
     * Set the current session.
     * @param session the session to be set.
     */
	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public void gameOver() {
		stop();
	}

	@Override
	public void updateScore() {
        scoreLabel.setText(Integer.toString(session.getScore()));		
	}
	
	@Override
	public void updateLevel() {
        levelLabel.setText(Integer.toString(session.getLevel()));		
	}

	/**
	 * Returns whether the game is currently in progress.
	 * @return whether the game is currently in progress.
	 */
	public boolean isInProgress() {
		return (session != null);
	}
}
