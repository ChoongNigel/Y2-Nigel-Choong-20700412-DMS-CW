package com.comp2042.logic.bricks;

import com.comp2042.GameLogic.MatrixOperations;

import java.util.List;
import java.util.stream.Collectors;

//Change from interface to abstract class
// Bring deepCopyList to .logic.bricks as the function is only used in this folder
public abstract class Brick {
    /**
     * Copy brick size from template
     * @param list
     * @return
     */
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream()
                .map(MatrixOperations::copy)
                .collect(Collectors.toList());
    }

    /**
     * Helper function to get brick shape
     * @return
     */
    public abstract List<int[][]> getShapeMatrix();
}
