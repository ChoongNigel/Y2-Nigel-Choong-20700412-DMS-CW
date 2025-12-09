package com.comp2042.SceneController.BGControl;

import javafx.scene.media.AudioClip;

public class SoundEffect {
    private final AudioClip clip;

    /**
     * Sound effect class for applying sound effect on key pressed
     * @param soundFile
     * @param volume
     */
    public SoundEffect(String soundFile, double volume) {
        var url = getClass().getResource("/Audio/"+soundFile+".mp3");
        if (url == null) {
            throw new IllegalArgumentException("Sound effect not found: " + soundFile);
        }
        clip = new AudioClip(url.toExternalForm());
        clip.setVolume(volume);
    }

    public void play() {
        clip.play();
    }
}
