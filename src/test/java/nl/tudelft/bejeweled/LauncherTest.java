package nl.tudelft.bejeweled;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javafx.application.Platform;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.game.BejeweledGame;
import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Created by Jeroen on 9-9-2015.
 * Test c;ass for Launcher.
 */
public class LauncherTest extends ApplicationTest {

    private Launcher launcher = new Launcher();

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
    public void testStartGame()
    {
        BejeweledGame game = launcher.getGame();
        assertFalse(game.isInProgress());

        // start and stop the game concurrently with JavaFX thread
        Platform.runLater(new Runnable() {
            public void run() {
                game.start();
                assertTrue(game.isInProgress());

                game.stop();
                assertFalse(game.isInProgress());
            }
        });
    }
}
