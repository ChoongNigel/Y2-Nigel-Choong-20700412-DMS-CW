package com.comp2042.GameInfo;

import com.comp2042.GameLogic.MatrixOperations;

public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Update upcoming shape (Rotation)
     * @param shape
     * @param position
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * get the rotated shape
     * @return
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * get rotated shape position
     * @return
     */
    public int getPosition() {
        return position;
    }
}
