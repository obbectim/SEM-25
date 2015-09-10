package nl.tudelft.bejeweled.board;

/**
 * Created by Jeroen on 6-9-2015.
 * Interface for the boardobserver class
 */
public interface BoardObserver {

    /**
     * There are no more valid moves left on the board.
     */
    void boardOutOfMoves();

    /**
     * Callback from when there has been a valid move and
     * jewels have been removed from the board.
     */
    void boardJewelRemoved();
}
