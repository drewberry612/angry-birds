// RegNo. 2312089
// Adapted from code by Michael Fairbank

package Assignment;

import Assignment.Characters.Piggie;
import Assignment.Objects.Rope;
import Assignment.Utilities.ImageManager;
import Assignment.Utilities.MouseControls;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import static Assignment.Utilities.Constants.*;

public class View extends JComponent {
	// this class controls all the visuals of the game

	private AngryBirdsGame game;

	public View(AngryBirdsGame game) {
		this.game = game;
	}
	
	@Override
	public void paintComponent(Graphics g0) {
		// this method is invoked every frame of the game

		AngryBirdsGame game;
		synchronized (this) {
			game = this.game;
		}
		Graphics2D g = (Graphics2D) g0;
		g.drawImage(game.currentBG, game.bgTransform, null); // this draws the background image

		switch (game.layout) {
			case LEVEL_1, LEVEL_2, LEVEL_3 -> { // when in a level
				g.drawImage(game.currentDirt, game.dirtTransform, null);
				drawEntities(game, g);
				drawTrajectoryLine(game, g);
				drawOverlay(game, g);
			}
			case MENU_1 -> { // when in first menu
				g.drawImage(ImageManager.logo, 140, 100,null);
				text(400, 500, 50, 4, "Press enter to start", g);
			}
			case MENU_2 -> { // when in menu after level 1
				drawMenu(1, AngryBirdsGame.LayoutMode.LEVEL_1, g);
				text(365, 460, 40, 3, "Press enter to start next level", g);
			}
			case MENU_3 -> { // when in menu after level 2
				drawMenu(2, AngryBirdsGame.LayoutMode.LEVEL_2, g);
				text(365, 460, 40, 3, "Press enter to start next level", g);
			}
			case MENU_4 -> { // when in menu after level 3
				drawMenu(3, AngryBirdsGame.LayoutMode.LEVEL_3, g);
				text(475, 460, 40, 3, "Press enter to quit", g);
			}
		}
	}

	// draws the text on each menu after a level
	private void drawMenu(int type, AngryBirdsGame.LayoutMode l, Graphics2D g) {
		text(440, 200, 50, 3, "Level " + type + " complete", g);
		text(490, 300, 40, 3, "Score: " + game.scores.get(l), g);
		String time = generateTimer(game.times.get(l)); // used to generate the text a timer in the correct format
		text(490, 360, 40, 3, "Time taken: " + time, g);
	}

	// draws each entity in the current level
	private void drawEntities(AngryBirdsGame game, Graphics2D g) {
		// draws the entities that are stored in arrays
		for (Polygon p : game.polygons)
			p.draw(g);
		for (Rope r : game.ropes)
			r.draw(g);
		for (Piggie p : game.piggies)
			p.draw(g);

		if (game.bird != null) { // stops the error that occurs when there is no bird
			game.bird.draw(g);
		}

		// draws the explosion effect image, wherever there are explosions
		for (Vec2 v: game.explosions.keySet()) {
			if (game.explosions.get(v) > 0) {
				int x = convertWorldXtoScreenX(v.x);
				int y = convertWorldYtoScreenY(v.y);
				int screenRadius = (int) Math.max(convertWorldLengthToScreenLength(0.75f), 1);

				double width = ImageManager.explosion.getWidth(null);
				double height = ImageManager.explosion.getHeight(null);
				AffineTransform transf = new AffineTransform();
				transf.scale(screenRadius * 2 / width, screenRadius * 2 / height);

				AffineTransform reset = g.getTransform();
				g.translate(x, y);
				g.translate(-screenRadius, -screenRadius);
				g.drawImage(ImageManager.explosion, transf, null);
				g.setTransform(reset);
			}
		}

		// This following is commented so that if the barriers want to be seen, this code can be used
//		for (Barrier b : game.barriers)
//			b.draw(g);
	}

	// draws the time and score over the gameplay in a level
	private void drawOverlay(AngryBirdsGame game, Graphics2D g) {
		String score = "Score: " + game.currentScore;
		String time = generateTimer(System.currentTimeMillis() - game.start_time);

		text(10, 42, 46, 4, score, g);
		text(1165, 42, 46, 4, time, g);
	}

	// creates the text for a timer in the correct format
	private String generateTimer(float t) {
		int current_time = (int) t / 1000;
		int seconds = current_time % 60;
		int minutes = (current_time - seconds) / 60;

		String time = minutes + ":" + seconds;
		if (seconds < 10) { // adds a zero so that the timer is in the correct format
			time = minutes + ":0" + seconds;
		}

		return time;
	}

	// draws a GlyphVector that makes the text into shapes so an outline can be drawn on the text
	private void text(int x, int y, int fontSize, float thickness, String text, Graphics2D g) {
		AffineTransform resetTranslation = g.getTransform();
		g.translate(x, y);
		GlyphVector glyphVector = new Font("Trebuchet MS", Font.BOLD, fontSize).createGlyphVector(g.getFontRenderContext(), text);
		Shape textShape = glyphVector.getOutline();
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(thickness));
		g.draw(textShape); // draws the outline
		g.setColor(Color.white);
		g.fill(textShape); // fills the shape
		g.setTransform(resetTranslation);
	}

	// draws the line of the trajectory that the bird will take from the slingshot
	private void drawTrajectoryLine(AngryBirdsGame game, Graphics2D g) {
		if (MouseControls.isMouseButtonPressed()) { // only draws while the user is aiming the bird
			Vec2 start = MouseControls.getWorldCoordinatesOfMousePointerInit();

			if (game.bird.isMouseWithinBird(start)) { // checks whether the mouse coordinates are inside the bird
				Vec2 end = MouseControls.getWorldCoordinatesOfMousePointerCurrent();

				if (end.x != 0 && end.y != 10.625f) { // this stops the line going straight down when the mouse is held on the bird
					Vec2 force = start.sub(end).mul(5f);

					// defines the initial values for the euler update
					Vec2 pos = game.bird.body.getPosition();
					Vec2 vel = force;
					Vec2 acc = new Vec2(0, -GRAVITY);

					ArrayList<Vec2> world_positions = new ArrayList<>();
					// simulated euler updates for trajectory line
					for (int i = 0; i < 71; i++) {
						world_positions.add(new Vec2(convertWorldXtoScreenX(pos.x), convertWorldYtoScreenY(pos.y)));

						Vec2 vel2 = vel.add(acc.mul(DELTA_T));
						Vec2 velAv = vel2.add(vel).mul(0.5f);
						pos = pos.add(velAv.mul(DELTA_T));
						vel = vel.add(acc.mul(DELTA_T));
					}

					// creates a line shape that is dotted
					Path2D line = new Path2D.Float();
					line.moveTo(world_positions.get(0).x, world_positions.get(0).y);
					for (int i = 1; i < world_positions.size(); i++) {
						line.lineTo(world_positions.get(i).x, world_positions.get(i).y);
						i++;
						// this skips a segment of the line so that it is dotted
						line.moveTo(world_positions.get(i).x, world_positions.get(i).y);
					}
					g.setColor(Color.black);
					g.draw(line);
				}
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return FRAME_SIZE;
	}
}