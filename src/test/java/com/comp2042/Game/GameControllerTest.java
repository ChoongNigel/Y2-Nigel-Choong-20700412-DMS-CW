package com.comp2042.Game;

import com.comp2042.GameLogic.DownData;
import com.comp2042.GameInfo.ViewData;
import com.comp2042.GameLogic.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void userDownAddsScoreOrLocks() {
        GUIforTest fakeGui = new GUIforTest();
        GameController gc = new GameController(fakeGui);

        int before = gc.getBoard().getScore().scoreProperty().get();

        DownData data = gc.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        int after = gc.getBoard().getScore().scoreProperty().get();

        assertTrue(after == before || after == before + 1,
                "After down button pressed, score must + 1; or naturally dropped score no change");

        assertNotNull(data, "DownData should never be null");
    }

    @Test
    void hardDropIncreasesScore() {
        GUIforTest fakeGui = new GUIforTest();
        GameController gc = new GameController(fakeGui);

        int before = gc.getBoard().getScore().scoreProperty().get();

        DownData data = gc.onDownEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));

        int after = gc.getBoard().getScore().scoreProperty().get();

        assertTrue(after > before, "Hard drop should increase score");
        assertNotNull(data, "DownData should not be null for hard drop");
    }

    @Test
    void onLeftEvent_movesBrickLeft() {
        GUIforTest fakeGui = new GUIforTest();
        GameController gc = new GameController(fakeGui);

        ViewData before = gc.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        int xBefore = before.getxPosition();

        ViewData after = gc.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        int xAfter = after.getxPosition();

        assertTrue(xAfter <= xBefore, "x position should not increase when moving left");
    }

    @Test
    void movesBrickRight() {
        GUIforTest fakeGui = new GUIforTest();
        GameController gc = new GameController(fakeGui);

        ViewData before = gc.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        int xBefore = before.getxPosition();

        ViewData after = gc.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        int xAfter = after.getxPosition();

        assertTrue(xAfter >= xBefore, "x position should not decrease when moving right, considering on edge as well");
    }

    @Test
    void returnsRotatedData() {
        GUIforTest fakeGui = new GUIforTest();
        GameController gc = new GameController(fakeGui);

        ViewData data = gc.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        assertNotNull(data, "Rotate should return non-null Data");
    }

    @Test
    void returnHoldData() {
        GUIforTest fakeGui = new GUIforTest();
        GameController gc = new GameController(fakeGui);

        ViewData data = gc.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
        assertNotNull(data, "Hold should return non-null Data");
    }

    @Test
    void createNewGame_resetsScoreAndBoard() {
        GUIforTest fakeGui = new GUIforTest();
        GameController gc = new GameController(fakeGui);

        // Add some score and then reset
        gc.getBoard().getScore().add(100);
        assertTrue(gc.getBoard().getScore().scoreProperty().get() > 0);

        gc.createNewGame();

        assertEquals(0, gc.getBoard().getScore().scoreProperty().get(),
                "Score should be reset to 0 on newGame");
        assertNotNull(gc.getBoard().getViewData(),
                "Board should have a new current brick after newGame");
    }
}