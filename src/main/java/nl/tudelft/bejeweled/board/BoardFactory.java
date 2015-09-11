package nl.tudelft.bejeweled.board;

import nl.tudelft.bejeweled.sprite.Jewel;
import nl.tudelft.bejeweled.sprite.SpriteStore;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
     * Generates a new board from text file.
     * @param file Name of the board configuration file.
     * @param sceneNodes The group container for the Jewel nodes.
     * @param gridWidth Width of the board in squares.
     * @param gridHeight Height of the board in squares.
     * @param spriteWidth Width of the sprites in pixels.
     * @param spriteHeight Height of the sprites in pixels.
     * @return A new Board.
     */
    public Board fromTextGenerateBoard(String file, Group sceneNodes, int gridWidth, int gridHeight, int spriteWidth, int spriteHeight) {
        Jewel[][] grid = new Jewel[gridWidth][gridHeight];
        InputStream in = BoardFactory.class.getResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        int k = 0;
        try {
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("|");
                
                for (int j = 0; j < gridHeight; j++) {
                    int amr = Integer.parseInt(parts[j]);
                    System.out.println(amr);
                    Jewel jewel = new Jewel(amr, k, j);
                    jewel.setxPos(k * spriteWidth);
                    jewel.setyPos(j * spriteHeight);
                    grid[k][j] = jewel;
                    
                    // add to actors in play (sprite objects)
                    spriteStore.addSprites(jewel);
                    
                    // add sprites
                    sceneNodes.getChildren().add(0, jewel.getNode());
                }
                k++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Board board = new Board(grid, sceneNodes, gridWidth, gridHeight, spriteWidth, spriteHeight);
        board.setSpriteStore(spriteStore);
        
        // add event handlers.
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Jewel jewel = grid[i][j];
                grid[i][j].getNode().addEventFilter(MouseEvent.MOUSE_CLICKED,
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
    /**
     * Generates a new board.
     * @param sceneNodes The group container for the Jewel nodes.
     * @param gridWidth Width of the board in squares.
     * @param gridHeight Height of the board in squares.
     * @param spriteWidth Width of the sprites in pixels.
     * @param spriteHeight Height of the sprites in pixels.
     * @return A new Board.
     */
    public Board generateBoard(Group sceneNodes, int gridWidth, int gridHeight, int spriteWidth, int spriteHeight) {
        Random rand = new Random();
        Jewel[][] grid = new Jewel[gridWidth][gridHeight];

        // create the boards jewels
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
               Jewel jewel = new Jewel(rand.nextInt(Board.NUMBER_OF_DIFFERENT_JEWEL_TYPES) + 1, i, j);
            //	Jewel jewel = new Jewel((i+j)%7+1, i, j);
                jewel.setxPos(i * spriteWidth);
                jewel.setyPos(j * spriteHeight);
                grid[i][j] = jewel;

                // add to actors in play (sprite objects)
                spriteStore.addSprites(jewel);

                // add sprites
                sceneNodes.getChildren().add(0, jewel.getNode());
            }
        }

        Board board = new Board(grid, sceneNodes, gridWidth, gridHeight, spriteWidth, spriteHeight);
        board.setSpriteStore(spriteStore);

        // add event handlers.
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Jewel jewel = grid[i][j];
                grid[i][j].getNode().addEventFilter(MouseEvent.MOUSE_CLICKED,
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
