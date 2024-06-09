// RegNo. 2312089

package Assignment.Characters;

import Assignment.Bird;
import Assignment.Utilities.ImageManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static Assignment.Utilities.Constants.*;

public class Piggie extends Bird {
    // used to create a piggie so that the correct images can be drawn

    public Piggie(float sx, float sy, float radius) {
        // nulls here because piggies use a different sprite system to the birds
        super(sx, sy, radius, PIGGIE_DENSITY, new BufferedImage[] {null, null, null});

        body.setType(BodyType.DYNAMIC); // changed to dynamic so the piggie can move
        body.setUserData("Piggie"); // used in collision handler
        sprites = new HashMap<>();

        // this if else determines which type of piggie it is and assigns certain variables
        if (radius == SMALL_PIGGIE) {
            this.score = 500;
            sprites.put("Main", ImageManager.piggies.get("small_piggie")[0]);
            sprites.put("Scared", ImageManager.piggies.get("small_piggie")[1]);
        }
        else {
            this.score = 1000;
            sprites.put("Main", ImageManager.piggies.get("big_piggie")[0]);
            sprites.put("Scared", ImageManager.piggies.get("big_piggie")[1]);
        }
    }

    @Override
    public void timestepUpdate() {
        // this override stops the piggie from being destroyed when it is still
        if (drag > 0) { // applies a drag effect
            Vec2 force = new Vec2(body.getLinearVelocity());
            force = force.mul(-drag * mass);
            body.applyForceToCenter(force);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        BufferedImage sprite;
        if (body.getLinearVelocity().length() < 1) {
            sprite = sprites.get("Main");
        }
        else { // when the piggie is moving above a certain speed change the image
            sprite = sprites.get("Scared");
        }

        drawSprite(g, sprite); // see Bird class for the implementation
    }
}
