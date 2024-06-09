// RegNo. 2312089

package Assignment.Objects;

import Assignment.Polygon;
import Assignment.Utilities.ImageManager;

import java.awt.*;
import java.awt.geom.Path2D;

import static Assignment.Utilities.Constants.GLASS_DENSITY;

public class Glass extends Polygon {
    // used to create glass
    // glass can only be rectangles
    public Glass(float sx, float sy, int factor, float radius, float angle) {
        super(sx, sy, radius, new Color(173, 216, 230), GLASS_DENSITY, defineShape(factor, radius), 4, angle, ImageManager.polygons.get("Glass"));
        this.score = 50;
    }

    // defines a rectangle shape
    public static Path2D.Float defineShape(int factor, float radius) {
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
