// RegNo. 2312089
// Adapted from code by Michael Fairbank

package Assignment.Objects;

import Assignment.AngryBirdsGame;
import Assignment.Characters.Piggie;
import Assignment.Polygon;
import Assignment.Utilities.ImageManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import static Assignment.Utilities.Constants.ROPE_DENSITY;

public class Rope {
	// this is an object that stores 3 polygons that act as a rope
	// with joints between two objects

	public ArrayList<Polygon> parts; // holds each segment of the rope
	public boolean setForDestruction; // used to determine whether the rope should be destoryed

	// ropes are only active between a piggie and wood
	// the anchor parameters are local body coordinates of each object, that determine where the joint will go
	public Rope(Wood block, Piggie piggie, Vec2 anchor1, Vec2 anchor2) {
		// assign variables
		parts = new ArrayList<>();
		setForDestruction = false;

		// determine the end positions of the whole rope
		Vec2 end2 = block.body.getPosition().add(anchor1);
		Vec2 end1 = piggie.body.getPosition();

		float r = end2.sub(end1).length() / 3; // length of each segment

		// defines a rectangle shape for the segments of rope
		Path2D.Float p = new Path2D.Float();
		p.moveTo(r/6, r/2);
		p.lineTo(-r/6, r/2);
		p.lineTo(-r/6, -r/2);
		p.lineTo(r/6, -r/2);
		p.lineTo(r/6, r/2);
		p.closePath();

		// creates each segment, which are instances of Polygon
		int j = 3;
		float x = end1.x;
		for (int i=1; i<=j; i++) {
			float y = (i* end2.sub(end1).abs().y/j) - r/4; // the y coord of the center of the segment
			parts.add(new Polygon(x, end1.y + y, r/6, Color.white, ROPE_DENSITY, p, 4, 0, ImageManager.polygons.get("Rope")));
			parts.get(i-1).body.setUserData("Rope");
		}

		// creates a joint between the wood and the first segment of rope
		jointCreation(block.body, parts.get(2).body, new Vec2(anchor2), new Vec2(0, 3*parts.get(2).radius), true);

		// creates a joint between the first and second segments of rope
		jointCreation(parts.get(2).body, parts.get(1).body, new Vec2(0, -3*parts.get(2).radius), new Vec2(0, 3*parts.get(1).radius), false);

		// creates a joint between the second and last segments of rope
		jointCreation(parts.get(1).body, parts.get(0).body, new Vec2(0, -3*parts.get(1).radius), new Vec2(0, 3*parts.get(0).radius), false);

		// creates a joint between the last segment of rope and the piggie
		jointCreation(parts.get(0).body, piggie.body, new Vec2(0, -3*parts.get(0).radius), new Vec2(0, 0), false);
	}

	// creates a revolute joint given two bodies and anchors
	public void jointCreation(Body bodyA, Body bodyB, Vec2 anchorA, Vec2 anchorB, boolean collide) {
		RevoluteJointDef jd = new RevoluteJointDef();
		jd.bodyA = bodyA;
		jd.bodyB = bodyB;
		jd.localAnchorA = anchorA;
		jd.localAnchorB = anchorB;
		jd.collideConnected = collide;
		AngryBirdsGame.world.createJoint(jd);
	}

	// draws each part separately
	public void draw(Graphics2D g) {
		for (Polygon p: parts) {
			p.draw(g);
		}
	}
}
