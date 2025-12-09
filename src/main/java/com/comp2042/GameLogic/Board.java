package com.comp2042.GameLogic;

import com.comp2042.GameInfo.Score;
import com.comp2042.GameInfo.ViewData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    int ToBottom();


    void newGame();
}
