// RegNo. 2312089
// Adapted from code by Michael Fairbank

package Assignment.Utilities;

import org.jbox2d.common.Vec2;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Assignment.Utilities.Constants.convertScreenXtoWorldX;
import static Assignment.Utilities.Constants.convertScreenYtoWorldY;

public class MouseControls extends MouseAdapter {
	// This class is implemented twice in AngryBirds game
	// The first being as a MouseListener, the second being as a MouseMotionListener#
	// This allows for all functions to be used


	// positions of the mouse when the mouse is clicked, while it is being dragged, and when the mouse is released
	private static int mouse_initX, mouse_initY, mouse_finalX, mouse_finalY, mouse_currentX, mouse_currentY;

	private static boolean mouseButtonPressed, mouseButtonReleased;

	public void mousePressed(MouseEvent e) {
		if (!mouseButtonPressed) { // makes sure that the initial mouse position can't change
			mouse_initX = e.getX();
			mouse_initY = e.getY();
		}
		mouseButtonPressed = true;
	}

	// this will always change the x and y coords while the mouse is dragged
	public void mouseDragged(MouseEvent e) {
		mouse_currentX = e.getX();
		mouse_currentY = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		mouse_finalX = e.getX();
		mouse_finalY = e.getY();
		mouseButtonReleased = true;
		mouseButtonPressed = false;
	}

	public static boolean isMouseButtonPressed() {
		return mouseButtonPressed;
	}
	public static boolean isMouseButtonReleased() {
		return mouseButtonReleased;
	}

	public static void setMouseButtonReleased(boolean mouseButtonReleased) {
		MouseControls.mouseButtonReleased = mouseButtonReleased;
	}

	// these convert each mouse position to world coordinates for correlating with the bird
	public static Vec2 getWorldCoordinatesOfMousePointerInit() {
		return new Vec2(convertScreenXtoWorldX(mouse_initX), convertScreenYtoWorldY(mouse_initY));
	}
	public static Vec2 getWorldCoordinatesOfMousePointerFinal() {
		return new Vec2(convertScreenXtoWorldX(mouse_finalX), convertScreenYtoWorldY(mouse_finalY));
	}
	public static Vec2 getWorldCoordinatesOfMousePointerCurrent() {
		return new Vec2(convertScreenXtoWorldX(mouse_currentX), convertScreenYtoWorldY(mouse_currentY));
	}
}
