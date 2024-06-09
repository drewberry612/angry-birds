// RegNo. 2312089
// Adapted from code by Michael Fairbank

package Assignment;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;

import static Assignment.Utilities.Constants.*;

public class Polygon {
	// this class is the parent class for each object in the game

	public float ratioOfScreenScaleToWorldScale; // used to scale the sprite to the correct size
	private float drag; // linear drag force
	private final float mass;
	public Color col;
	public Body body;
	public Path2D.Float polygonPath1, polygonPath2; // polygonPath2 can be null
	protected boolean setForDestruction; // when true, this body will be destroyed in the next update
	public int score; // score for destroying the object
	public float radius;
	public BufferedImage texture; // the sprite/texture of the object


	// constructor for regular shapes that require one fixture
	public Polygon(float sx, float sy, float radius, Color col, float density, Path2D.Float polygonPath, int numSides, float angle, BufferedImage texture) {
		World w = AngryBirdsGame.world;
		assignAttributes(col, new Path2D.Float[] {polygonPath, null}, radius, texture);
		defineBody(w, sx, sy);

		this.mass = calculateMass(density, numSides);

		PolygonShape shape = createShape(polygonPath1, numSides);

		FixtureDef fixtureDef = createFixture(shape, density);
		body.createFixture(fixtureDef);

		body.setTransform(body.getPosition(), angle);
		body.setUserData("Polygon"); // used in CollisionHandler
	}

	// constructor for complex shapes that require multiple fixtures
	public Polygon(float sx, float sy, float radius, Color col, float density, Path2D.Float[] polygonPath, int numSides1, int numSides2, float angle, BufferedImage texture) {
		World w = AngryBirdsGame.world;
		assignAttributes(col, polygonPath, radius, texture);
		defineBody(w, sx, sy);

		// this is the mass of the two combined parts of the complex shape
		this.mass = calculateMass(density, numSides1) + calculateMass(density, numSides2);

		PolygonShape shape1 = createShape(polygonPath1, numSides1);
		PolygonShape shape2 = createShape(polygonPath2, numSides2);

		FixtureDef fixtureDef = createFixture(shape1, density);
		body.createFixture(fixtureDef);

		fixtureDef = createFixture(shape2, density);
		body.createFixture(fixtureDef);

		body.setTransform(body.getPosition(), angle);
		body.setUserData("Polygon"); // used in CollisionHandler
	}

	// calculates the mass of the object from its density and number of sides
	public float calculateMass(float density, int numSides) {
		return (float) (density * (numSides / 2f) * radius * radius * Math.sin(2*Math.PI/numSides));
	}

	// creates a FixtureDef for a given shape
	// is invoked multiple times for complex shapes
	public FixtureDef createFixture(PolygonShape shape, float density) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = 0.42f; // this is surface friction
		fixtureDef.restitution = 0.5f;
		return fixtureDef;
	}

	// creates the shape of each fixture
	public PolygonShape createShape(Path2D.Float polygonPath, int numSides) {
		PolygonShape shape = new PolygonShape();
		Vec2[] vertices = verticesOfPath2D(polygonPath, numSides);
		shape.set(vertices, numSides);
		return shape;
	}

	// defines the body of this object in the world
	public void defineBody(World w, float sx, float sy) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(sx, sy);
		bodyDef.linearVelocity.set(0, 0);
		bodyDef.angularDamping = 0.075f;
		this.body = w.createBody(bodyDef);
	}

	// sets all the attributes to constant or given values
	public void assignAttributes(Color col, Path2D.Float[] polygonPath, float radius, BufferedImage texture) {
		this.drag = DRAG;
		this.ratioOfScreenScaleToWorldScale = convertWorldLengthToScreenLength(1);
		this.col = col;
		this.polygonPath1 = polygonPath[0];
		this.polygonPath2 = polygonPath[1];
		this.setForDestruction = false;
		this.radius = radius;
		this.score = 0;
		this.texture = texture;
	}

	// draws the shape with a given texture
	public void draw(Graphics2D g) {
		Vec2 position = body.getPosition();
		float angle = body.getAngle();

		// scales the shape on screen to the body shape
		AffineTransform af = new AffineTransform();
		af.translate(convertWorldXtoScreenX(position.x), convertWorldYtoScreenY(position.y));
		af.scale(ratioOfScreenScaleToWorldScale, -ratioOfScreenScaleToWorldScale);// there is a minus in here because screenworld is flipped upsidedown compared to physics world
		af.rotate(angle);

		Path2D.Float p = new Path2D.Float(polygonPath1, af);

		// sets the paint that will be used to fill the shape
		TexturePaint texturePaint = new TexturePaint(texture, p.getBounds2D());
		g.setPaint(texturePaint);

		g.fill(p);
		g.setColor(col);
		g.draw(p);

		if (polygonPath2 != null) { // when the shape is complex, draw the other shape
			p = new Path2D.Float(polygonPath2, af);
			g.setPaint(texturePaint);
			g.fill(p);
			g.setColor(col);
			g.draw(p);
		}
	}

	// additional physics updates, invoked at each timestep
	public void timestepUpdate() {
		if (drag > 0) {
			Vec2 force = new Vec2(body.getLinearVelocity());
			force = force.mul(-drag * mass);
			body.applyForceToCenter(force);
		}
	}
	
	// Vec2 vertices of Path2D
	public static Vec2[] verticesOfPath2D(Path2D.Float p, int n) {
		Vec2[] result = new Vec2[n];
		float[] values = new float[6];
		PathIterator pi = p.getPathIterator(null);
		int i = 0;
		while (!pi.isDone() && i < n) {
			int type = pi.currentSegment(values);
			if (type == PathIterator.SEG_LINETO) {
				result[i++] = new Vec2(values[0], values[1]);
			}
			pi.next();
		}
		return result;
	}
}