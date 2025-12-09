package com.comp2042.SceneController.BGControl;

import com.comp2042.Game.GameController;
import com.comp2042.Main;
import com.comp2042.Game.GuiController;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SceneChanger<T> {

    public static BackgroundMusic bgm;
    public static String CurrentBGM=null;
    private GameController gameController;

    /**
     * Function to change scene between scene (fxmls)
     * @param Scenefxml
     * @param BGM
     * @return
     * @param <T>
     * @throws IOException
     */
    public static <T> T scenechange(String Scenefxml, String BGM) throws IOException {

        if (BGM!=null){
            if (bgm == null || CurrentBGM == null || !CurrentBGM.equals(BGM)) {
                if (bgm != null) {
                    bgm.stop();
                }
                bgm = new BackgroundMusic("/Audio/" + BGM + ".mp3");
                bgm.play();
                CurrentBGM = BGM;
            }
        }

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/FXMLScene/" + Scenefxml + ".fxml")
        );

        if (loader.getLocation() == null) {
            throw new IllegalArgumentException(
                    "FXML NOT FOUND!\n" +
                            "Looking for: /FXMLScene/" + Scenefxml + ".fxml\n" +
                            "File must be in: src/main/resources/FXMLScene/" + Scenefxml + ".fxml"
            );
        }

        Parent newRoot = loader.load();
        T controller = loader.getController();

        if(controller instanceof GuiController){
            ((GuiController)controller).setBgmGame(bgm);
        }

        Stage stage = Main.primaryStage;
        Scene scene = stage.getScene();

        if (scene == null) {
            scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();
            return controller;
        }

        scene.setRoot(newRoot);
        stage.show();

        newRoot.setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), newRoot);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();


        return controller;
    }


    /**
     * Return current playing bgm
     * @return
     */
    public static BackgroundMusic getBgm() {
        return bgm;
    }

    /**
     * Apply game controller when game scene is played
     * @param GUIControl
     */
    public static void GameSceneControllerApply(GuiController GUIControl) {
        GameController gameController = new GameController(GUIControl);
        GUIControl.setEventListener(gameController);
        GUIControl.setGameController(gameController);
    }

    /**
     * When level increase, change the bgm
     * @param bgmmm
     */

    public static void levelIncreaseBgmChange(String bgmmm) {
        if(bgm==null) return;

        BackgroundMusic oldBgm = bgm;
        CurrentBGM = bgmmm;


        BackgroundMusic newBgm = new BackgroundMusic("/Audio/" + bgmmm + ".mp3");
        newBgm.setVolume(0.0);
        newBgm.play();
        bgm = newBgm;

        oldBgm.fadeTo(0.0, Duration.millis(1000), oldBgm::dispose);

        newBgm.fadeTo(0.3, Duration.millis(1000), null);
    }

}