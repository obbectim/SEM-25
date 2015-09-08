package nl.tudelft.bejeweled.game;

import javafx.scene.Group;
import javafx.scene.Scene;
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

    private SpriteStore spriteStore;

    private Pane gamePane;

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

        System.out.println("Game started");

        // Create the group that holds all the jewel nodes and create a game scene
        setSceneNodes(new Group());
        gamePane.getChildren().add(new Scene(getSceneNodes(),
                gamePane.getWidth(),
                gamePane.getHeight()).getRoot());

        // generate the jewels
        board = boardFactory.generateBoard(getSceneNodes(), gamePane.getWidth(), gamePane.getHeight());

        // check for any combo's on the freshly created board
        int comboCount = board.checkBoardCombos();
        System.out.println("Combo Jewels on board: " + comboCount);
        board.generateJewels();

        inProgress = true;
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
    public void initialise(Pane gamePane) {
        this.gamePane = gamePane;

        // draw and stretch the board background
        gamePane.setStyle("-fx-background-image: url('/board.png'); -fx-background-size: cover;");

        // draw the board as a background on the pane
        /*
        Image boardImage = new Image(Game.class.getResourceAsStream("/board.png"));
        ImageView boardImageView = new ImageView();
        boardImageView.setImage(boardImage);
        gamePane.getChildren().add(boardImageView);

        Canvas canvas = new Canvas(560, 564);
        gamePane.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image boardImage = new Image("/board.png");
        gc.drawImage(boardImage, 0, 0);
        */
    }

    /**
     * Update the game and let board generate new Jewels.
     * The board handles removing the Jewels by handling mouse click events.
     */
    @Override
    public void updateGame() {
        board.updateBoard();
    }

    @Override
    public void boardOutOfMoves() {
    	stop();
    }

    @Override
    public void boardJewelsRemoved(int count) {
    	// TODO: count the points based on the amount of jewels cleared
    }
}
