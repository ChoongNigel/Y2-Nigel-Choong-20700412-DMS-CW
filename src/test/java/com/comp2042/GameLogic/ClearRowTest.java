package com.comp2042.GameLogic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {

    int[][] matrix={
            {1,0},
            {0,1}
    };

    @Test
    void getLinesRemoved() {
        ClearRow clearRow=new ClearRow(2, matrix,300);

        assertEquals(2, clearRow.getLinesRemoved(),"Only 2 lines should be removed");
        assertEquals(300,clearRow.getScoreBonus(), "Row cleared should get 300 score");

        int[][] copiedMatrix=clearRow.getNewMatrix();
        assertArrayEquals(matrix,copiedMatrix,"Matrix should be copied equally");
    }

    @Test
    void getNewMatrix_CopyNotOri() {
        ClearRow clearRow=new ClearRow(1, matrix,300);
        int[][] firstCopiedMatrix=clearRow.getNewMatrix();
        int[][] secondCopiedMatrix=clearRow.getNewMatrix();

        assertNotSame(firstCopiedMatrix,secondCopiedMatrix,"Matrix should not be the same, it's a copy");

        firstCopiedMatrix[0][0]=9;
        int[][] thirdCopiedMatrix=clearRow.getNewMatrix();
        assertEquals(1, thirdCopiedMatrix[0][0],"copied matrix should not affect original");
    }
}