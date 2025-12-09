package com.comp2042.SceneController.MainMenu;

import com.comp2042.GameLogic.SimpleBoard;
import com.comp2042.GameInfo.UsersFunction;
import com.comp2042.GameInfo.ViewData;
import com.comp2042.SceneController.BGControl.Background;
import com.comp2042.SceneController.BGControl.SceneChanger;
import com.comp2042.Game.GuiController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainMenuControl {

    @FXML private Button Exit;
    @FXML private Button LeaderBoard;
    @FXML private Button PlayAnnonymous;
    @FXML private Button PlayUser;
    @FXML private VBox MenuPane;
    @FXML private MediaView background;
    @FXML private VBox leaderboardContainer;
    @FXML private Button leaderboardBackButton;
    @FXML private AnchorPane leaderboardPane;

    public static SimpleBoard board;
    public static ViewData brick;

    @FXML
    private void initialize() throws IOException {
        MenuPane.setBackground(javafx.scene.layout.Background.EMPTY);
        Background.ApplyGif(background,"GameBackground.mp4");
        MenuPane.setVisible(true);
        leaderboardPane.setVisible(false);
        UsersFunction.loadFromFile();

        if (UsersFunction.UserLoggedIn){
            PlayAnnonymous.setText("Play as "+ UsersFunction.currentUser);
            PlayUser.setText("Log in as other user");

            PlayAnnonymous.setWrapText(true);
            PlayUser.setWrapText(true);

            PlayAnnonymous.setTextAlignment(TextAlignment.CENTER);
            PlayUser.setTextAlignment(TextAlignment.CENTER);
        }
        else{
            UsersFunction.currentUser="Guest";
        }

        PlayUser.setOnAction(e -> {
            try {
                User_Register control= SceneChanger.scenechange("ProfileRegistery","MainMenuBGM");

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        PlayAnnonymous.setOnAction(e -> {
            try {
                GuiController controller= SceneChanger.scenechange("gameLayout", "TetrisGameBGM");
                SceneChanger.GameSceneControllerApply(controller);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        LeaderBoard.setOnAction(e -> {
            MenuPane.setVisible(false);
            leaderboardPane.setVisible(true);
            populateLeaderboard();
        });

        leaderboardBackButton.setOnAction(e->{
            leaderboardPane.setVisible(false);
            MenuPane.setVisible(true);
        });

        Exit.setOnAction(e -> {
            Platform.exit();
        });
    }

    /**
     * Create Leaderboard
     */
    private void populateLeaderboard() {
        leaderboardContainer.getChildren().clear();

        List<Map.Entry<String, Integer>> entries = UsersFunction.getLeaderboard();

        int rank = 1;
        for (Map.Entry<String, Integer> e : entries) {
            String username = e.getKey();
            int score = e.getValue();

            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(4, 12, 4, 12));
            row.setPrefHeight(32);

            Label rankLabel = new Label(String.valueOf(rank));
            rankLabel.getStyleClass().add("lb-rank");

            Label nameLabel = new Label(username);
            nameLabel.getStyleClass().add("lb-name");
            HBox.setHgrow(nameLabel, Priority.ALWAYS);

            Label scoreLabel = new Label(String.valueOf(score));
            scoreLabel.getStyleClass().add("lb-score");

            if (rank == 1) {
                row.getStyleClass().add("lb-row-first");
            } else if (rank == 2) {
                row.getStyleClass().add("lb-row-second");
            } else if (rank == 3) {
                row.getStyleClass().add("lb-row-third");
            } else if (rank % 2 == 0) {
                row.getStyleClass().add("lb-row-even");
            } else {
                row.getStyleClass().add("lb-row-odd");
            }

            row.getChildren().addAll(rankLabel, nameLabel, scoreLabel);
            leaderboardContainer.getChildren().add(row);

            rank++;
        }
    }
}