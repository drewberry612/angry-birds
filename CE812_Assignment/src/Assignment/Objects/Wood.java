// RegNo. 2312089

package Assignment.Objects;

import Assignment.Polygon;
import Assignment.Utilities.ImageManager;

import java.awt.*;
import java.awt.geom.Path2D;

import static Assignment.Utilities.Constants.WOOD_DENSITY;

public class Wood extends Polygon {
    // used to create wood
    // wood can be a square, triangle, rectangle, or a complex shape
    // wood can also take a few hits before being destroyed

    public float hp; // hit points of the wood
    public int invulnerable; // number of updates before the wood can be hit again

    // constructor for square and triangle
    public Wood(float sx, float sy, int type, int numSides, float radius) {
        super(sx, sy, radius, new Color(150,75,0), WOOD_DENSITY, defineShape(type, radius), numSides, 0, ImageManager.polygons.get("Wood"));
        assignAdditionalAttributes();
    }

    // constructor for complex shapes
    public Wood(float sx, float sy, int type, int factor1, int factor2, float radius, float angle) {
        super(sx, sy, radius, new Color(150,75,0), WOOD_DENSITY, defineComplexShape(type, radius, factor1, factor2), 4, 4, angle, ImageManager.polygons.get("Wood"));
        assignAdditionalAttributes();
    }

    // constructor for rectangles
    public Wood(float sx, float sy, float radius, int factor, float angle) {
        super(sx, sy, radius, new Color(150,75,0), WOOD_DENSITY, defineRectangleShape(factor, radius), 4, angle, ImageManager.polygons.get("Wood"));
        assignAdditionalAttributes();
    }

    // assigns values to additional attributes that Wood has
    public void assignAdditionalAttributes() {
        this.hp = 3;
        this.score = 150;
        this.invulnerable = 0;
    }

    // defines the shape of a square or triangle
    public static Path2D.Float defineShape(int type, float radius) {
        Path2D.Float p = new Path2D.Float();

        switch (type) {
            case 1 -> { // square
                p.moveTo(radius, radius);
                p.lineTo(-radius, radius);
                p.lineTo(-radius, -radius);
                p.lineTo(radius, -radius);
                p.lineTo(radius, radius);
            }
            case 2 -> { // triangle
                p.moveTo(0, radius);
                p.lineTo(-2*radius, -radius);
                p.lineTo(2*radius, -radius);
                p.lineTo(0, radius);
            }
        }
        p.closePath();
        return p;
    }

    // defines the shape of a complex shape
    public static Path2D.Float[] defineComplexShape(int type, float radius, float factor1, float factor2) {
        Path2D.Float p1 = new Path2D.Float();
        Path2D.Float p2 = new Path2D.Float();

        switch (type) {
            case 1 -> { // l-shape
                // horizontal
                p1.moveTo(-radius, 0);
                p1.lineTo(-radius, -radius);
                p1.lineTo(factor2*radius, -radius);
                p1.lineTo(factor2*radius, 0);
                p1.lineTo(-radius, 0);

                // vertical
                p2.moveTo(0, 0);
                p2.lineTo(0, factor1*radius);
                p2.lineTo(-radius, factor1*radius);
                p2.lineTo(-radius, 0);
                p2.lineTo(0, 0);
            }
            case 2 -> { // t-shape
                // horizontal
                p1.moveTo(-factor2*radius/2, 0);
                p1.lineTo(-factor2*radius/2, -radius);
                p1.lineTo(factor2*radius/2, -radius);
                p1.lineTo(factor2*radius/2, 0);
                p1.lineTo(-factor2*radius/2, 0);

                // vertical
                p2.moveTo(-radius/2, 0);
                p2.lineTo(-radius/2, factor1*radius);
                p2.lineTo(radius/2, factor1*radius);
                p2.lineTo(radius/2, 0);
                p2.lineTo(-radius/2, 0);
            }
        }

        p1.closePath();
        p2.closePath();

        return new Path2D.Float[] {p1, p2};
    }

    // defines the shape of a rectangle
    public static Path2D.Float defineRectangleShape(int factor, float radius) {
        Path2D.Float p = new Path2D.Float();
        float y = radius * factor;

        p.moveTo(radius, y);
        p.lineTo(-radius, y);
        p.lineTo(-radius, -y);
        p.lineTo(radius, -y);
        p.lineTo(radius, y);
        p.closePath();
        return p;
    }


    // used to apply damage to the wood object
    public void takeDamage() {
        if (invulnerable == 0) {
            hp--;
            invulnerable = 15; // 15 updates before the wood can be hit again
            if (hp == 0) { // destroy if no hit points left
                setForDestruction = true;
            }
        }
    }


    @Override
    public void timestepUpdate() {
        super.timestepUpdate();
        if (invulnerable > 0) { // reduces the invulnerable frames
            invulnerable--;
        }
    }
}
