package nl.tudelft.bejeweled.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import nl.tudelft.bejeweled.game.Game;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jeroen on 3-9-2015.
 */
public class BejeweledGuiController implements Initializable {

    private Game game;

    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonStop;
    @FXML
    private Button buttonExit;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert buttonStart != null;
        assert buttonStop != null;
        assert buttonExit != null;

        // initialize your logic here: all @FXML variables will have been injected
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

    public void setGame(Game game) {
        this.game = game;
    }
}
