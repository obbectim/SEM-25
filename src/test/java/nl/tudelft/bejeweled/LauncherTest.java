package nl.tudelft.bejeweled;

import static org.junit.Assert.assertFalse;

import javafx.stage.Stage;
import nl.tudelft.bejeweled.game.BejeweledGame;
import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;

/**
 * Created by Jeroen on 9-9-2015.
 * Test c;ass for Launcher.
 */
public class LauncherTest extends ApplicationTest {

    private Launcher launcher = new Launcher();

    @Override
    public void start(Stage primaryStage) throws Exception {
        File boardFile = new File("board.mine");
        File scoreFile = new File("score.mine");
        if (boardFile.exists() || scoreFile.exists()) {
            boardFile.delete();
            scoreFile.delete();
        }
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
    public void testStartGame() {
        BejeweledGame game = (BejeweledGame) launcher.getGame();
        assertFalse(game.isInProgress());

        clickOn("#buttonExit");
    }
}
