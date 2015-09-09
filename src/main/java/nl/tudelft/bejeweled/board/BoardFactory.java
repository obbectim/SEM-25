package nl.tudelft.bejeweled.board;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import nl.tudelft.bejeweled.sprite.Jewel;
import nl.tudelft.bejeweled.sprite.SpriteStore;

import java.util.Random;

/**
 * Created by Jeroen on 4-9-2015.
 * Factory class for creating the board class
 */
public class BoardFactory {

    /**
     * The sprite store providing the sprites for the game.
     */
    private final SpriteStore spriteStore;

    /**
     * Creates the boadfactory that will create the board that
     * will load the jewels.
     * @param spriteStore The sprite store providing the sprites.
     */
    public BoardFactory(SpriteStore spriteStore) {
        this.spriteStore = spriteStore;
    }

    /**
     *
     * @param width The width of the board.
     * @param height The height of the board.
     * @return A new Board.
     */
    public Board generateBoard(Group sceneNodes, double width, double height) {
        Random rand = new Random();
        Jewel[][] grid = new Jewel[8][8];

        // create the boards jewels
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
               Jewel jewel = new Jewel(rand.nextInt((7 - 1) + 1) + 1, i, j);
            //	Jewel jewel = new Jewel((i+j)%7+1, i, j);
                jewel.xPos = i * (width / 8.0);
                jewel.yPos = j * (height / 8.0);
                grid[i][j] = jewel;

                // add to actors in play (sprite objects)
                spriteStore.addSprites(jewel);

                // add sprites
                sceneNodes.getChildren().add(0, jewel.node);
            }
        }

        Board board = new Board(grid, sceneNodes, width, height);
        board.setSpriteStore(spriteStore);

        // add event handlers.
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Jewel jewel = grid[i][j];
                grid[i][j].node.addEventFilter(MouseEvent.MOUSE_CLICKED,
                        new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent event) {
                                System.out.println("Jewel[" + jewel.getBoardX() + "][" + jewel.getBoardY() + "] " + event.getEventType());
                                board.addSelection(jewel);
                                event.consume();
                            }
                        }
                );
            }
        }

        return board;
    }
}
