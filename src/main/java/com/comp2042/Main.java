package com.comp2042;

import com.comp2042.SceneController.BGControl.BackgroundMusic;
import com.comp2042.SceneController.BGControl.SceneChanger;
import com.comp2042.SceneController.MainMenu.MainMenuControl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;


    @Override
    public void start(Stage stage) throws Exception {
        primaryStage=stage;
        primaryStage.setTitle("Tetris");
        SceneChanger.scenechange("MainMenu", "MainMenuBGM");
        primaryStage.setWidth(1152);
        primaryStage.setHeight(670);
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
