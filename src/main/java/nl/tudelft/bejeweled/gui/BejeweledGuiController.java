package nl.tudelft.bejeweled.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import nl.tudelft.bejeweled.game.Game;

import java.net.URL;
import java.util.ResourceBundle;

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
    private Pane boardPane;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert buttonStart != null;
        assert buttonStop != null;
        assert buttonExit != null;

        // set button event handles
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                game.start();
            }
        });

        buttonStop.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                game.stop();
            }
        });

        buttonExit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Exit");
            }
        });
    }

    /**
     * Setter method for game.
     * @param game The current game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Getter method for boardPane (the pane of the game)
     * @return The boardPane used by the board.
     */
    public Pane getBoardPane() {
        return boardPane;
    }
}
