// RegNo. 2312089

package Assignment.Characters;

import Assignment.Bird;
import Assignment.Utilities.ImageManager;

import static Assignment.Utilities.Constants.BIRD_DENSITY;

public class Black extends Bird {
    // used to create the black bird so that the correct images can be drawn
    // could be expanded to give the bird a special effect
    public Black() {
        super(1.5f, 4, 0.7f, BIRD_DENSITY, ImageManager.birds.get("Black"));
        this.score = 150;
    }
}
