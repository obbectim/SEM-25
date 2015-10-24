package nl.tudelft.bejeweled.board;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import nl.tudelft.bejeweled.game.BejeweledGame;
import nl.tudelft.bejeweled.jewel.BasicJewel;
import nl.tudelft.bejeweled.jewel.ExplosivePowerUp;
import nl.tudelft.bejeweled.jewel.HyperPowerUp;
import nl.tudelft.bejeweled.jewel.Jewel;
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.ExplosiveSprite;
import nl.tudelft.bejeweled.sprite.SelectionCursor;
import nl.tudelft.bejeweled.sprite.SpriteState;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Created by Jeroen on 4-9-2015.
 * Class that encapsulates the board.
 */
public class Board implements Serializable {
	private int gridWidth;
	private int gridHeight;
	private int spriteWidth;
	private int spriteHeight;
	private static final int MINIMAL_COMBO_LENGTH = 3;
	private static final int EXPLOSIVE_JEWEL_COMBO_LENGTH = 4;
	private static final int HYPER_JEWEL_COMBO_LENGTH = 5;
	public static final int NUMBER_OF_JEWEL_TYPES = 7;

    private List<Jewel> selection = new ArrayList<>();
    
    private transient Jewel[][] grid;
    private List<Integer> explosivesSave;
    private List<Integer> hyperSave;
    private transient List<BoardObserver> observers;

    /** The JavaFX group containing all the jewels. */
    private transient Group sceneNodes;

	private SpriteStore spriteStore;
	private SelectionCursor selectionCursor;
	private boolean toReverseMove = false;
	private boolean empty = false;
	private transient Jewel reverse1;
	private transient Jewel reverse2;
	private int[][] state;
	private boolean locked = false;

    /**
     * Constructor for the board class.
     * @param grid Two-dimensional grid holding all Jewel sprites.
     * @param sceneNodes The JavaFX group container for the Jewel Nodes.
     */
    public Board(Jewel[][] grid, Group sceneNodes) {
        this.gridWidth = BejeweledGame.GRID_WIDTH;
        this.gridHeight = BejeweledGame.GRID_HEIGHT;
        this.spriteWidth = BejeweledGame.SPRITE_WIDTH;
        this.spriteHeight = BejeweledGame.SPRITE_HEIGHT;
        
        this.grid = grid;
        this.sceneNodes = sceneNodes;
        
        this.observers = new ArrayList<>();
    }

    /**
     * Adds an observer for the board.
     * @param observer BoardObserver to be added to the list of observers
     */
    public void addObserver(BoardObserver observer) {
    	if (!observers.contains(observer)) {
    		observers.add(observer);
    	}
    }

    /**
     * Updates the observers a jewel was removed.
     */
    private void updateScore() {
        for (BoardObserver observer : observers) {
            observer.boardJewelRemoved();
        }
    }

    /**
     * Handler method for detecting a click on the board.
     * @param noJewelHit Boolean determining if mouse is over a Jewel or not
     */
    public void boardClicked(Boolean noJewelHit) {
        if (noJewelHit) {
            if (getSelectionCursor() != null) {
                // if there was a selection remove it
                sceneNodes.getChildren().remove(getSelectionCursor().getNode());
                selectionCursor = null;
            }
            getSelection().clear();
        }
        
    }

