package nl.tudelft.bejeweled.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.CustomBoardLauncher;
import nl.tudelft.bejeweled.Launcher;
import nl.tudelft.bejeweled.game.BejeweledGame;
import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Created by Jeroen on 12-9-2015.
 */
public class BejeweledGameTest extends ApplicationTest {

    private CustomBoardLauncher launcher = new CustomBoardLauncher();
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        launcher.launchGame(primaryStage);
    }

    /**
     * Quit the user interface when we're done.
     */
    @After
    public void tearDown() { }

    /**
     * First test.
     */
    @Test
    public void testStartGame() {
        BejeweledGame game = launcher.getGame();
        assertFalse(game.isInProgress());

        clickOn("#buttonStart");

        // start and stop the game concurrently with JavaFX thread
        Platform.runLater(new Runnable() {
            public void run() {
                assertTrue(game.isInProgress());
            }
        });
    }
}
