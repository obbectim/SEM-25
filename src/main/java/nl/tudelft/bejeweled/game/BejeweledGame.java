package nl.tudelft.bejeweled.game;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import nl.tudelft.bejeweled.board.Board;
import nl.tudelft.bejeweled.board.BoardFactory;
import nl.tudelft.bejeweled.board.BoardObserver;
import nl.tudelft.bejeweled.sprite.Jewel;
import nl.tudelft.bejeweled.sprite.SpriteStore;

import java.util.Iterator;


/**
 * Created by Jeroen on 6-9-2015.
 * Bejeweled Game class.
 */
public class BejeweledGame extends Game implements BoardObserver {
	public static final int GRID_WIDTH = 8;
	public static final int GRID_HEIGHT = 8;
	public static final int SPRITE_WIDTH = 64;
	public static final int SPRITE_HEIGHT = 64;

    /** The board class that maintains the jewels. */
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

    /** The constructor for the bejeweled game. 
    * @param framesPerSecond - The number of frames per second the game will attempt to render.
    * @param windowTitle - The title displayed in the window.
    */
    public BejeweledGame(int framesPerSecond, String windowTitle, SpriteStore spriteStore) {
        super(framesPerSecond, windowTitle);
        this.spriteStore = spriteStore;
        boardFactory = new BoardFactory(spriteStore);
    }

    /**
     *  Starts the game.
     */
    @Override
    public void start() {
        if (inProgress) {
            return;
        }
        inProgress = true;
        
        System.out.println("Game started");

        score = 0;

        // fill board if game was stopped and started again
        board.fillNullSpots();

        // Create the group that holds all the jewel nodes and create a game scene
        gamePane.getChildren().add(new Scene(getSceneNodes(),
                gamePane.getWidth(),
                gamePane.getHeight()).getRoot());

        // check for any combo's on the freshly created board
        int comboCount = board.checkBoardCombos();
        System.out.println("Combo Jewels on board: " + comboCount);
    }

    /**
     *  Stops the game.
     */
    @Override
    public void stop() {
        if (!inProgress) {
            return;
        }

        System.out.println("Game stopped");

        // remove the JavaFX group with jewel nodes
    //    getSceneNodes().getChildren().removeAll();
        gamePane.getChildren().remove(getSceneNodes());
        spriteStore.removeAllSprites();
        board.resetGrid();

        inProgress = false;
    }

    /**
     * Initialise the game world by update the JavaFX Stage.
     * @param gamePane The primary scene.
     */
    @Override
    public void initialise(Board board, Pane gamePane, Label scoreLabel) {
        this.gamePane = gamePane;
        this.scoreLabel = scoreLabel;
        this.board = board;

        // set initial score
        scoreLabel.setText(Integer.toString(score));

        // draw and stretch the board background
        gamePane.setStyle("-fx-background-image: url('/board.png'); -fx-background-size: cover;");

        // start observing the board for callback events
        board.addObserver(this);

        // set callback for clicking on the board
        gamePane.addEventFilter(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        if (isInProgress() && event.getTarget() instanceof Pane) {
                            board.boardClicked(true);
                        }
                    }
                }
        );
    }
    
    /**
     * Updates all game logic, At this point this consists of updating 
     * the board that is loaded for the game if the game is in progress.
     */
    protected void updateLogic() {
        if (inProgress) {
        	board.update();
        }
    }
    
    @Override
    public void boardOutOfMoves() {

        final Label label = new Label("Game Over");
        label.setFont(new Font("Arial", 55));

        // position the label
        //TODO Position this label nicely in the center
        label.setLayoutX(100);
        label.setLayoutY(200);
        gamePane.getChildren().add(label);

        FadeTransition ft = new FadeTransition(Duration.millis(3000), label);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                gamePane.getChildren().remove(label);
            }
        });
        ft.play();

    	stop();
    }

    @Override
    public void boardJewelRemoved() {
    	final int point = 10;
    	score += point; // add 10 points per jewel removed
        scoreLabel.setText(Integer.toString(score));
    }
    
    /**
     *Check if the game is still in progress.
     * @return true if the game is in progress.
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Getter method for the board.
     * @return The board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Getter method for the score.
     * @return The current score
     */
    public int getScore() {
        return score;
    }
}
