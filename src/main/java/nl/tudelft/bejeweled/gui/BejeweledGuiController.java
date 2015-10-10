package nl.tudelft.bejeweled.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.game.Game;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Created by Jeroen on 3-9-2015.
 * The JavaFX Controller class that accompanies bejeweeld_gui.fxml
 */
public class BejeweledGuiController implements Initializable {

    // The launched game
    private Game game;

    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonStop;
    @FXML
    private Button buttonExit;
    @FXML
    private Button buttonHint;
    @FXML
    private Pane boardPane;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private ListView<String> highscoreList;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert buttonStart != null;
        initializeStartButton();
        assert buttonStop != null;
        initializeStopButton();
        assert buttonExit != null;
        initializeExitButton();    
        assert buttonHint != null;
        initializeHintButton();
        assert highscoreList != null;
    }
    
    /**
     * Populates the highscore list with highscores.
     */
    private void initializeListView() {
    	TreeMap<Integer, String> highscores = game.getHighScores();
    	List<String> entries = new ArrayList<String>();
    	
    	int counter = highscores.size();
    	for (Map.Entry<Integer, String> entry : highscores.entrySet()) {
    		entries.add(0, counter + ". " + entry.getValue() + ": " + entry.getKey());
    		counter--;
    	}
    	
    	highscoreList.getItems().setAll(entries);
	}

	/**
     * Sets start button to start the game.
     * */
    public void initializeStartButton() {
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                game.start();
            }
        });
    }
    
    /**
     * Sets stop button to stop the game.
     * */
    public void initializeStopButton() {
        buttonStop.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                game.stop();
                initializeListView();
            }
        });
    }
    
    /**
     * Sets exit button to exit the game.
     * */
    public void initializeExitButton() {
    	 buttonExit.setOnAction(new EventHandler<ActionEvent>() {

             @Override
             public void handle(ActionEvent event) {

                 // get a handle to the stage
                 Stage stage = (Stage) buttonExit.getScene().getWindow();
                 game.save();
                 stage.close();
             }
         });
    }
    
    /**
     * Set hint button to create a hint to help the player.
     */
    public void initializeHintButton() {
    	buttonHint.setOnAction(new EventHandler<ActionEvent>() {
    		
    		@Override
    		public void handle(ActionEvent event) {
    			game.showHint();
    		}
    	});
    }
    
    
    /**
     * Setter method for game.
     * @param game The current game
     */
    public void setGame(Game game) {
        this.game = game;
        initializeListView();
    }

    /**
     * Getter method for boardPane (the pane of the game).
     * @return The boardPane used by the board.
     */
    public Pane getBoardPane() {
        return boardPane;
    }

    /**
     * Getter method for the score label in the dialog.
     * @return The Label used to display the score.
     */
    public Label getScoreLabel() {
        return scoreLabel;
    }
    
    /**
     * Getter method for the level label in the dialog.
     * @return The Label used to display the level.
     */
    public Label getLevelLabel() {
    	return levelLabel;
    }
}
