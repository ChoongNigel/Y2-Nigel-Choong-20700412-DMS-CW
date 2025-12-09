package com.comp2042.Game;

import com.comp2042.GameInfo.UsersFunction;
import com.comp2042.GameLogic.*;
import com.comp2042.GameInfo.ViewData;
import com.comp2042.SceneController.BGControl.Background;
import com.comp2042.SceneController.BGControl.BackgroundMusic;
import com.comp2042.SceneController.BGControl.SceneChanger;
import com.comp2042.SceneController.BGControl.SoundEffect;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable, GameUIControl {

    private static final int BRICK_SIZE = 20    ;
    private static final int HIDDEN_ROW=3;
    public BorderPane gameBoard;
    private SimpleBoard board;
    private BackgroundMusic bgmGame;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane holdBrickPanel;

    @FXML
    private Pane GameOverPanel;

    @FXML
    private MediaView gamerunningbg;

    @FXML
    private Text ScoreValues;

    @FXML
    private Text ScoreValues2;

    @FXML
    private GridPane NextBrickPanel;

    @FXML
    private Button Restart;

    @FXML
    private Button Restart2;

    @FXML
    private Button Resume;

    @FXML
    private Button gameToHome;

    @FXML
    private Button gameToHome2;

    @FXML
    private Pane PausePanel;

    @FXML
    private Text levelNumber;

    @FXML
    private VBox levelPane;

    @FXML
    private Text levelText;

    @FXML
    private StackPane levelUpPanel;

    @FXML
    private Text userShow;

    @FXML
    private Text userHighscore;

    private Rectangle[][] displayMatrix;

    private GameController gameController;
    private static InputEventListener eventListener;

    private Rectangle[][] rectangles;
    private Rectangle[][] heldBrick;

    private Timeline timeLine;
    private SoundEffect moveEffect;
    private SoundEffect slam;
    private SoundEffect rotate;
    private SoundEffect scored;
    private SoundEffect levelswitch;



    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /**
     * Initialize the base of display. Including buttons, BGM, Background etc.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser();
        Background.ApplyGif(gamerunningbg,"InGameBackground.mp4");
        moveEffect=new SoundEffect("left-rightclip",0.2);
        slam=new SoundEffect("slam",0.3);
        rotate=new SoundEffect("flip", 0.3);
        scored=new SoundEffect("Scored",0.3);
        levelswitch=new SoundEffect("levelswitch",0.3);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::KeyControl);
        GameOverPanel.setVisible(false);
        PausePanel.setVisible(false);
        updateLevelPaneStyle(1);

        Restart.setOnAction(event -> {
            newGame();
            GameOverPanel.setVisible(false);

        });
        Restart2.setOnAction(event -> {
            newGame();
            PausePanel.setVisible(false);
        });

        gameToHome.setOnAction(event -> {
            try {
                SceneChanger.scenechange("MainMenu", "MainMenuBGM");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        gameToHome2.setOnAction(event -> {
            try {
                SceneChanger.scenechange("MainMenu", "MainMenuBGM");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Resume.setOnAction(event -> {
            pauseGame();
        });
    }

    /**
     * Helper function to determine key received, and respective action to take
     * @param keyEvent
     */
    @Override
    public void KeyControl(KeyEvent keyEvent){
        if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
            switch (keyEvent.getCode()) {
                case KeyCode.LEFT, KeyCode.A:
                    ViewData v= eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
                    refreshGameBackground(board.getBoardMatrix(),v);
                    refreshBrick(v);
                    refreshNextBrick(v);
                    keyEvent.consume();
                    moveEffect.play();
                    break;
                case KeyCode.RIGHT, KeyCode.D:
                    ViewData v2=eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
                    refreshGameBackground(board.getBoardMatrix(),v2);
                    refreshBrick(v2);
                    refreshNextBrick(v2);
                    keyEvent.consume();
                    moveEffect.play();
                    break;
                case KeyCode.UP, KeyCode.W:
                    rotate.play();
                    ViewData v3=eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
                    refreshGameBackground(board.getBoardMatrix(),v3);
                    refreshBrick(v3);
                    refreshNextBrick(v3);
                    keyEvent.consume();
                    break;
                case KeyCode.DOWN, KeyCode.S:
                    moveEffect.play();
                    moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();
                    break;
                case KeyCode.TAB, KeyCode.SPACE:
                    hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                    shakePane(gamePanel);
                    slam.play();
                    keyEvent.consume();
                    break;
                case KeyCode.C, KeyCode.SHIFT:
                    ViewData v4 = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
                    refreshGameBackground(board.getBoardMatrix(), v4);
                    refreshBrick(v4);
                    refreshNextBrick(v4);
                    refreshHoldBrick(v4);
                    keyEvent.consume();
                    break;
            }
        }
        if (keyEvent.getCode() == KeyCode.N) {
            newGame();
        }
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            pauseGame();
        }
    }

    /**
     * Allign the brick panel with game panel, with customized parameter
     * @param brick
     */
    private void BrickPanelLayout(ViewData brick){
        brickPanel.setLayoutX(-2+gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-3 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + (brick.getyPosition()-HIDDEN_ROW) * BRICK_SIZE);
    }

    /**
     * The actual game part, create game panel, hold brick panel, next brick panel, and brick panel
     * @param boardMatrix
     * @param brick
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = HIDDEN_ROW; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i-HIDDEN_ROW );
            }
        }
        refreshGameBackground(boardMatrix, brick);

        BrickPanelLayout(brick);
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                int boardRow = brick.getyPosition() + i;
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
                if (boardRow < HIDDEN_ROW) {
                    rectangles[i][j].setFill(Color.TRANSPARENT);
                } else {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }

            }
        }

        initHoldBrickPanel(brick);
        initNextBrickPanel(brick);

        levelIncrease(board.getScore().scoreProperty().get());
        restartTimelineWithNewSpeed();
    }

    /**
     * As level increased, brick speed increase and apply new bgm
     * @param score
     */
    public void levelIncrease(int score){
        int oldLevel = GameController.getLevel();  // current level
        int newLevel = oldLevel;

        if (score < 2000 && score >= 0) {
            newLevel = 1;
            GameController.brickSpeed = 450;
        } else if (score > 2000 && score <= 5000) {
            newLevel = 2;
            GameController.brickSpeed = 300;
        } else if (score > 5000) {
            newLevel = 3;
            GameController.brickSpeed = 200;
        }


        if (newLevel != oldLevel) {
            GameController.setLevel(newLevel);

            if (newLevel == 1) {
                SceneChanger.levelIncreaseBgmChange("TetrisGameBGM");
                updateLevelPaneStyle(newLevel);
            } else if (newLevel == 2) {
                SceneChanger.levelIncreaseBgmChange("TetrisGameBGM2");
                levelswitch.play();
                updateLevelPaneStyle(newLevel);
                showLevelUpAnnouncement();
                shakePane(gamePanel);
            } else if (newLevel == 3) {
                SceneChanger.levelIncreaseBgmChange("TetrisGameBGM3");
                levelswitch.play();
                updateLevelPaneStyle(newLevel);
                showLevelUpAnnouncement();
                shakePane(gamePanel);
            }
        }
    }

    private Rectangle[][] nextBrickRectangle;

    /**
     * Create Next Brick Panel
     * @param brick
     */
    private void initNextBrickPanel(ViewData brick) {
        int[][] nextData=brick.getNextBrickData();
        NextBrickPanel.setHgap(0);
        NextBrickPanel.setVgap(0);
        NextBrickPanel.getChildren().clear();
        nextBrickRectangle =new Rectangle[nextData.length][nextData[0].length];

        for (int i=0;i<nextData.length;i++) {
            for (int j=0;j<nextData[i].length;j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(nextData[i][j]));
                rectangle.setArcWidth(6);
                rectangle.setArcHeight(6);
                nextBrickRectangle[i][j] = rectangle;
                NextBrickPanel.add(rectangle, j, i);
            }
        }
    }

    /**
     * Create Hold Brick panel
     * @param brick
     */
    private void initHoldBrickPanel(ViewData brick) {
        int[][] held = brick.getHeldBrickData();
        holdBrickPanel.setHgap(0);
        holdBrickPanel.setVgap(0);

        if (held == null) {
            return;
        }

        heldBrick = new Rectangle[held.length][held[0].length];

        for (int i = 0; i < held.length; i++) {
            for (int j = 0; j < held[i].length; j++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(held[i][j], r);
                r.setArcWidth(6);
                r.setArcHeight(6);
                heldBrick[i][j] = r;
                holdBrickPanel.add(r, j, i);
            }
        }
    }

    /**
     * Refresh next brick after the current brick is placed
     * @param brick
     */
    private void refreshNextBrick(ViewData brick) {
        int[][] next = brick.getNextBrickData();
        if (next == null || nextBrickRectangle == null) return;


        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next[0].length; j++) {
                setRectangleData(next[i][j], nextBrickRectangle[i][j]);
            }
        }
    }

    /**
     * Refresh held brick
     * @param brick
     */
    private void refreshHoldBrick(ViewData brick) {
        int[][] held = brick.getHeldBrickData();
        holdBrickPanel.getChildren().clear();

        if (held == null) {
            heldBrick = null;
            return;
        }

        heldBrick = new Rectangle[held.length][held[0].length];

        for (int i = 0; i < held.length; i++) {
            for (int j = 0; j < held[i].length; j++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(held[i][j], r);
                r.setArcWidth(6);
                r.setArcHeight(6);
                heldBrick[i][j] = r;
                holdBrickPanel.add(r, j, i);
            }
        }
    }

    /**
     * Refresh the falling brick when the current one is placed
     * @param brick
     */
    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            BrickPanelLayout(brick);

            int[][] data = brick.getBrickData();

            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    int boardRow = brick.getyPosition() + i;

                    if (boardRow < HIDDEN_ROW) {
                        rectangles[i][j].setFill(Color.TRANSPARENT);
                    } else {
                        setRectangleData(data[i][j], rectangles[i][j]);
                    }
                }
            }
        }
    }

    /**
     * If the brick is placed, generate the exact same placed brick and merge it with the rows
     * Also show the ghost brick of current brick
     * @param board
     * @param brick
     */
    public void refreshGameBackground(int[][] board, ViewData brick) {
        for (int i = HIDDEN_ROW; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }

        if (brick != null) {
            drawGhost(brick);
        }
    }

    /**
     * Set up board for GameController class
     * @param board
     */
    public void setBoard(SimpleBoard board) {
        this.board=board;

        board.getScore().scoreProperty().addListener((obs, oldVal, newVal) -> {
            int score = newVal.intValue();
            levelIncrease(score);             // update level + brickSpeed
            restartTimelineWithNewSpeed();    // apply new speed
        });
    }

    /**
     * When level increased, run this function to increase the brick speed
     */
    private void restartTimelineWithNewSpeed() {
        if (timeLine != null) {
            timeLine.stop();
        }
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(GameController.brickSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        if (!isPause.get()) {
            timeLine.play();
        }
    }

    /**
     * When brick moves down, check whether clear the row or not. If yes, get score bonus, if not, refresh new brick
     * @param event
     */
    private void moveDown(MoveEvent event) {
        ClearRowWithScore(event);
    }

    /**
     * When brick Hard Drop to bottom, check if row is cleared and give score bonus if yes.
     * @param event
     */
    private void hardDrop(MoveEvent event){
        ClearRowWithScore(event);
    }

    /**
     * Helper function to paint the bricks
     * @param i
     * @return
     */
    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }

    /**
     * Helper function to paint the ghost brick with same colour of it's brick, just lower opacity
     * @param i
     * @return
     */
    private Paint getGhostFillColor(int i) {
        Color base = (Color) getFillColor(i);
        return new Color(base.getRed(),base.getGreen(),base.getBlue(),0.5);
    }

    /**
     * Resize the rectangle and fill up rectangle colour
     * @param color
     * @param rectangle
     */
    private void setRectangleData(int color, Rectangle rectangle) {
        if (rectangle==null) return;
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    /**
     * Helper function that multiple called to clear row and player gain score
     * @param event
     */
    private void ClearRowWithScore(MoveEvent event) {//Make this an individual Function for tab/down event
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                scored.play();
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            if (downData.getViewData()!=null) {
                refreshBrick(downData.getViewData());
                refreshNextBrick(downData.getViewData());
            }
        }
        gamePanel.requestFocus();
    }

    /**
     * Calculate where ghost block should be and fill up ghost block
     * @param brick
     */
    private void drawGhost(ViewData brick) {
        int[][] ghostData = brick.getGhostBrickData();
        if (ghostData == null) return;

        int gx = brick.getGhostX();
        int gy = brick.getGhostY();

        for (int i = 0; i < ghostData.length; i++) {
            for (int j = 0; j < ghostData[i].length; j++) {
                if (ghostData[i][j] != 0) {
                    int boardRow = gy + i;
                    int boardCol = gx + j;

                    if (boardRow < HIDDEN_ROW || boardRow >= displayMatrix.length) continue;
                    if (boardCol < 0 || boardCol >= displayMatrix[0].length) continue;

                    Rectangle r = displayMatrix[boardRow][boardCol];
                    if (r != null) {
                        r.setFill(getGhostFillColor(ghostData[i][j]));
                        r.setArcHeight(9);
                        r.setArcWidth(9);
                    }
                }
            }
        }
    }

    /**
     * When HardDrop, shake the pane
     * @param node
     */
    private void shakePane(Node node) {
        double originalX = node.getLayoutX();
        double originalY = node.getLayoutY();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),   new KeyValue(node.translateXProperty(), 0),  new KeyValue(node.translateYProperty(), 0)),
                new KeyFrame(Duration.millis(25),  new KeyValue(node.translateXProperty(), -2)),
                new KeyFrame(Duration.millis(50), new KeyValue(node.translateXProperty(), 2)),
                new KeyFrame(Duration.millis(75), new KeyValue(node.translateXProperty(), -2)),
                new KeyFrame(Duration.millis(100), new KeyValue(node.translateXProperty(), 2)),
                new KeyFrame(Duration.millis(150), new KeyValue(node.translateXProperty(), 0),  new KeyValue(node.translateYProperty(), 0))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * When Level increased, Level Up pane appear
     */
    private void showLevelUpAnnouncement() {
        if (levelUpPanel == null) return;

        // Ensure it's visible and on top
        levelUpPanel.setVisible(true);

        // Get scene height to compute positions
        double sceneHeight = levelUpPanel.getScene().getHeight();
        double panelHeight = levelUpPanel.getBoundsInParent().getHeight();

        // Start: below the bottom, fully transparent
        levelUpPanel.setTranslateY(sceneHeight / 2 + panelHeight); // off-screen down
        levelUpPanel.setOpacity(0.0);

        // Define keyframes:
        Timeline timeline = new Timeline(
                // 1) Fade in + move up to center over 300 ms
                new KeyFrame(Duration.millis(0),
                        new KeyValue(levelUpPanel.translateYProperty(), sceneHeight / 2 + panelHeight),
                        new KeyValue(levelUpPanel.opacityProperty(), 0.0)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(levelUpPanel.translateYProperty(), 0),  // center (assuming centered in layout)
                        new KeyValue(levelUpPanel.opacityProperty(), 1.0)
                ),

                // 2) Stay in place & fully visible until 1000 ms
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(levelUpPanel.translateYProperty(), 0),
                        new KeyValue(levelUpPanel.opacityProperty(), 1.0)
                ),

                // 3) Fade out + move up off-screen by 1600 ms
                new KeyFrame(Duration.millis(1600),
                        new KeyValue(levelUpPanel.translateYProperty(), -sceneHeight / 2 - panelHeight),
                        new KeyValue(levelUpPanel.opacityProperty(), 0.0)
                )
        );

        timeline.setCycleCount(1);
        timeline.setOnFinished(e -> levelUpPanel.setVisible(false)); // hide after animation
        timeline.play();
    }

    /**
     * Style the levelpane, respective to different level
     * @param newLevel
     */
    private void updateLevelPaneStyle(int newLevel) {
        if (levelPane == null) return;

        levelPane.getStyleClass().removeAll("level-1", "level-2", "level-3");
        levelPane.getStyleClass().add("level-" + newLevel);
        levelText.getStyleClass().removeAll("level-1", "level-2", "level-3");
        levelText.getStyleClass().add("level-" + newLevel);
    }

    /**
     * Helper function to set new bgm for new level
     * @param bgm
     */
    public void setBgmGame(BackgroundMusic bgm) {
        this.bgmGame = bgm;
    }

    /**
     * Recognize current event
     * @param eventListener
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Setter function to set gamecontroller in SceneChanger
     * @param gc
     */
    public void setGameController(GameController gc) {
        this.gameController = gc;
    }

    /**
     * Update logged in user on the user display pane
     */
    public void currentUser(){
        if (UsersFunction.currentUser != null) {
            userShow.setText(UsersFunction.currentUser);
            userHighscore.setText(UsersFunction.UserHighScore(UsersFunction.currentUser));
        } else {
            // optionally handle guest / no user
            userShow.setText("Guest");
            userHighscore.setText("0");
        }
    }

    /**
     * Bind the scores and level with respective pane
     * @param integerProperty
     * @param levelProperty
     */
    @Override
    public void bindScoreAndLevel(IntegerProperty integerProperty, IntegerProperty levelProperty) {
        ScoreValues.textProperty().bind(integerProperty.asString("%,d"));
        ScoreValues2.textProperty().bind(integerProperty.asString("%,d"));
        levelNumber.textProperty().bind(levelProperty.asString("%,d"));
    }

    /**
     * When Game Over, reset everything and show the game over panel
     */
    @Override
    public void gameOver() {
        timeLine.stop();
        UsersFunction.updateScores(UsersFunction.currentUser,board.getScore().scoreProperty().getValue());
        GameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        bgmGame.pause();
    }

    /**
     * When New game, reset everything
     */
    @Override
    public void newGame() {
        currentUser();

        timeLine.stop();
        GameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        bgmGame.play();
        System.out.println(bgmGame);
    }

    /**
     * When game is paused, pause everything
     */
    @Override
    public void pauseGame() {
        if (timeLine==null){
            return;
        }
        if(isPause.getValue() == Boolean.FALSE) {
            isPause.setValue(Boolean.TRUE);
            if (timeLine!=null){
                timeLine.pause();
                PausePanel.setVisible(true);
            }
        }
        else{
            isPause.setValue(Boolean.FALSE);
            timeLine.play();
            PausePanel.setVisible(false);
        }
        gamePanel.requestFocus();

    }
}
