// RegNo. 2312089

package Assignment.Utilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    // This class handles all sounds that the game uses
    // Every sound is an instance of Clip


    // storage devices for every sound
    // these don't need to be static due to each use of this class can be accessed through a reference in AngryBirdsGame
    public Map<String, Clip> birdSounds;
    public Map<String, Clip> objectsSounds;
    public Map<String, Clip> piggieSounds;
    public Map<String, Clip> otherSounds;
    private final Clip theme;


    // constructor that loads every sound clip
    public SoundManager() {
        // bird sounds
        birdSounds = new HashMap<>();
        String birdsPath = "birds/";

        birdSounds.put("bird1a", getClip(birdsPath + "bird1a"));
        birdSounds.put("bird1b", getClip(birdsPath + "bird1b"));
        birdSounds.put("bird2a", getClip(birdsPath + "bird2a"));
        birdSounds.put("bird2b", getClip(birdsPath + "bird2b"));
        birdSounds.put("bird3a", getClip(birdsPath + "bird3a"));
        birdSounds.put("bird3b", getClip(birdsPath + "bird3b"));
        birdSounds.put("bird4a", getClip(birdsPath + "bird4a"));
        birdSounds.put("bird4b", getClip(birdsPath + "bird4b"));

        birdSounds.put("bird_collision1", getClip(birdsPath + "bird_collision1"));
        birdSounds.put("bird_collision2", getClip(birdsPath + "bird_collision2"));
        birdSounds.put("bird_collision3", getClip(birdsPath + "bird_collision3"));
        birdSounds.put("bird_destroy", getClip(birdsPath + "bird_destroy"));


        // object sounds
        objectsSounds = new HashMap<>();
        String objectsPath = "objects/";

        objectsSounds.put("glass_destroy", getClip(objectsPath + "glass_destroy"));
        objectsSounds.put("rock_collide", getClip(objectsPath + "rock_collide"));
        objectsSounds.put("tnt_explode", getClip(objectsPath + "tnt_explode"));
        objectsSounds.put("wood_collide", getClip(objectsPath + "wood_collide"));
        objectsSounds.put("wood_destroy", getClip(objectsPath + "wood_destroy"));


        // piggie sounds
        piggieSounds = new HashMap<>();
        String piggiePath = "piggie/";

        piggieSounds.put("piggie_collision", getClip(piggiePath + "piggie_collision"));
        piggieSounds.put("piggie_destroy", getClip(piggiePath + "piggie_destroy"));


        // other sounds
        otherSounds = new HashMap<>();
        otherSounds.put("level_clear", getClip("level_clear"));
        otherSounds.put("sling", getClip("sling"));


        // theme music
        theme = getClip("music");
    }

    // plays the clip from start to end
    public static void playClip(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }

    // loops the theme music for a loong enough time that it shouldn't end before the game ends
    public void loopMusic() {
        theme.setFramePosition(0);
        theme.loop(1000);
    }

    // uses AudioInputStream to read a sound file from the filepath given
    private Clip getClip(String file) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("Sounds/" + file + ".wav"));
            clip.open(audio);
        }
        catch (Exception e) { // only used when a sound file can't be found
            e.printStackTrace();
        }
        return clip;
    }
}
