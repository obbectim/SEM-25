package nl.tudelft.bejeweled.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import nl.tudelft.bejeweled.game.Game;


/**
 * Created by Jeroen on 3-9-2015.
 * Class that initialises the GUI for the Bejeweled game.
 */
public class BejeweledGui {

    private Pane boardPane;

    private Label scoreLabel;
    private Label levelLabel;

    /**
     * The constructor initialises the GUI.
     *
     * @param game  The current game
     * @param stage The primary stage
     */
    public BejeweledGui(Game game, Stage stage) {
        createStage(game, stage);
    }

    /**
     * Initialises the GUI using the fxml template file.
     *
     * @param game  The game object
     * @param stage The (root) stage to draw the GUI on
     */
    public void createStage(Game game, Stage stage) {
        try {
            // load fxml file from resource and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BejeweledGui.class.getResource("/bejeweled_gui.fxml"));
            AnchorPane page = loader.load();
            BejeweledGuiController bejeweledController = loader.getController();

            // init the scene and set dialog properties
            Scene scene = new Scene(page);
            stage.setScene(scene);
            Image icon = new Image(BejeweledGui.class.getResourceAsStream("/bejeweled_icon.png"));
            stage.getIcons().add(icon);
            stage.setTitle("Bejeweled");
            stage.setResizable(false);

            // draw board as a background on the boardPane
            boardPane = bejeweledController.getBoardPane();

            scoreLabel = bejeweledController.getScoreLabel();
            
            levelLabel = bejeweledController.getLevelLabel();

            // redirect button callbacks to game class
            bejeweledController.setGame(game);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
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