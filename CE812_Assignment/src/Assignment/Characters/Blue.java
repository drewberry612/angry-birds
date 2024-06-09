// RegNo. 2312089

package Assignment.Characters;

import Assignment.Bird;
import Assignment.Utilities.ImageManager;

import static Assignment.Utilities.Constants.BIRD_DENSITY;

public class Blue extends Bird {
    // used to create the blue bird so that the correct images can be drawn
    // could be expanded to give the bird a special effect
    public Blue() {
        super(1.5f, 4, 0.2f, BIRD_DENSITY, ImageManager.birds.get("Blue"));
        this.score = 50;
    }
}
