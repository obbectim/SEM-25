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
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.Jewel;
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
	public static final int NUMBER_OF_JEWEL_TYPES = 7;

    private List<Jewel> selection = new ArrayList<Jewel>();
    
    private Random rand = new Random();
    
    private transient Jewel[][] grid;

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
     * Removes an observer of the board.
     * @param observer BoardObserver to be removed
     */
    public void removeObserver(BoardObserver observer) {
    	observers.remove(observer);
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
    	if (!isLocked()) {
    		getSelection().add(jewel);
    		//TODO Cleanup this method with better logic.
    		if (getSelection().size() == 1) {
    			selectionCursor = new SelectionCursor(getSelection().get(0).getxPos(), 
    					getSelection().get(0).getyPos());
    			spriteStore.addSprites(getSelectionCursor());
    			sceneNodes.getChildren().add(0, getSelectionCursor().getNode());
    		}
    		// 2 gems are selected, see if any combo's are made
    		if (getSelection().size() == 2) {

    			if (moveWithinDomain(getSelection().get(0), getSelection().get(1))) {
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
 		if (!reverse1.animationActive() && !reverse2.animationActive()) {
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
    public Boolean moveWithinDomain(Jewel j1, Jewel j2) {
        if (Math.abs(j1.getBoardX() - j2.getBoardX()) 
        		+ Math.abs(j1.getBoardY() - j2.getBoardY()) == 1) {
            return true;
        }
        return false;
    }

    
    /**
     * Performs a horizontal swap of two Jewels.
     * @param j1 First jewel to swap
     * @param j2 Second jewel to swap
     */
    private void horizontalSwap(Jewel j1, Jewel j2) {
    	int tmp;
    	double previousJ1, previousJ2;
    	tmp = j1.getBoardX();
        j1.setBoardX(j2.getBoardX());
        j2.setBoardX(tmp);

        previousJ1 = j1.getxPos();
        j1.setxPos(j2.getxPos());
        j1.getNode().setTranslateX(previousJ1 - j1.getxPos());
        previousJ2 = j2.getxPos();
        j2.setxPos(previousJ1);
        j2.getNode().setTranslateX(previousJ2 - j2.getxPos());
    }
    
    /**
     * Performs a vertical Swap of two Jewels.
     * @param j1 First jewel to swap
     * @param j2 Second jewel to swap
     */
    private void verticalSwap(Jewel j1, Jewel j2) {
    	int tmp;
    	double previousJ1, previousJ2;
    	tmp = j1.getBoardY();
        j1.setBoardY(j2.getBoardY());
        j2.setBoardY(tmp);

        previousJ1 = j1.getyPos();
        j1.setyPos(j2.getyPos());
        j1.getNode().setTranslateY(previousJ1 - j1.getyPos());
        previousJ2 = j2.getyPos();
        j2.setyPos(previousJ1);
        j2.getNode().setTranslateY(previousJ2 - j2.getyPos());
    }
    
    /**
     * Swaps two Jewel's with one another.
     * Both the grid positions as the graphics positions are updated.
     * @param j1 First Jewel to be swapped.
     * @param j2 Second Jewel to be swapped.
     */
    public void swapJewel(Jewel j1, Jewel j2) {
        // TODO This can probably be done nicer
        int x1, x2, y1, y2;
        x1 = j1.getBoardX();
        y1 = j1.getBoardY();
        x2 = j2.getBoardX();
        y2 = j2.getBoardY();

        Jewel tmpJewel;
        // find out what direction the change is
        // horizontal swap
        if (Math.abs(j1.getBoardX() - j2.getBoardX()) == 1) {
            horizontalSwap(j1, j2);
        }
        else if (Math.abs(j1.getBoardY() - j2.getBoardY()) == 1) {
            // vertical swap
            verticalSwap(j1, j2);

        }

        tmpJewel = j1;
        grid[x1][y1] = j2;
        grid[x2][y2] = tmpJewel;
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
                        && grid[col][i].getState() != SpriteState.TO_BE_REMOVED) {
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
                        && grid[i][row].getState() != SpriteState.TO_BE_REMOVED) {
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

        for (Iterator<Jewel> jewelIterator = comboList.iterator(); jewelIterator.hasNext();) {
            Jewel jewel = jewelIterator.next();
            // remove the JavaFX nodes from the scene group and animate an implosion
            jewel.implode(sceneNodes);
            // remove the event filter
            jewel.getNode().setOnMouseClicked(null);
            updateScore();
            // TODO Make sure the Jewels are also removed from the spriteStore.
            // grid[jewel.getBoardX()][jewel.getBoardY()] = null;
        }

        return count;
    }
    
    
    /**
     * Checks the left most completion option for a valid move.
     * 
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkLeft(Jewel[][] jewels, int x, int y) {
    	if (y > 0) {
    		if (y > 1 && jewels[x][y - 2].getType() == jewels[x][y].getType()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Checks the left-top completion option for a valid move.
     * 
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkLeftTop(Jewel[][] jewels, int x, int y) {
    	if (y > 0) {
    		if (x > 0 && jewels[x - 1][y - 1].getType() == jewels[x][y].getType()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Checks the left-bottom completion option for a valid move.
     * 
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkLeftBottom(Jewel[][] jewels, int x, int y) {
    	if (y > 0) {
    		if (x < jewels.length - 1 && jewels[x + 1][y - 1].getType()
    				== jewels[x][y].getType()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Checks the right most completion option for a valid move.
     * 
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkRight(Jewel[][] jewels, int x, int y) {
    	final int three = 3;
    	
    	if (y < jewels[0].length - 2) {
    		if (y < jewels[0].length - three && jewels[x][y + three].getType() 
    				== jewels[x][y].getType()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Checks the right-top completion option for a valid move.
     * 
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkRightTop(Jewel[][] jewels, int x, int y) {
    	if (y < jewels[0].length - 2) {
    		
    		if (x > 0 && jewels[x - 1][y + 2].getType() == jewels[x][y].getType()) {
    			return true;
    		}
    		
    	}
    	
    	return false;
    }
    
    /**
     * Checks the right-top completion option for a valid move.
     * 
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkRightBottom(Jewel[][] jewels, int x, int y) {
    	if (y < jewels[0].length - 2) {
    		if (x < jewels.length - 1 && jewels[x + 1][y + 2].getType() 
    				== jewels[x][y].getType()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Checks if a pair of similar jewels can be extended by a valid move (done for a vertical pair
     * but can be used for horizontal if jewels matrix is transposed).
     * @param jewels The grid containing the Jewels
     * @param x x-position of the first jewel of the pair to be extended
     * @param y y-position of the first jewel of the pair to be extended
     * @return a list of the coordinates of the Jewels (x1,y1,x2,y2)
     */
    public List<Integer> validMove(Jewel[][] jewels, int x, int y) {
    	final int three = 3;
		if (checkLeft(jewels, x, y)) {
			return Arrays.asList(x, y - 1, x, y - 2);
		}
		if (checkLeftTop(jewels, x, y)) {
			return Arrays.asList(x, y - 1, x - 1, y - 1);
		}
		if (checkLeftBottom(jewels, x, y)) {
			return Arrays.asList(x, y - 1, x + 1, y - 1);
		}
		if (checkRight(jewels, x, y)) {
			return Arrays.asList(x, y + 2, x, y + three);
		}
		if (checkRightTop(jewels, x, y)) {
			return Arrays.asList(x, y + 2, x - 1, y + 2);
		}
		if (checkRightBottom(jewels, x, y)) {
			return Arrays.asList(x, y + 2, x + 1, y + 2);
		}
    	return new ArrayList<Integer>();
    }
    
    /**
     * Checks a vertical pair for a valid move.
     * @param x x-position of the first jewel of the vertical pair
     * @param y y-position of first jewel of the vertical pair
     * @return a list of the two Jewels to be swapped for a valid move
     */
    private List<Jewel> verticalRow(int x, int y) {
    	final int three = 3;
    	List<Integer> indices = validMove(grid, x, y);
    	
    	if (!indices.isEmpty()) {
    		return Arrays.asList(grid[indices.get(0)][indices.get(1)], 
    	                                          grid[indices.get(2)][indices.get(three)]);
    	}
    	return new ArrayList<Jewel>();
    }
    
    /**
     * Checks a horizontal pair for a valid Move.
     * @param x x-position of the first jewel of the horizontal pair
     * @param y y-position of first jewel of the horizontal pair
     * @return a list of the two Jewels to be swapped for a valid move
     */
    private List<Jewel> horizontalRow(int x, int y) {
    	/**
    	 * Create transpose of matrix
    	 */
    	Jewel[][] transposed = new Jewel[grid[0].length][grid.length];
    	for (int i = 0; i < grid[0].length; i++) {
			for (int j = 0; j < grid.length; j++) {
				transposed[i][j] = grid[j][i];
			}
		}
    	
    	final int three = 3;
    	List<Integer> indices = validMove(transposed, y, x);
    	if (!indices.isEmpty()) {
    		return Arrays.asList(grid[indices.get(1)][indices.get(0)], 
    	                                          grid[indices.get(three)][indices.get(2)]);
    	}
    	return new ArrayList<Jewel>();
    }
    
    /**
     * Checks a possible horizontal row (xox) for a valid move.
     * @param x x-position of first jewel of possible row
     * @param y y-position of first jewel of possible row
     */
    private List<Jewel> horizontalRowPossible(int x, int y) {
    	if (y > 0 && grid[x + 1][y - 1].getType() == grid[x][y].getType()) {
    		
        	return Arrays.asList(grid[x + 1][y], grid[x + 1][y - 1]);
    	}
    	if (y < grid[0].length - 1 && grid[x + 1][y + 1].getType() == grid[x][y].getType()) {
    		return Arrays.asList(grid[x + 1][y], grid[x + 1][y + 1]);
    	}
    	
    	return new ArrayList<Jewel>();
    }
    
    /**
     * Checks a possible vertical row (xox) for a valid move.
     * @param x x-position of first jewel of possible row
     * @param y y-position of first jewel of possible row
     */
    private List<Jewel> verticalRowPossible(int x, int y) {
    	if (x > 0 && grid[x - 1][y + 1].getType() == grid[x][y].getType()) {
    		return Arrays.asList(grid[x][y + 1], grid[x - 1][y + 1]);
    	}
    	if (x < grid.length - 1 && grid[x + 1][y + 1].getType() == grid[x][y].getType()) {
    		return Arrays.asList(grid[x][y + 1], grid[x + 1][y + 1]);
    	}
    	return new ArrayList<Jewel>();
    }
    
    /**
     * Function that checks if there are any moves possible.
     * 
     * <p>Iterates through all gems and looks for pairs or two or 
     * constructions like "xox" where another x could fill in.
     * For each case a different function is called which checks for
     * a valid move.</p>
     * 
     * @param x x-position in the grid to be checked for a pair
     * @param y y-position in the grid to be checked for a pair
     * @return returns two jewels in a list to swap if move is possible
     */
    private List<Jewel> checkForPair(int x, int y) {
    	int type = grid[x][y].getType();
    	List<Jewel> swap;
		if (y < grid[0].length - 1 && type == grid[x][y + 1].getType()) {
			swap = verticalRow(x, y);
			if (!swap.isEmpty()) { 
				return swap;
			}
		}
		if (x < grid.length - 1 && type == grid[x + 1][y].getType()) {
			swap = horizontalRow(x, y);
			if (!swap.isEmpty()) {
				return swap;
			}
		}
		if (y < grid[0].length - 2 && type == grid[x][y + 2].getType()) {
			swap = verticalRowPossible(x, y);
			if (!swap.isEmpty()) {
				return swap;
			}
		}
		if (x < grid.length - 2 && type == grid[x + 2][y].getType()) {
			swap = horizontalRowPossible(x, y);
			if (!swap.isEmpty()) {
				return swap;
			}
		}
		return new ArrayList<Jewel>();
    }
    
    /**
     * Function that checks if there are any moves possible.
     * @return true if no moves possible
     */
    public boolean outOfMoves() {
    	for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (!checkForPair(x, y).isEmpty()) {
					return false;
				}
			}
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
    	  Jewel jewel = new Jewel(rand.nextInt(NUMBER_OF_JEWEL_TYPES) + 1, i, j);
          jewel.setxPos(i * spriteWidth);
          jewel.setyPos(j * spriteHeight);
          grid[i][j] = jewel;
          spriteStore.addSprites(jewel);
          sceneNodes.getChildren().add(0, jewel.getNode());
          setSpriteStore(spriteStore);
          grid[i][j].getNode().addEventFilter(MouseEvent.MOUSE_CLICKED,
                  new EventHandler<MouseEvent>() {
                      public void handle(MouseEvent event) {
                          addSelection(jewel);
                          event.consume();
                      }
                  }
          );
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
        	  grid[i][j].setState(SpriteState.ANIMATION_ACTIVE);
        	  grid[i][j].getNode().setTranslateX(translateX);
        	  grid[i][j].getNode().setTranslateY(translateY);
          }
    }

    /**
     * This function updates the positions of the jewels of the grid.
     * Jewels with empty spots under them are moved down, 
     * and the empty positions are filled with new jewels
     */
    public void updateJewelPositions() {
    	for (int i = 0; i < gridWidth; i++) {	
    		int emptySpots = 0;
    		for (int j = gridHeight - 1; j >= 0; j--) {
    			if (grid[i][j] == null || grid[i][j].getState() == SpriteState.TO_BE_REMOVED) {
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
        	jewel.setyPos(jewel.getyPos() + spots * this.spriteHeight);
        	jewel.setBoardY(jewel.getBoardY() + spots);
        	jewel.getNode().setTranslateY(-spots * this.spriteHeight);
    }

    	
    /**
     * This function fills the null spots in the grid[][] with Jewels
     * at the start of the game.
     */
    public void fillNullSpots() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == null) {
                    addRandomJewel(x, y);                
                }
            }
        }
    }
    
    /**
     * Clears the grid and removes the jewels from the scenegroup.
     */
    public void clearGrid() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
            	if (grid[x][y] != null) {
	            	grid[x][y].remove(sceneNodes);
	            	grid[x][y] = null;
            	}
        	}
        }
    }
    
    
    /**
	 * Getter function for the current spriteStore.
	 * @return current spriteStore
	 */

	public Object getSpriteStore() {
		return spriteStore;
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
     * Getter function for the state grid.
     * @return The state grid.
     */
    public int[][] getState() {
        return state;
    }

    /**
     * Setter method for the state grid.
     * @param state The state grid.
     */
    public void setState(int[][] state) {
        this.state = state;
    }

    /**
     * Function to reset the entire grid[][] to null.
     */
    public void resetGrid() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
            	grid[x][y] = null;
            }
        }
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
		
		List<Jewel> swap;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				swap = checkForPair(x, y);
				if (!swap.isEmpty()) {
					addSelection(swap.get(1));
					return;
				}
			}
		}
	}

    /**
     * Determines if any Jewel on the board is still animating.
     * @return True if any Jewels are falling, otherwise false.
     */
	public boolean anyJewelsAnimating() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (grid[x][y] != null && grid[x][y].animationActive()) {
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
               Jewel jewel = new Jewel(state[i][j], i, j); 
                jewel.setxPos(i * spriteWidth); 
                jewel.setyPos(j * spriteHeight); 
                grid[i][j] = null;
                grid[i][j] = jewel;
                spriteStore.addSprites(jewel);
                sceneNodes.getChildren().add(0, jewel.getNode());
                setSpriteStore(spriteStore);
                grid[i][j].getNode().addEventFilter(MouseEvent.MOUSE_CLICKED,
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

	/**
	 * Lock or unlock the board for modification.
	 * @param locked true if the board is to be locked
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}
