package com.comp2042.GameInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersFunctionTest {

    @BeforeEach
    void setUp() {
        UsersFunction.resetForTesting();
    }

    @Test
    void registerUser() {
        UsersFunction.RegisterUser("bla", "1234");

        // user map updated
        assertTrue(UsersFunction.user.containsKey("bla"));
        assertEquals("1234", UsersFunction.user.get("bla"));

        // scores map updated
        assertEquals(0, UsersFunction.scores.getOrDefault("bla", -1));
    }


    @Test
    void loginVerify() {
        UsersFunction.RegisterUser("bib", "pass");

        assertTrue(UsersFunction.loginVerify("bib", "pass"));
        assertFalse(UsersFunction.loginVerify("bib", "wrong"));
        assertFalse(UsersFunction.loginVerify("unknown", "pass"));
    }

    @Test
    void updateScores() {
        UsersFunction.RegisterUser("charlie", "pw");

        UsersFunction.updateScores("charlie", 100);
        assertEquals(100, UsersFunction.scores.getOrDefault("charlie", 0),"Score not updated");

        UsersFunction.updateScores("charlie", 50);
        assertEquals(100, UsersFunction.scores.getOrDefault("charlie", 0),
                "High score should be 100 (50<100)");

        UsersFunction.updateScores("charlie", 200);
        assertEquals(200, UsersFunction.scores.getOrDefault("charlie", 0),
                "High score should be 200");
    }

    @Test
    void currentUserUpdate() {
        UsersFunction.RegisterUser("bib", "pw");
        UsersFunction.updateScores("bib", 150);

        UsersFunction.currentUserUpdate("bib");

        assertEquals("bib", UsersFunction.currentUser, "Current user should be bib");
        assertEquals(150, UsersFunction.currentUserScore,  "Current user score should be 150");
    }

    @Test
    void userHighScore() {
        UsersFunction.RegisterUser("bla", "pw");
        UsersFunction.updateScores("bla", 300);

        String hs = UsersFunction.UserHighScore("bla");
        assertEquals("300", hs);
    }
}