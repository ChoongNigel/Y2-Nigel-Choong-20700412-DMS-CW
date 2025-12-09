package com.comp2042.GameInfo;

import com.comp2042.GameLogic.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int[][] nextBrickData;
    private final int[][] ghostBrickData;
    private final int ghostX;
    private final int ghostY;
    private final int xPosition;
    private final int yPosition;
    private final int[][] heldBrickData;
    private final boolean canHold;

    /**
     * return brick's data
     * @param brickData
     * @param xPosition
     * @param yPosition
     * @param nextBrickData
     * @param ghostBrickData
     * @param ghostX
     * @param ghostY
     * @param canHold
     * @param heldBrickData
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] ghostBrickData, int ghostX, int ghostY, boolean canHold, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostBrickData = ghostBrickData;
        this.ghostX = ghostX;
        this.ghostY = ghostY;
        this.canHold = canHold;
        this.heldBrickData = heldBrickData;
    }

    public boolean canHold(){
        return canHold;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    public int[][] getGhostBrickData() {
        return MatrixOperations.copy(ghostBrickData);
    }
    public int[][] getHeldBrickData() {
        if (heldBrickData == null) {
            return null;
        }
        return MatrixOperations.copy(heldBrickData);
    }

    public int getGhostX() {
        return ghostX;
    }
    public int getGhostY() {
        return ghostY;
    }
}
