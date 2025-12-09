package com.comp2042.SceneController.BGControl;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Background {
    /**
     * Apply mp4 format video to background, and keep repeat
     * @param mediaview_pane
     * @param background_path
     */
    public static void ApplyGif(MediaView mediaview_pane, String background_path) {
        if (mediaview_pane == null) {
            System.err.println("MediaView Pane is null");
            return;
        }

        try {
            String path = Background.class.getResource("/Backgrounds/" + background_path).toExternalForm();
            if (path == null || path.contains("null")) {
                System.err.println("Background Video not found: " + background_path);
                System.err.println("Put it in: src/main/resources/Backgrounds/" + background_path);
                return;
            }

            Media media = new Media(path);
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaview_pane.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setMute(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setRate(1.0);  // Start forward
            mediaPlayer.play();

            System.out.println("Background Ran Successfully — Infinite Reverse Loop Active!");

        } catch (Exception e) {
            System.err.println("Background Ran Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}