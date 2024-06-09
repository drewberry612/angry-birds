// RegNo. 2312089
// some of these constants were adapted from code by Michael Fairbank

package Assignment.Utilities;

import java.awt.*;

public class Constants {
    // This class holds all the constant values that the game uses
    // As well as some functions that convert between coordinate systems

    // frame dimensions
    public static final int SCREEN_HEIGHT = 680;
    public static final int SCREEN_WIDTH = 640 * 2;
    public static final Dimension FRAME_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);

    // world dimensions
    public static final float WORLD_WIDTH = 10 * 2;//metres
    public static final float WORLD_HEIGHT = SCREEN_HEIGHT * (WORLD_WIDTH / SCREEN_WIDTH);// meters - keeps world dimensions in same aspect ratio as screen dimensions


    // physics values
    public static final float GRAVITY=9.8f;
    public static final float DRAG = 0.075f;
    public static final float EXPLOSION_RADIUS = 3;
    public static final float EXPLOSION_POWER = 3000;
    public static final float NINETY_DEGREES = (float) Math.PI/2;
    public static final float ONE_EIGHTY_DEGREES = (float) Math.PI;

    // entity densities
    public static final float WOOD_DENSITY = 2;
    public static final float GLASS_DENSITY = 1;
    public static final float EXPLOSIVE_DENSITY = 3;
    public static final float STONE_DENSITY = 7.2f;
    public static final float ROPE_DENSITY = 0.001f;
    public static final float BIRD_DENSITY = 10;
    public static final float PIGGIE_DENSITY = 1;


    // time values
    public static final int DELAY = 20; // sleep time between two drawn frames in milliseconds
    public static final int EULER_UPDATES_PER_FRAME = 1000;
    public static final float DELTA_T = DELAY / 1000.0f; // estimate for time between two frames in seconds


    // radii of different piggie sizes
    public static final float SMALL_PIGGIE = 0.4f;
    public static final float BIG_PIGGIE = 0.6f;


    // coordinates conversion functions
    public static int convertWorldXtoScreenX(float worldX) {
        return (int) (worldX/WORLD_WIDTH*SCREEN_WIDTH);
    }

    public static int convertWorldYtoScreenY(float worldY) {
        // minus sign in here is because screen coordinates are upside down
        return (int) (SCREEN_HEIGHT-(worldY/WORLD_HEIGHT*SCREEN_HEIGHT));
    }

    public static float convertWorldLengthToScreenLength(float worldLength) {
        return (worldLength/WORLD_WIDTH*SCREEN_WIDTH);
    }

    public static float convertScreenXtoWorldX(int screenX) {
        return screenX*WORLD_WIDTH/SCREEN_WIDTH;
    }

    public static float convertScreenYtoWorldY(int screenY) {
        return (SCREEN_HEIGHT-screenY)*WORLD_HEIGHT/SCREEN_HEIGHT;
    }
}
