package nl.tudelft.bejeweled;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.game.BejeweledGame;
import nl.tudelft.bejeweled.game.Game;
import nl.tudelft.bejeweled.gui.BejeweledGui;

/**
 * Created by Jeroen on 1-9-2015.
 * Class that launches the game
 */
public class Launcher extends Application {

    /**
     *  The current game
     */
    private Game game;

    /**
     * The GUI for the Bejeweled game
     */
    private BejeweledGui bejeweledGui;

    public static void main(String[] args) {
        Application.launch(Launcher.class, (java.lang.String[]) null);
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
        game = new BejeweledGame(60, "Bejeweled");

        // initialise the gui and map start/stop buttons
        bejeweledGui = new BejeweledGui(game, theStage);

        // initialise the game
        game.initialise(bejeweledGui.getBoardPane(), bejeweledGui.getScoreLabel());

        // begin game loop
        game.beginGameLoop();
    }
}
