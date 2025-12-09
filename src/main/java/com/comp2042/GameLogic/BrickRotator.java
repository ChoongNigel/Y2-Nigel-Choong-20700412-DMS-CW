package com.comp2042.GameLogic;

import com.comp2042.GameInfo.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * get the rotated shape of current brick
     * @return
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * get current brick shape
     * @return
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Set current brick shape
     * @param currentShape
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * get specific brick matrix
     * @return
     */
    public Brick getBrick() {
        return brick;
    }

    /**
     * Set a specific brick
     * @param brick
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
