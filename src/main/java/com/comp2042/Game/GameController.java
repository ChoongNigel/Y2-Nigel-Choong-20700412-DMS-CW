package com.comp2042.Game;

import com.comp2042.GameLogic.*;
import com.comp2042.GameInfo.ViewData;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class GameController implements InputEventListener {

    private final SimpleBoard board;

    private final GuiController viewGuiController;

    public static int brickSpeed;
    public static IntegerProperty level= new SimpleIntegerProperty(1);

    private int[][] heldBrickData=null;
    private boolean canHold=true;

    /**
     * Controller that help GuiController class to visualize the bricks and board.
     *
     * @param c Guicontroller, take the key's, gamecycle
     */

    public GameController(GuiController c) {
        this.viewGuiController = c;
        this.board= new SimpleBoard(10,25);

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.setBoard(board);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScoreAndLevel(board.getScore().scoreProperty(), level);
    }

    public static void setLevel(int value){
        level.set(value);
    }

    public static int getLevel() {
        return level.get();
    }

    /**
     * Brick fall down event, whether is harddrop or nature fall.
     * @param event
     * @return DownData
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        ClearRow clearRow = null;

        if (event.getEventType() == EventType.HARD_DROP) {
            int droppedCells = board.ToBottom();
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }

            board.getScore().add(droppedCells * 2);

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix(), board.getViewData());

            return new DownData(clearRow, null);
        }

        // Normal down movement
        boolean canMove = board.moveBrickDown();
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix(),board.getViewData());
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * move brick to left
     * @param event
     * @return
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Move brick to right
     * @param event
     * @return
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Rotate brick
     * @param event
     * @return
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Hold Brick
     * @param event
     * @return
     */
    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        ViewData viewData = board.holdCurrentBrick();
        viewGuiController.refreshGameBackground(board.getBoardMatrix(), viewData);
        return viewData;
    }

    /**
     * create new game
     */
    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix(),board.getViewData());
    }


    public Board getBoard() {
        return board;
    }
}
