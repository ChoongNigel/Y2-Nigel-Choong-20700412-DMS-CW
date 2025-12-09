package com.comp2042.GameInfo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Return integer property of score, to be display on score panel
     * @return
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Score Addition
     * @param i
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Score reset
     */
    public void reset() {
        score.setValue(0);
    }
}
