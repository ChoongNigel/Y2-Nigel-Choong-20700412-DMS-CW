package com.comp2042.GameLogic;

import com.comp2042.GameInfo.ViewData;
import com.comp2042.GameInfo.NextShapeInfo;
import com.comp2042.GameInfo.Score;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private boolean canHold = true;
    private Brick heldBrick = null;

    /**
     * Define new game board
     * @param width
     * @param height
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;

        currentGameMatrix = new int[height][width];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Compute where the ghost block should landed
     * @return
     */
    private int computeGhostRow() {
        int x = (int)currentOffset.getX();
        int y = (int)currentOffset.getY();
        int[][] shape=brickRotator.getCurrentShape();
        int[][] matrix = MatrixOperations.copy(currentGameMatrix);

        while (!MatrixOperations.intersect(matrix, shape, x, y + 1)) {
            y++;
        }
        return y;
    }

    public ViewData holdCurrentBrick() {
        if (!canHold) {
            return getViewData();
        }

        Brick currentBrick = brickRotator.getBrick();

        if (heldBrick == null) {
            heldBrick = currentBrick;
            Brick newBrick = brickGenerator.getBrick();
            spawnNewBrick(newBrick);
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            spawnNewBrick(temp);
        }

        canHold = false;
        return getViewData();
    }

    /**
     * Move brick down naturally
     * @return
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Check whether a brick is at bottom
     * @return
     */
    @Override
    public int ToBottom(){
        int dropped=0;
        while(moveBrickDown()){
            dropped++;
        }
        return dropped;
    }

    /**
     * Move brick left
     * @return
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Move brick right
     * @return
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * rotate the brick
     * @return
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /**
     * Declare where the new brick should be spawned
     * @param brick
     * @return
     */
    private boolean spawnNewBrick(Brick brick) {
        brickRotator.setBrick(brick);
        currentOffset = new Point(4, 0);

        return MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    /**
     * Generate new brick
     * @return
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        // Allow hold again for this new piece
        canHold = true;
        return spawnNewBrick(currentBrick);
    }

    /**
     * return board matrix
     * @return
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * View the current brick data
     * @return
     */
    @Override
    public ViewData getViewData() {
        int[][] shape = brickRotator.getCurrentShape();
        int x = (int) currentOffset.getX();
        int y = (int) currentOffset.getY();

        int[][] next = brickGenerator
                .getNextBrick()
                .getShapeMatrix()
                .get(0);

        int ghostY = computeGhostRow();
        int ghostX = x;
        int[][] ghostShape = shape;

        int[][] heldData = null;
        if (heldBrick != null) {
            heldData = heldBrick.getShapeMatrix().get(0);
        }

        return new ViewData(
                shape,
                x,
                y,
                next,
                ghostShape,
                ghostX,
                ghostY,
                this.canHold,
                heldData
        );
    }

    /**
     * Merge dropped to bottom brick to background
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Clear the row if row is completed
     * @return
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    /**
     * Return current score
     * @return
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Create new game
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}