    /**
     * Add Jewel to the current selection.
     * After 2 Jewels are selected they are swapped.
     * @param jewel The Jewel to be added to the current selection.
     */
    public void addSelection(Jewel jewel) {
    	if (!isLocked() && !anyJewelsAnimating()) {
    		getSelection().add(jewel);
    		//TODO Cleanup this method with better logic.
    		if (getSelection().size() == 1) {
    			selectionCursor = new SelectionCursor(getSelection().get(0).getSprite().getxPos(), 
    					getSelection().get(0).getSprite().getyPos());
    			spriteStore.addSprite(getSelectionCursor());
    			sceneNodes.getChildren().add(0, getSelectionCursor().getNode());
    		}
    		// 2 gems are selected, see if any combo's are made
    		if (getSelection().size() == 2) {
    			
    			//Check for hypermoves
    			if (getSelection().get(0).isHyper()) {
    				hyperMove(getSelection().get(0), getSelection().get(1));
    			} else if (getSelection().get(1).isHyper()) {
    				hyperMove(getSelection().get(1), getSelection().get(0));
    			} else if (moveWithinDomain(getSelection().get(0), getSelection().get(1))) {
    				Logger.logInfo("Swapping jewels " + getSelection().get(0).toString()
    						+ " and " + getSelection().get(1).toString());
    				swapJewel(getSelection().get(0), getSelection().get(1));

    				int comboCount = checkBoardCombos();
    				Logger.logInfo("Combo Jewels on board: " + comboCount);
    				if (comboCount == 0) {
    					setToReverse(getSelection().get(0), getSelection().get(1));
    				}
    			}
    			sceneNodes.getChildren().remove(getSelectionCursor().getNode());
    			selectionCursor = null;
    			getSelection().clear();
    		}
    	}
    }

