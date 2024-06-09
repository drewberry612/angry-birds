// RegNo. 2312089

package Assignment.Utilities;

import Assignment.AngryBirdsGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    // This class handles all images that the game uses
    // Each image is an instance of BufferedImage

    // static storage devices for every image that can be accessed easily from any class
    public static Map<AngryBirdsGame.LayoutMode, BufferedImage> backgrounds;
    public static Map<AngryBirdsGame.LayoutMode, BufferedImage> dirt; // holds the dirt and slingshot overlay for each level
    public static BufferedImage logo; // angry birds logo on first menu
    public static Map<String, BufferedImage[]> birds;
    public static Map<String, BufferedImage[]> piggies;
    public static Map<String, BufferedImage> polygons;
    public static BufferedImage explosion; // explosion effect image


    // constructor that loads every image
    public ImageManager() {
        // background images
        backgrounds = new HashMap<>();
        String backgroundsPath = "backgrounds/";

        backgrounds.put(AngryBirdsGame.LayoutMode.MENU_1, loadImage(backgroundsPath + "menu1"));
        backgrounds.put(AngryBirdsGame.LayoutMode.MENU_2, loadImage(backgroundsPath + "menu2"));
        backgrounds.put(AngryBirdsGame.LayoutMode.MENU_3, loadImage(backgroundsPath + "menu3"));
        backgrounds.put(AngryBirdsGame.LayoutMode.MENU_4, loadImage(backgroundsPath + "menu4"));
        backgrounds.put(AngryBirdsGame.LayoutMode.LEVEL_1, loadImage(backgroundsPath + "level1"));
        backgrounds.put(AngryBirdsGame.LayoutMode.LEVEL_2, loadImage(backgroundsPath + "level2"));
        backgrounds.put(AngryBirdsGame.LayoutMode.LEVEL_3, loadImage(backgroundsPath + "level3"));


        // dirt overlay images
        dirt = new HashMap<>();
        dirt.put(AngryBirdsGame.LayoutMode.MENU_1, null);
        dirt.put(AngryBirdsGame.LayoutMode.MENU_2, null);
        dirt.put(AngryBirdsGame.LayoutMode.MENU_3, null);
        dirt.put(AngryBirdsGame.LayoutMode.MENU_4, null);

        String otherPath = "other/";
        dirt.put(AngryBirdsGame.LayoutMode.LEVEL_1, loadImage(otherPath + "level_dirt1"));
        dirt.put(AngryBirdsGame.LayoutMode.LEVEL_2, loadImage(otherPath + "level_dirt2"));
        dirt.put(AngryBirdsGame.LayoutMode.LEVEL_3, loadImage(otherPath + "level_dirt3"));


        // logo image
        logo = loadImage(otherPath + "logo");


        // bird images
        birds = new HashMap<>();
        String birdPath = "birds/";

        birds.put("Red", new BufferedImage[]{loadImage(birdPath + "red"), null, loadImage(birdPath + "red_collide")});
        birds.put("Yellow", new BufferedImage[]{loadImage(birdPath + "yellow"), loadImage(birdPath + "yellow_fly"), null});
        birds.put("Black", new BufferedImage[]{loadImage(birdPath + "black"), null, loadImage(birdPath + "black_collide")});
        birds.put("Blue", new BufferedImage[]{loadImage(birdPath + "blue"), loadImage(birdPath + "blue_fly"), loadImage(birdPath + "blue_collide")});


        // piggie images
        piggies = new HashMap<>();
        String piggiePath = "piggies/";

        piggies.put("big_piggie", new BufferedImage[]{loadImage(piggiePath + "big_piggie"), loadImage(piggiePath + "big_piggie_scared")});
        piggies.put("small_piggie", new BufferedImage[]{loadImage(piggiePath + "small_piggie"), loadImage(piggiePath + "small_piggie_scared")});


        // object images
        polygons = new HashMap<>();
        String blocksPath = "blocks/";

        polygons.put("Glass", loadImage(blocksPath + "glass"));
        polygons.put("Wood", loadImage(blocksPath + "wood"));
        polygons.put("Explosive", loadImage(blocksPath + "explosive"));
        polygons.put("Rope", loadImage(blocksPath + "rope"));
        polygons.put("Stone", loadImage(blocksPath + "stone"));


        // explosion effect image
        explosion = loadImage(otherPath + "explosion");
    }

    // uses ImageIO to read an image file from the filepath given
    public BufferedImage loadImage(String file) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("Images/" + file + ".png"));
        }
        catch (Exception e) { // only used when an image file can't be found
            e.printStackTrace();
        }
        return img;
    }
}
