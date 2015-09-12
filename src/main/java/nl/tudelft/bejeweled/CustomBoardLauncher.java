package nl.tudelft.bejeweled;

import javafx.scene.Group;
import nl.tudelft.bejeweled.board.Board;

/**
 * Created by Jeroen on 12-9-2015.
 * Extension of the launcher class to load custom boards from text.
 * Primarily used for testing purposes.
 */
public class CustomBoardLauncher extends Launcher {

    private String boardLocation = "/board.txt";

    /**
     * Function to set the location of a custom board.
     * @param newLocation the location of the board template file.
     */
    public void setLocation(String newLocation) {
        boardLocation = newLocation;
    }

    /**
     * Gets the location of the map file for the level.
     * @return The map location.
     */
    public String getLocation() {
        return boardLocation;
    }

    @Override
    public Board makeBoard(Group sceneNodes) {
        return getBoardFactory().fromTextGenerateBoard(boardLocation, sceneNodes);
    }
}
