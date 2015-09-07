package nl.tudelft.bejeweled.gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import nl.tudelft.bejeweled.board.Board;
import nl.tudelft.bejeweled.game.Game;
import nl.tudelft.bejeweled.sprite.Jewel;

import java.util.Random;


/**
 * Created by Jeroen on 3-9-2015.
 * Class that initialises the GUI for the Bejeweled game.
 */
public class BejeweledGui {

    private Pane boardPane;

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
            stage.getIcons().add(new Image(BejeweledGui.class.getResourceAsStream("/bejeweled_icon.png")));
            stage.setTitle("Bejeweled");
            stage.setResizable(false);

            // draw board as a background on the boardPane
            boardPane = bejeweledController.getBoardPane();

            // redirect button callbacks to game class
            bejeweledController.setGame(game);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Pane getBoardPane() {
        return boardPane;
    }
}