	private void hyperMove(Jewel hyperjewel, Jewel jewel2) {
		hyperjewel.implode(sceneNodes);
	    for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
            	if (grid[x][y].getType() == jewel2.getType()) {
	                grid[x][y].implode(sceneNodes);
            	}
            }
        }
	}

	/**
     * Set up two jewels to be swapped, intended to undo an invalid move.
     * @param jewel1 The first Jewel.
     * @param jewel2 The second Jewel.
     */
    private void setToReverse(Jewel jewel1, Jewel jewel2) {
		toReverseMove = true;
		reverse1 = jewel1;
		reverse2 = jewel2;
	}
    
	/**
     * Jewels set up to be swapped will swap if their current animations have finished.
     */
    private void tryToReverse() {
    	assert (reverse1 != null);
    	assert (reverse2 != null);
 		if (!reverse1.getSprite().animationActive() && !reverse2.getSprite().animationActive()) {
 			swapJewel(reverse1, reverse2);
 			toReverseMove = false;
 			reverse1 = null;
 			reverse2 = null;
 		}
 	}
     

	/**
     * Checks if two Jewels are exactly one apart either horizontally
     * or vertically.
     * @param j1 The first Jewel.
     * @param j2 The second Jewel.
     * @return True if the Jewels can be swapped.
     */
    private Boolean moveWithinDomain(Jewel j1, Jewel j2) {
        if (Math.abs(j1.getBoardX() - j2.getBoardX()) 
        		+ Math.abs(j1.getBoardY() - j2.getBoardY()) == 1) {
            return true;
        }
        return false;
    }
    
    /**
     * Swaps two Jewel's with one another.
     * Both the grid positions as the graphics positions are updated.
     * @param j1 First Jewel to be swapped.
     * @param j2 Second Jewel to be swapped.
     */
    private void swapJewel(Jewel j1, Jewel j2) {     
        //Swap the jewels in the board
        grid[j1.getBoardX()][j1.getBoardY()] = j2;
        grid[j2.getBoardX()][j2.getBoardY()] = j1;
        
        //TODO create a moveTo function for Jewel that in turn moves it sprite
        //Swap the positions of the sprite images
        int previousJ1X = j1.getSprite().getxPos();
    	int previousJ1Y = j1.getSprite().getyPos();
    	j1.getSprites().forEach((sprite) -> sprite.moveTo(j2.getSprite().getxPos(), j2.getSprite().getyPos()));
    	j2.getSprites().forEach((sprite) -> sprite.moveTo(previousJ1X, previousJ1Y));

       // j1.getSprite().moveTo(j2.getSprite().getxPos(), j2.getSprite().getyPos());
     //   j2.getSprite().moveTo(previousJ1X, previousJ1Y);
        
    	//Swap the jewel's variables of its position on the board
    	int previousJ1I = j1.getBoardX();
        int previousJ1J = j1.getBoardY();
        j1.setBoardX(j2.getBoardX());
        j1.setBoardY(j2.getBoardY());
        j2.setBoardX(previousJ1I);
        j2.setBoardY(previousJ1J);
    }

    /**
     * Checks the board for combos in columns.
     * @param comboList List to store the combo in
     */
    private void checkVerticalCombo(List<Jewel> comboList) {
        // list to hold current selection of combo
        Stack<Jewel> current = new Stack<>();
        int matches;
        int type;

        // first check columns for combos
        for (int col = 0; col < grid.length; col++) {
            matches = 0;
            type = 0;
            for (int i = 0; i < grid[0].length; i++) {
                if (grid[col][i].getType() == type && type != 0
                        && grid[col][i].getSprite().getState() != SpriteState.TO_BE_REMOVED) {
                    matches++;
                    current.push(grid[col][i]);
                } //subtract 1 because arrays start at 0
                if (grid[col][i].getType() != type || i == grid[0].length - 1) {
                    if (matches >= MINIMAL_COMBO_LENGTH) {
                        while (!current.empty()) {
                            comboList.add(current.pop());
                        }
                    }
                    current.clear();
                    type = grid[col][i].getType();
                    current.push(grid[col][i]);
                    matches = 1;
                }
            }
        }
    }
    
    /** Checks the board for combos in rows.
     * @param comboList List to store the combo in
     */
    private void checkHorizontalCombo(List<Jewel> comboList) {
        // list to hold current selection of combo
        Stack<Jewel> current = new Stack<>();
        int matches;
        int type;
        
        for (int row = 0; row < grid[0].length; row++) {
            matches = 0;
            type = 0;
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][row].getType() == type && type != 0
                        && grid[i][row].getSprite().getState() != SpriteState.TO_BE_REMOVED) {
                    matches++;
                    current.push(grid[i][row]);
                }
                if (grid[i][row].getType() != type || i == grid[0].length - 1) {
                    if (matches >= MINIMAL_COMBO_LENGTH) {
                        while (!current.empty()) {
                            comboList.add(current.pop());
                        }
                    }
                    current.clear();
                    type = grid[i][row].getType();
                    current.push(grid[i][row]);
                    matches = 1;
                }
            }
        }
    }
    
    /**
     * Check the board for any combo Jewels.
     * @return The number of Jewels removed from the game.
     */
    public int checkBoardCombos() {
        List<Jewel> comboList = new ArrayList<>();

        checkVerticalCombo(comboList);
        
        checkHorizontalCombo(comboList);
        
        // remove duplicates by putting the list in set
        Set<Jewel> comboSet = new HashSet<>();
        comboSet.addAll(comboList);
        comboList.clear();
        comboList.addAll(comboSet);
        int count = comboList.size();
        
        List<Jewel> additionalJewels = new ArrayList();
        // check if any of the jewels is a 
        for (Iterator<Jewel> jewelIterator = comboList.iterator(); jewelIterator.hasNext();) {
            Jewel jewel = jewelIterator.next();
            
            if (jewel.isExplosive())
            	additionalJewels.addAll(explosiveSurrounding(jewel, comboList));

        }
        
        comboList.addAll(additionalJewels);
        
        for (Iterator<Jewel> jewelIterator = comboList.iterator(); jewelIterator.hasNext();) {
            Jewel jewel = jewelIterator.next();
            // remove the JavaFX nodes from the scene group and animate an implosion
            jewel.implode(sceneNodes);
            // remove the event filter
            jewel.getNodes().forEach((node) -> node.setOnMouseClicked(null));
            updateScore();
            // TODO Make sure the Jewels are also removed from the spriteStore.
            // grid[jewel.getBoardX()][jewel.getBoardY()] = null;
        }
        //Check if PowerJewels should be generated.
         if (count == EXPLOSIVE_JEWEL_COMBO_LENGTH) { 
	        	Jewel powerjewel = comboList.get(0);
	        	addExplosiveJewel(	powerjewel.getType(), 
	        						powerjewel.getBoardX(), 
	        						powerjewel.getBoardY()
	        						);
	        }
         if (count >= HYPER_JEWEL_COMBO_LENGTH) { 
	        	Jewel powerjewel = comboList.get(0);
	        	addHyperJewel(		powerjewel.getType(), 
	        						powerjewel.getBoardX(), 
	        						powerjewel.getBoardY()
	        						);
	        }
        return count;
    }

    List<Jewel> explosiveSurrounding(Jewel jewel, List<Jewel> comboList) {
    	List<Jewel> newComboList = new ArrayList();
    	newComboList.addAll(comboList);
    	
    	System.out.println("Hello World");
    	// Upper left corner of explosion region
    	int x0 = -1;
    	int y0 = -1;
    	
    	// Lower right corner of the explosion region
    	int x1 = 1;
    	int y1 = 1;
    	
    	x0 = Math.max(0, x0+jewel.getBoardX());
    	x1 = Math.min(7, x1+jewel.getBoardX());
    	
    	y0 = Math.max(0, y0+jewel.getBoardY());
    	y1 = Math.min(7, y1+jewel.getBoardY());
    	    	
    	for (int i = x0; i <= x1; i++)
    		for (int j = y0; j <= y1; j++) {
    			System.out.println(i + ", " + j);
    			if (!newComboList.contains(grid[i][j])) {
    				newComboList.add(grid[i][j]);
    				System.out.println("Added");
    			}
    		}
    				
    	return newComboList;
    }
    
    /**
     * Function that checks if there are any moves possible.
     * 
     * <p>Iterates through all gems and looks for pairs or two or 
     * constructions like "xox" where another x could fill in.
     * For each case a different function is called which checks for
     * a valid move.</p>
     *
     * @return returns two jewels in a list to swap if move is possible
     */
    private List<Jewel> getValidMovePair(BoardMoveStrategy strategy) {
		return strategy.getValidMovePair();
    }
    
    /**
     * Function that checks if there are any moves possible.
     * @return true if no moves possible
     */
    public boolean outOfMoves() {
        if (!getValidMovePair(new BruteForceStrategy(grid)).isEmpty()) {
            return false;
        }
    	for (BoardObserver observer : observers) {
			observer.boardOutOfMoves();
    	}
		return true;
    }
    
    /**
     * This function adds a jewel of a random type to the grid at the specified position.
     * @param i Grid column
     * @param j Grid row
     */
    protected void addRandomJewel(int i, int j) {
        Random rand = new Random();
        Jewel jewel = new BasicJewel(rand.nextInt(NUMBER_OF_JEWEL_TYPES) + 1, i, j,
        		i * spriteWidth, j * spriteHeight);
        grid[i][j] = jewel;
        spriteStore.addSprites(jewel.getSprites());
        sceneNodes.getChildren().addAll(0, jewel.getNodes());
        setSpriteStore(spriteStore);
        grid[i][j].getNodes().forEach((node) -> node.addEventFilter(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    addSelection(jewel);
                    event.consume();
                }
            }
        )
        );
    }
    
    /**
     * This function adds an explosive jewel of the selected type to the grid at the specified position.
     * @param i Grid column
     * @param j Grid row
     */
    protected void addExplosiveJewel(int type, int i, int j) {
        Jewel jewel = new ExplosivePowerUp(new BasicJewel(type, i, j,
        		i * spriteWidth, j * spriteHeight));
        grid[i][j] = jewel;
        spriteStore.addSprites(jewel.getSprites());
        sceneNodes.getChildren().addAll(0, jewel.getNodes());
        setSpriteStore(spriteStore);
        grid[i][j].getNodes().forEach((node) -> node.addEventFilter(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    addSelection(jewel);
                    event.consume();
                }
            }
        )
        );
        grid[i][j].getSprites().forEach((sprite)-> sprite.fadeIn(sceneNodes)); 
     }
    
    /**
     * This function adds an explosive jewel of the selected type to the grid at the specified position.
     * @param i Grid column
     * @param j Grid row
     */
    protected void addHyperJewel(int type, int i, int j) {
        Jewel jewel = new HyperPowerUp(new BasicJewel(type, i, j,
        		i * spriteWidth, j * spriteHeight));
        grid[i][j] = jewel;
        spriteStore.addSprites(jewel.getSprites());
        sceneNodes.getChildren().addAll(0, jewel.getNodes());
        setSpriteStore(spriteStore);
        grid[i][j].getNodes().forEach((node) -> node.addEventFilter(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    addSelection(jewel);
                    event.consume();
                }
            }
        )
        );
        grid[i][j].getSprites().forEach((sprite)-> sprite.fadeIn(sceneNodes));  
      }
    
    
    /**
     * This function adds a animating jewel of a random type to the grid at the specified position.
     * The jewel is initially shown at a different position, and moves to its target spot.
     * @param i Grid column
     * @param j Grid row
     * @param translateX X offset in pixels
     * @param translateY Y offset in pixels
     */
    protected void addRandomJewel(int i, int j, int translateX, int translateY) {
    	  addRandomJewel(i, j);
          if (translateX != 0 || translateY != 0) {
        	  grid[i][j].getSprite().setState(SpriteState.ANIMATION_ACTIVE);
        	  grid[i][j].getNodes().forEach((node) -> node.setTranslateX(translateX));
        	  grid[i][j].getNodes().forEach((node) -> node.setTranslateY(translateY));
          }
    }

    /**
     * This function updates the positions of the jewels of the grid.
     * Jewels with empty spots under them are moved down, 
     * and the empty positions are filled with new jewels
     */
    private void updateJewelPositions() {
    	for (int i = 0; i < gridWidth; i++) {	
    		int emptySpots = 0;
    		for (int j = gridHeight - 1; j >= 0; j--) {
    			if (grid[i][j] == null || grid[i][j].getSprite().getState() == SpriteState.TO_BE_REMOVED) {
    				emptySpots++;
    			} else {
    				if (emptySpots > 0) {
        				grid[i][j + emptySpots] = grid[i][j];
    					moveJewelDown(grid[i][j], emptySpots);
    				}
    			}
    		}
    		for (int k = 0; k < emptySpots; k++) {
    			addRandomJewel(i, k, 0, -(emptySpots) * spriteHeight);
    		}
    	}
    }

    /**
     * A function to move a jewel down on the board with correct animation.
     * @param jewel The jewel to move
     * @param spots The number of spots to move down
     */
    private void moveJewelDown(Jewel jewel, int spots) {
        jewel.setBoardY(jewel.getBoardY() + spots);
    	jewel.relativeMoveTo(0, spots * this.spriteHeight);
    }

    /**
	 * Setter function for the current spriteStore.
	 * @param spriteStore spriteStore to be set
	 */
	public void setSpriteStore(SpriteStore spriteStore) {
		this.spriteStore = spriteStore;
	}
	
    /**
	 * Update the board; check for combos, and fill empty spots .
	 * 	 */
	public void update() {
		if (!anyJewelsAnimating()) {
			if (empty) {
				spawnJewels();
				empty = false;
			} else if (toReverseMove) {
				tryToReverse();
			} else {
				checkBoardCombos();
				updateJewelPositions();
				outOfMoves();
			}
		}
	}
	
	/**
	 * Spawn new jewels at the start of a new level.
	 */
	private void spawnJewels() {
	    for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
            	 addRandomJewel(i, j);
            	 grid[i][j].fadeIn(sceneNodes); 
            }
        }
	}

	/**
	 * Getter function for the current jewel Grid (used for testing).
	 * @return current grid of jewels
	 */
	public Jewel[][] getGrid() {
		return grid;
	}

	/**
	 * Getter function for the current selection (used for testing).
	 * @return current selection
	 */
	public List<Jewel> getSelection() {
		return selection;
	}
	
	/**
	 * Getter function for the current selectionCursor (used for testing).
	 * @return current selectionCursor
	 */
	public SelectionCursor getSelectionCursor() {
		return selectionCursor;
	}

    /**
     * Setter method for the state grid.
     * @param state The state grid.
     */
    public void setState(int[][] state) {
        this.state = state;
    }
    
    /**
     * Fade out the entire grid.
     */
    public void implodeGrid() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
            	if (grid[x][y] != null) {
	                grid[x][y].implode(sceneNodes);
            	}
            }
        }
        empty = true;
    }

	/**
	 * Calculates and visualizes a hint for a next move.
	 */
	public void showHint() {
		
		List<Jewel> swap = getValidMovePair(new BruteForceStrategy(grid));
		
		if (!swap.isEmpty()) {
            addSelection(swap.get(1));
        }
	}

    /**
     * Determines if any Jewel on the board is still animating.
     * @return True if any Jewels are falling, otherwise false.
     */
	private boolean anyJewelsAnimating() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (grid[x][y] != null && grid[x][y].getSprite().animationActive()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Process grid to save its state.
	 * @return processed grid
	 */
	public int[][] convertGrid() {
		int[][] mGrid = new int[gridWidth][gridHeight]; 
		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				mGrid[i][j] = grid[i][j].getType();  
			}	
		}
		return mGrid;
		
	}
	
    /**
     * Make grid from saved state.
     * @param sceneNodes the sceneNodes used for the display of the jewels.
     */
	public void makeGrid(Group sceneNodes) {
        this.observers = new ArrayList<>();
		grid = new Jewel[gridWidth][gridHeight];
		this.sceneNodes = sceneNodes;
		for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
               Jewel jewel = new BasicJewel(state[i][j], i, j, i * spriteWidth, j * spriteHeight); 
                grid[i][j] = null;
                grid[i][j] = jewel;
                spriteStore.addSprite(jewel.getSprite());
                sceneNodes.getChildren().add(0, jewel.getSprite().getNode());
                setSpriteStore(spriteStore);
                grid[i][j].getSprite().getNode().addEventFilter(MouseEvent.MOUSE_CLICKED,
                        new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent event) {
                                addSelection(jewel);
                                event.consume();
                            }
                        }
                );
            }
        }
	}

	/**
	 * Test whether the board is locked for modification.
	 * @return whether the board is locked for modification.
	 */
	public boolean isLocked() {
		return locked;
	}
	
	public void saveExplosivesAndHypers() {
		explosivesSave = new ArrayList<Integer>();
		hyperSave = new ArrayList<Integer>();
		for (Jewel[] column : grid) {
			for (Jewel jewel : column) {
				if (jewel.isExplosive()) {
					explosivesSave.add(jewel.getType());
					explosivesSave.add(jewel.getBoardX());
					explosivesSave.add(jewel.getBoardY());
				}
				if (jewel.isHyper()) {
					hyperSave.add(jewel.getType());
					hyperSave.add(jewel.getBoardX());
					hyperSave.add(jewel.getBoardY());
				}
			}
		}
	}
	
	public void restoreExplosivesAndHypers() {
		for (int i = 0; i < explosivesSave.size(); i+=3) {
			int type = explosivesSave.get(i);
			int x = explosivesSave.get(i+1);
			int y = explosivesSave.get(i+2);
			grid[x][y].implode(sceneNodes);
			addExplosiveJewel(type, x, y);
		}
		for (int i = 0; i < hyperSave.size(); i+=3) {
			int type = hyperSave.get(i);
			int x = hyperSave.get(i+1);
			int y = hyperSave.get(i+2);
			grid[x][y].implode(sceneNodes);
			addHyperJewel(type, x, y);
		}
	}
	
	/**
	 * Lock or unlock the board for modification.
	 * @param locked true if the board is to be locked
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}
