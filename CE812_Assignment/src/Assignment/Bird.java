// RegNo. 2312089
// Adapted from code by Michael Fairbank

package Assignment;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static Assignment.Utilities.Constants.*;

public class Bird {
	// this is the parent class for all birds/piggies in the game

	public final int screenRadius; // radius of the object in screen coords
	protected final float drag, mass, radius; // entity attributes
	public final Body body;
	public boolean setForDestruction, collided; // entity states
	public int score;
	public Map<String, BufferedImage> sprites;


	public Bird(float sx, float sy, float radius, float density, BufferedImage[] sprite) {
		// assign the attributes
		this.radius = radius;
		this.drag = DRAG;
		this.mass = (float) (density * Math.PI * radius * radius); // this is m=dA
		this.screenRadius = (int)Math.max(convertWorldLengthToScreenLength(radius),1);
		this.setForDestruction = false;
		this.collided = false;

		// assigns the sprites
		sprites = new HashMap<>();
		sprites.put("Main", sprite[0]);
		sprites.put("Fly", sprite[1]);
		sprites.put("Collide", sprite[2]);

		World w = AngryBirdsGame.world;

		// defines the body of the entity
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(sx, sy);
		bodyDef.linearVelocity.set(0, 0);
		this.body = w.createBody(bodyDef);

		// defines the shape of the entity
		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = radius;

		// defines the fixture
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = density;
		fixtureDef.friction = 0.8f; // this is surface friction
		fixtureDef.restitution = 0.5f;
		body.createFixture(fixtureDef);

		body.setUserData("Bird"); // used in CollisionHandler
	}


	public void draw(Graphics2D g) {
		BufferedImage sprite = decideSprite();
		if (sprite == null) {
			sprite = sprites.get("Main");
		}

		drawSprite(g, sprite);
	}

	// draws the sprite in the correct position, to the correct scale and angle
	public void drawSprite(Graphics2D g, BufferedImage sprite) {
		int x = convertWorldXtoScreenX(body.getPosition().x);
		int y = convertWorldYtoScreenY(body.getPosition().y);
		float a = body.getAngle();

		// defines the transformation to scale the sprite to th correct size
		double width = sprite.getWidth(null);
		double height = sprite.getHeight(null);
		AffineTransform transf = new AffineTransform();
		transf.scale(screenRadius * 2 / width, screenRadius * 2 / height);

		// draws the image at the correct angle and resets the graphics
		AffineTransform reset = g.getTransform();
		g.translate(x, y);
		g.rotate(-a);
		g.translate(-screenRadius, -screenRadius);
		g.drawImage(sprite, transf, null);
		g.setTransform(reset);
	}

	// decides which sprite will be drawn
	public BufferedImage decideSprite() {
		if (collided) { // after the bird hits an object
			return sprites.get("Collide");
		}
		else if (body.getLinearVelocity().length() > 2) { // after being flung
			return sprites.get("Fly");
		}
		else {
			return sprites.get("Main");
		}
	}

	// additional physics update that occurs once every frame
	public void timestepUpdate() {
		if (body.getType() == BodyType.DYNAMIC) { // make sure the bird has been slung
			if (drag > 0) {
				Vec2 force = new Vec2(body.getLinearVelocity());
				force = force.mul(-drag * mass);
				body.applyForceToCenter(force);
			}

			// this is the condition of the destruction of the bird
			if (body.getLinearVelocity().abs().y < 0.1 && body.getLinearVelocity().length() < 0.4) {
				setForDestruction = true;
			}
		}
	}

	public boolean isMouseWithinBird(Vec2 mouse) {
		if (body.getType() == BodyType.STATIC) {
			if (mouse.x > body.getPosition().x - radius && mouse.x < body.getPosition().x + radius) {
				return mouse.y > body.getPosition().y - radius && mouse.y < body.getPosition().y + radius;
			}
		}
		return false;
	}
}
