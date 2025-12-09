package com.comp2042.Game;

import com.comp2042.GameInfo.ViewData;
import com.comp2042.GameLogic.InputEventListener;
import com.comp2042.GameLogic.SimpleBoard;
import javafx.beans.property.IntegerProperty;

public class GUIforTest extends GuiController {
    public boolean gameOverCalled = false;
    public boolean refreshCalled = false;

    @Override
    public void setEventListener(InputEventListener listener) {

    }

    @Override
    public void setBoard(SimpleBoard board) {
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
    }

    @Override
    public void bindScoreAndLevel(IntegerProperty integerProperty,
                                  IntegerProperty levelProperty) {
    }

    @Override
    public void refreshGameBackground(int[][] board, ViewData brick) {
        refreshCalled = true;
    }

    @Override
    public void gameOver() {
        gameOverCalled = true;
    }
}
