// RegNo. 2312089

package Assignment.Objects;

import Assignment.Polygon;
import Assignment.Utilities.ImageManager;

import java.awt.*;
import java.awt.geom.Path2D;

import static Assignment.Utilities.Constants.STONE_DENSITY;

public class Stone extends Polygon {
    // used to create stone
    // stone can be a square, triangle, rectangle, or a complex shape
    // stone cannot be destroyed

    // constructor for square and triangle
    public Stone(float sx, float sy, int type, int numSides, float radius) {
        super(sx, sy, radius, Color.gray, STONE_DENSITY, defineShape(type, radius), numSides, 0, ImageManager.polygons.get("Stone"));
    }

    // constructor for complex shapes
    public Stone(float sx, float sy, int type, int factor1, int factor2, float radius, float angle) {
        super(sx, sy, radius, Color.gray, STONE_DENSITY, defineComplexShape(type, radius, factor1, factor2), 4, 4, angle, ImageManager.polygons.get("Stone"));
    }

    // constructor for rectangles
    public Stone(float sx, float sy, float radius, int factor, float angle) {
        super(sx, sy, radius, Color.gray, STONE_DENSITY, defineRectangleShape(factor, radius), 4, angle, ImageManager.polygons.get("Stone"));
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
            case 4 -> { // triangle
                p.moveTo(0, radius);
                p.lineTo(-radius, -radius);
                p.lineTo(radius, -radius);
                p.lineTo(0, radius);
            }
        }
        p.closePath();
        return p;
    }

    // defines the shape of a complex shape
    public static Path2D.Float[] defineComplexShape(int type, float radius, int factor1, int factor2) {
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
        p.closePath();
        return p;
    }
}
