package com.comp2042.Game;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;

public interface GameUIControl {
    void KeyControl(KeyEvent e);
    void newGame();
    void pauseGame();
    void gameOver();
    void bindScoreAndLevel(IntegerProperty integerProperty,  IntegerProperty levelProperty);

}
