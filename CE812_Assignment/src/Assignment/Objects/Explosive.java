// RegNo. 2312089

package Assignment.Objects;

import Assignment.Polygon;
import Assignment.Utilities.ImageManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import static Assignment.Utilities.Constants.*;

public class Explosive extends Polygon {
    // used to create an explosive
    // explosives can only be squares
    public Explosive(float sx, float sy, float radius) {
        super(sx, sy, radius, Color.red, EXPLOSIVE_DENSITY, defineShape(radius), 4, 0, ImageManager.polygons.get("Explosive"));
        this.score = 100;
    }

    @Override
    public void draw(Graphics2D g) {
        // draws the square explosive at the correct scale

        // radius of the object on the screen rather than in the world
        int screenRadius = (int)Math.max(convertWorldLengthToScreenLength(radius),1);

        // conversions to screen coordinate system
        int x = convertWorldXtoScreenX(body.getPosition().x);
        int y = convertWorldYtoScreenY(body.getPosition().y);
        float a = body.getAngle();

        // transforms and scales the image to the correct shape/size
        double width = texture.getWidth(null);
        double height = texture.getHeight(null);
        AffineTransform transf = new AffineTransform();
        transf.scale(screenRadius * 2 / width, screenRadius * 2 / height);

        // draw the explosive at the correct angle and coordinates, and then reset the graphics to the original angle
        AffineTransform reset = g.getTransform();
        g.translate(x, y);
        g.rotate(a);
        g.translate(-screenRadius, -screenRadius);
        g.drawImage(texture, transf, null);
        g.setTransform(reset);
    }

    // defines a square shape, given a radius
    public static Path2D.Float defineShape(float radius) {
        Path2D.Float p = new Path2D.Float();
        p.moveTo(radius, radius);
        p.lineTo(-radius, radius);
        p.lineTo(-radius, -radius);
        p.lineTo(radius, -radius);
        p.lineTo(radius, radius);
        p.closePath();
        return p;
    }
}
