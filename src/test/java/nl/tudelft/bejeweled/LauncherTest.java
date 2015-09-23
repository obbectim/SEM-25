package nl.tudelft.bejeweled;

import static org.junit.Assert.assertFalse;

import javafx.application.Platform;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.game.BejeweledGame;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Created by Jeroen on 9-9-2015.
 * Test c;ass for Launcher.
 */
public class LauncherTest extends ApplicationTest {

    private Launcher launcher = new Launcher();

    @Override
    public void start(Stage primaryStage) throws Exception {
        launcher.launchGame(primaryStage);
    }

    /**
     * Quit the user interface when we're done.
     */
    @After
    public void tearDown() {
        closeCurrentWindow();
    }

    /**
     * First test.
     */
    @Test
    @Ignore
    public void testStartGame() {
        BejeweledGame game = (BejeweledGame)launcher.getGame();
        assertFalse(game.isInProgress());

        clickOn("#buttonExit");
    }
}
