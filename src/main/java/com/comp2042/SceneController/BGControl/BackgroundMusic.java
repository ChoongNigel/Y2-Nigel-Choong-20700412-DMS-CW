package com.comp2042.SceneController.BGControl;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class BackgroundMusic {
    private MediaPlayer player;

    /**
     * Apply background music
     * @param source
     */
    public BackgroundMusic(String source){
        var url=getClass().getResource(source);

        if (url==null){
            System.err.println("Background Music not found: " + source);
        }

        assert url != null;
        Media media=new Media(url.toExternalForm());
        player=new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setAutoPlay(true);
        player.setVolume(0.3);
    }

    public void play(){
        if (player != null) {
            player.play();
        }
    }

    public void pause(){
        if (player != null) {
            player.pause();
        }
    }

    public void stop(){
        if (player != null) {
            player.stop();
        }
    }

    public void dispose(){
        if (player != null) {
            player.dispose();
            player = null;
        }
    }

    public void fadeTo(double targetVolume, Duration duration, Runnable onFinished) {
        if (player == null) {
            if (onFinished != null) onFinished.run();
            return;
        }

        Timeline fade = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(player.volumeProperty(), player.getVolume())),
                new KeyFrame(duration,
                        new KeyValue(player.volumeProperty(), targetVolume))
        );
        if (onFinished != null) {
            fade.setOnFinished(e -> onFinished.run());
        }
        fade.play();
    }

    public double getVolume() {
        return player == null ? 0.0 : player.getVolume();
    }

    public void setVolume(double v) {
        if (player != null) {
            player.setVolume(v);
        }
    }
}
