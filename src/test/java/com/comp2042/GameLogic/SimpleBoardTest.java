package com.comp2042.GameLogic;

import com.comp2042.GameInfo.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {
    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board=new SimpleBoard(10,25);
    }

    @Test
    void newGameResetScore(){
        board.getScore().add(100);

        board.newGame();

        assertEquals(0,board.getScore().scoreProperty().getValue(),"Should reset to 0");
    }

    @Test
    void brickStopMoveDownAtBottom(){
        Board board=new SimpleBoard(10,25);
        board.createNewBrick();

        boolean canMove;
        do{
            canMove=board.moveBrickDown();
        }while (canMove);

        assertFalse(board.moveBrickDown());
    }

    @Test
    void createNewBrick(){
        boolean collide=board.createNewBrick();
        ViewData view=board.getViewData();

        assertFalse(collide,"Brick created shouldn't collide");
        assertNotNull(view, "Created new brick");

    }

    @Test
    void stopMoveRight(){
        board.createNewBrick();

        boolean canMove;
        do{
            canMove=board.moveBrickRight();
        }while(canMove);

        assertFalse(board.moveBrickRight(),"Can't move right at edge");
    }

    @Test
    void holdCurrentBrickTest() {

        board.createNewBrick();

        ViewData first = board.holdCurrentBrick();
        int[][] held1 = first.getHeldBrickData();
        assertNotNull(held1, "After hold, heldBrickData should not be null");


        ViewData second = board.holdCurrentBrick();
        int[][] held2 = second.getHeldBrickData();
        assertArrayEquals(held1, held2,
                "After first hold, can't hold again to avoid bug");
    }

}