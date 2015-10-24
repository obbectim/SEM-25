package nl.tudelft.bejeweled.board;
import java.io.Serializable;

import nl.tudelft.bejeweled.game.BejeweledGame;
import nl.tudelft.bejeweled.jewel.BasicJewel;
import nl.tudelft.bejeweled.jewel.Jewel;
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
public class BoardFactory implements Serializable {
	private int gridWidth;
	private int gridHeight;
	private int spriteWidth;
	private int spriteHeight;

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
     * @return A new Board.
     */
    public Board fromTextGenerateBoard(String file, Group sceneNodes) {
        this.gridWidth = BejeweledGame.GRID_WIDTH;
        this.gridHeight = BejeweledGame.GRID_HEIGHT;
        this.spriteWidth = BejeweledGame.SPRITE_WIDTH;
        this.spriteHeight = BejeweledGame.SPRITE_WIDTH;  
        Jewel[][] grid = new Jewel[gridWidth][gridHeight];
        InputStream in = BoardFactory.class.getResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line; int k = 0;
        try {
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("|");  
                for (int j = 0; j < gridHeight; j++) {
                    int amr = Integer.parseInt(parts[j]);
                    Jewel jewel = new BasicJewel(amr, k, j, k * spriteWidth, j * spriteHeight);
                    grid[k][j] = jewel;
                    // add to actors in play (sprite objects)
                    spriteStore.addSprite(jewel.getSprite());
                    sceneNodes.getChildren().add(0, jewel.getSprite().getNode()); 
                } k++;        
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addEventHandler(grid, sceneNodes, gridWidth, gridHeight,
       		 spriteWidth,  spriteHeight);
    }
    /**
     * Generates a new board.
     * @param sceneNodes The group container for the Jewel nodes.
     * @return A new Board.
     */
    public Board generateBoard(Group sceneNodes) {
        this.gridWidth = BejeweledGame.GRID_WIDTH;
        this.gridHeight = BejeweledGame.GRID_HEIGHT;
        this.spriteWidth = BejeweledGame.SPRITE_WIDTH;
        this.spriteHeight = BejeweledGame.SPRITE_WIDTH;
        
        Random rand = new Random();
        Jewel[][] grid = new Jewel[gridWidth][gridHeight];

        // create the boards jewels
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
               Jewel jewel = new BasicJewel(rand.nextInt(Board.NUMBER_OF_JEWEL_TYPES) + 1, i, j,
            		   i * spriteWidth, j * spriteHeight);
                grid[i][j] = jewel;

                // add to actors in play (sprite objects)
                spriteStore.addSprite(jewel.getSprite());

                // add sprites
                sceneNodes.getChildren().add(0, jewel.getSprite().getNode());
            }
        }

        return addEventHandler(grid, sceneNodes, gridWidth, gridHeight,
        		 spriteWidth,  spriteHeight);
    }

    /**
     * Event Handler.
     * @param grid The grid containing jewels
     * @param sceneNodes The group container for the Jewel nodes.
     * @param gridWidth Width of the board in squares.
     * @param gridHeight Height of the board in squares.
     * @param spriteWidth Width of the sprites in pixels.
     * @param spriteHeight Height of the sprites in pixels.
     * @return A new Board.
     * 
     * */
	public Board addEventHandler(Jewel[][] grid, Group sceneNodes, int gridWidth, int gridHeight,
    		int spriteWidth, int spriteHeight) {
		Board board = new Board(grid, sceneNodes);
        board.setSpriteStore(spriteStore);

        // add event handlers.
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Jewel jewel = grid[i][j];
                grid[i][j].getSprite().getNode().addEventFilter(MouseEvent.MOUSE_CLICKED,
                        new EventHandler<MouseEvent>() {
                          public void handle(MouseEvent event) {
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
