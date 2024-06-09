// RegNo. 2312089

package Assignment.Characters;

import Assignment.Bird;
import Assignment.Utilities.ImageManager;

import static Assignment.Utilities.Constants.BIRD_DENSITY;

public class Red extends Bird {
    // used to create the red bird so that the correct images can be drawn
    // could be expanded to give the bird a special effect
    public Red() {
        super(1.5f, 4, 0.4f, BIRD_DENSITY, ImageManager.birds.get("Red"));
        this.score = 100;
    }
}
