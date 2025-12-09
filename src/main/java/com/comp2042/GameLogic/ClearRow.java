package com.comp2042.GameLogic;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Clear the row with respective parameter given
     * @param linesRemoved
     * @param newMatrix
     * @param scoreBonus
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Remove lines
     * @return
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Get after line removed matrix
     * @return
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * return score bonus base on lines removed
     * @return
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
