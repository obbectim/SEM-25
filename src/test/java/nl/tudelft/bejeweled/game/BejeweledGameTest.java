package nl.tudelft.bejeweled.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javafx.application.Platform;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.CustomBoardLauncher;
import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;

/**
 * Created by Jeroen on 12-9-2015.
 * Test class for end to end testing the game.
 */
public class BejeweledGameTest extends ApplicationTest {

    private CustomBoardLauncher launcher = new CustomBoardLauncher();

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
     * Test of a user story where the user starts the game,
     * swaps 2 jewels and then ends the game.
     */
    @Test
    public void testMoveJewelNoWin() {
        BejeweledGame game = (BejeweledGame) launcher.getGame();
        assertFalse(game.isInProgress());

        clickOn("#buttonStart");

        // start and stop the game concurrently with JavaFX thread
        Platform.runLater(new Runnable() {
            public void run() {
                assertTrue(game.isInProgress());
            }
        });

        // click on 2 jewels that can be swapped
        clickOn(game.getBoard().getGrid()[0][0].getNode());
        clickOn(game.getBoard().getGrid()[0][1].getNode());

        // verify the score is still the same.
        Platform.runLater(new Runnable() {
            public void run() {
                assertSame(game.getScore(), 0);
            }
        });

        // stop the game
        clickOn("#buttonStop");
    }
}
