// RegNo. 2312089
// Adapted from code by Michael Fairbank

package Assignment;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;

import static Assignment.Utilities.Constants.*;

public class Barrier {
	// this class is used to create the boundaries of the world

	private final Vec2 startPos, endPos;
	private final Color col;
	public final Body body;

	public Barrier(float startx, float starty, float endx, float endy) {
		startPos = new Vec2(startx, starty);
		endPos = new Vec2(endx, endy);

		World w = AngryBirdsGame.world;

		// defines the body of the barrier
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC; // body is static so it doesn't move and acts as a wall
		bodyDef.position = new Vec2(startx,starty);
		this.body = w.createBody(bodyDef);

		// creates a chain shape that is completely straight
		Vec2[] vertices = new Vec2[] { new Vec2(), new Vec2(endx-startx, endy-starty) };
		ChainShape chainShape = new ChainShape();
		chainShape.createChain(vertices, vertices.length);

		// defines the fixture of the barrier
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = chainShape;
		fixtureDef.density = 0;
		fixtureDef.friction = 0.4f;// this is surface friction
		body.createFixture(fixtureDef);

		this.col = Color.WHITE;
		body.setUserData("Barrier"); // used in CollisionHandler
	}

	// used when the barriers need to be seen
	public void draw(Graphics2D g) {
		int x1 = convertWorldXtoScreenX(startPos.x);
		int y1 = convertWorldYtoScreenY(startPos.y);
		int x2 = convertWorldXtoScreenX(endPos.x);
		int y2 = convertWorldYtoScreenY(endPos.y);
		g.setColor(col);
		g.drawLine(x1, y1, x2, y2);
	}
}
