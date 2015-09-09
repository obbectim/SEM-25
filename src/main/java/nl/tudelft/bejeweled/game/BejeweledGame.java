package nl.tudelft.bejeweled.game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import nl.tudelft.bejeweled.board.Board;
import nl.tudelft.bejeweled.board.BoardFactory;
import nl.tudelft.bejeweled.board.BoardObserver;
import nl.tudelft.bejeweled.sprite.SpriteStore;


/**
 * Created by Jeroen on 6-9-2015.
 * Bejeweled Game class.
 */
public class BejeweledGame extends Game implements BoardObserver {

    /** The board class that maintains the jewels */
    private Board board;

    private BoardFactory boardFactory;

    private int score = 0;

    private SpriteStore spriteStore;

    /**
     * <code>true</code> if the game is in progress.
     */
    private boolean inProgress;

    private Pane gamePane;

    private Label scoreLabel;

    public BejeweledGame(int framesPerSecond, String windowTitle) {
        super(framesPerSecond, windowTitle);
        spriteStore = new SpriteStore();
        boardFactory = new BoardFactory(spriteStore);
    }

    /**
     *  Starts the game
     */
    @Override
    public void start() {
        if(inProgress)
            return;
        inProgress = true;
        
        System.out.println("Game started");

        score = 0;

        // Create the group that holds all the jewel nodes and create a game scene
        setSceneNodes(new Group());
        gamePane.getChildren().add(new Scene(getSceneNodes(),
                gamePane.getWidth(),
                gamePane.getHeight()).getRoot());

        // generate the jewels
        board = boardFactory.generateBoard(getSceneNodes(), gamePane.getWidth(), gamePane.getHeight());

        // start observing the board for callback events
        board.addObserver(this);
        
        // check for any combo's on the freshly created board
        int comboCount = board.checkBoardCombos();
        System.out.println("Combo Jewels on board: " + comboCount);
    }

    /**
     *  Stops the game
     */
    @Override
    public void stop() {
        if(!inProgress)
            return;

        System.out.println("Game stopped");

        // remove the JavaFX group with jewel nodes
        gamePane.getChildren().remove(getSceneNodes());
        spriteStore.removeAllSprites();

        inProgress = false;
    }

    /**
     * Initialise the game world by update the JavaFX Stage.
     * @param gamePane The primary scene.
     */
    @Override
    public void initialise(Pane gamePane, Label scoreLabel) {
        this.gamePane = gamePane;
        this.scoreLabel = scoreLabel;

        // set initial score
        scoreLabel.setText(Integer.toString(score));

        // draw and stretch the board background
        gamePane.setStyle("-fx-background-image: url('/board.png'); -fx-background-size: cover;");
    }
    
    protected void updateBoard() {
        if(inProgress){
        	board.update();
        }
    }
    
    @Override
    public void boardOutOfMoves() {
    	// TODO: Show a text like "Game over"
    	stop();
    }

    @Override
    public void boardJewelRemoved() {
    	score += 10; // add 10 points per jewel removed
        scoreLabel.setText(Integer.toString(score));
    }

    public boolean isInProgress() {
        return inProgress;
    }
}
