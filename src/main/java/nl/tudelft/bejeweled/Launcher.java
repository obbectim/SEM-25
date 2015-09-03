package nl.tudelft.bejeweled;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.game.Game;
import nl.tudelft.bejeweled.gui.BejeweledGui;

/**
 * Created by Jeroen on 1-9-2015.
 * Class that launches the game
 */
public class Launcher extends Application {

    private Game game;
    private BejeweledGui bejeweledGui ;

    public static void main(String[] args) {
        Application.launch(Launcher.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage theStage) {
        new Launcher().launchGame(theStage);
    }

    public void launchGame(Stage theStage) {

        // make a game
        // game makes a board
        // game listens to keyboard input
        // board provides game loop and logic

        // build the GUI elements
        // start the GUI
        game = new Game();
        bejeweledGui = new BejeweledGui(game, theStage);


    }
}
