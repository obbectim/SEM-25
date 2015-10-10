package nl.tudelft.bejeweled.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javafx.stage.Stage;
import nl.tudelft.bejeweled.CustomBoardLauncher;
import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import java.io.File;

/**
 * Created by Amrit on 24-9-2015.
 * Test class for save resume testing.
 */
public class SaveResumeTest extends ApplicationTest {
    
    private CustomBoardLauncher launcher = new CustomBoardLauncher();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        launcher.launchGame(primaryStage);
    }
    
    /**
     * Quit the user interface when we're done.
     */
    @After
    public void tearDown() {
      //  closeCurrentWindow();
    }
    
    /**
     * Test of a user story where the user starts the game,
     * exit and game is saved.
     */
    @Test
    public void testStartExit() {
        File saveFile = new File("save.mine");
        if (saveFile.exists()) {
        	saveFile.delete();
        }
        BejeweledGame game = (BejeweledGame) launcher.getGame();
        assertFalse(game.isInProgress());
        
        clickOn("#buttonStart");
        clickOn("#buttonExit");
        
        assertTrue(saveFile.exists());
    }
}