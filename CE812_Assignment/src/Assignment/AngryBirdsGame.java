// RegNo. 2312089
// Adapted from code by Michael Fairbank

package Assignment;

import Assignment.Characters.*;
import Assignment.Objects.*;
import Assignment.Utilities.ImageManager;
import Assignment.Utilities.KeyControls;
import Assignment.Utilities.MouseControls;
import Assignment.Utilities.SoundManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static Assignment.Utilities.Constants.*;

public class AngryBirdsGame {
	// this class controls the whole flow of the game

	// these store all entities in the current level
	public Bird bird;
	public List<Polygon> polygons;
	public List<Barrier> barriers;
	public List<Piggie> piggies;
	public List<Rope> ropes;

	// these define the state and flow of the whole game
	// determining which menu/level you are on, and where to go next
	public LayoutMode layout;
	public Map<LayoutMode, LayoutMode> flow;

	// these are the attributes that are used to display the scores and times achieved
	public Map<LayoutMode, Integer> times;
	public long start_time;
	public Map<LayoutMode, Integer> scores;
	public int currentScore;


	public Map<Vec2, Integer> explosions; // holds the number of frames an explosion should be shown for

	// hold the images that are used in the game
	public ImageManager images;
	public BufferedImage currentBG;
	public BufferedImage currentDirt;
	public AffineTransform bgTransform; // scales the background to the screen
	public AffineTransform dirtTransform; // scales the dirt/slingshot overlay to the screen

	public SoundManager sounds; // holds all the sounds used in the game

	// defines the LayoutMode
	public enum LayoutMode {LEVEL_1, LEVEL_2, LEVEL_3, MENU_1, MENU_2, MENU_3, MENU_4}

    public static World world; // Box2D container for all bodies and barriers

	// initialises important data structures
    public AngryBirdsGame() {
		times = new HashMap<>();
		scores = new HashMap<>();
		layout = LayoutMode.MENU_1; // this can be changed to set the start layout

		flow = new HashMap<>();
		flow.put(LayoutMode.MENU_1, LayoutMode.LEVEL_1);
		flow.put(LayoutMode.LEVEL_1, LayoutMode.MENU_2);
		flow.put(LayoutMode.MENU_2, LayoutMode.LEVEL_2);
		flow.put(LayoutMode.LEVEL_2, LayoutMode.MENU_3);
		flow.put(LayoutMode.MENU_3, LayoutMode.LEVEL_3);
		flow.put(LayoutMode.LEVEL_3, LayoutMode.MENU_4);

		images = new ImageManager(); // loads all images
		sounds = new SoundManager(); // loads all sounds

		reset();
	}

	// assigns the current background and its transformation
	private void assignBackground() {
		currentBG = ImageManager.backgrounds.get(layout);
		currentDirt = ImageManager.dirt.get(layout);

		// defines a transformation
		double width = currentBG.getWidth(null);
		double height = currentBG.getHeight(null);
		bgTransform = new AffineTransform();
		bgTransform.scale(SCREEN_WIDTH / width, SCREEN_HEIGHT / height);

		if (currentDirt != null) { // only does this when in a level, not a menu
			width = currentDirt.getWidth(null);
			height = currentDirt.getHeight(null);
			dirtTransform = new AffineTransform();
			dirtTransform.scale(SCREEN_WIDTH / width, SCREEN_HEIGHT / height);
		}
	}

	// resets all data structures and sets all the objects or attributes needed for a new menu/level
	private void reset() {
		assignBackground();

		bird = null;
		polygons = new ArrayList<>();
		barriers = new ArrayList<>();
		piggies = new ArrayList<>();
		ropes = new ArrayList<>();
		explosions = new HashMap<>();

		// this if holds all that will be shown in a level, a menu doesn't need these
		if (layout == LayoutMode.LEVEL_1 || layout == LayoutMode.LEVEL_2 || layout == LayoutMode.LEVEL_3) {
			currentScore = 0;
			start_time = System.currentTimeMillis();

			// creates a new world
			world = new World(new Vec2(0, -GRAVITY));
			world.setContinuousPhysics(true);

			newBird();

			// World boundaries
			barriers.add(new Barrier(0, 0, WORLD_WIDTH, 0));
			barriers.add(new Barrier(0, 0, 0, WORLD_HEIGHT));
			barriers.add(new Barrier(WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT));

			// Slingshot
			barriers.add(new Barrier(1, 0, 1, 2));
			barriers.add(new Barrier(2, 0, 2, 2));
			barriers.add(new Barrier(1, 2, 2, 2));

			// adds all the entities for a given level
			switch (layout) {
				case LEVEL_1 -> {
					// first building
					polygons.add(new Stone(7.5f, 2.25f, 0.25f, 6, NINETY_DEGREES));

					polygons.add(new Wood(6.75f, 1f, 0.25f, 4, 0));
					polygons.add(new Wood(8.25f, 1f, 0.25f, 4, 0));

					polygons.add(new Glass(6.75f, 3.5f, 4, 0.25f, 0));
					polygons.add(new Glass(8.25f, 3.5f, 4, 0.25f, 0));

					polygons.add(new Wood(7.5f, 5.15f, 2, 3, 0.65f)); // triangle

					piggies.add(new Piggie(7.5f, 2.9f, SMALL_PIGGIE));

					// second building
					polygons.add(new Stone(11f, 0.5f, 0.5f, 2, NINETY_DEGREES));
					polygons.add(new Stone(13f, 0.5f, 0.5f, 2, NINETY_DEGREES));
					polygons.add(new Stone(15f, 0.5f, 0.5f, 2, NINETY_DEGREES));

					polygons.add(new Glass(10.5f, 2.5f, 6, 0.25f, 0));
					polygons.add(new Glass(15.5f, 2.5f, 6, 0.25f, 0));

					polygons.add(new Wood(13f, 4.25f, 0.25f, 12, NINETY_DEGREES));

					polygons.add(new Stone(13f, 5.5f, 2, 2, 4, 1f, 0));

					piggies.add(new Piggie(11.5f, 1.4f, SMALL_PIGGIE));
					piggies.add(new Piggie(14.5f, 1.4f, SMALL_PIGGIE));
					piggies.add(new Piggie(13f, 1.6f, BIG_PIGGIE));

					// other
					polygons.add(new Wood(17.25f, 0.75f, 1, 1, 1, 0.75f, NINETY_DEGREES));
					polygons.add(new Wood(18.75f, 0.75f, 1, 1, 1, 0.75f, 0));

					piggies.add(new Piggie(18f, 1.9f, SMALL_PIGGIE));
				}
				case LEVEL_2 -> {
					// first hill
					barriers.add(new Barrier(6, 0, 8, 3));
					barriers.add(new Barrier(8, 3, 13, 3));
					barriers.add(new Barrier(13, 3, 15, 0));

					// second hill
					barriers.add(new Barrier(17, 0, 17, 5));
					barriers.add(new Barrier(17, 5, WORLD_WIDTH, 5));

					// building
					polygons.add(new Stone(10.5f, 3.25f, 0.25f, 14, NINETY_DEGREES));

					polygons.add(new Glass(8f, 4.75f, 5, 0.25f, 0));
					polygons.add(new Glass(13f, 4.75f, 5, 0.25f, 0));

					polygons.add(new Wood(10.5f, 4f, 1, 4, 0.5f));

					polygons.add(new Wood(10.5f, 6.25f, 0.25f, 14, NINETY_DEGREES));

					polygons.add(new Glass(9f, 7.25f, 3, 0.25f, 0));
					polygons.add(new Glass(12f, 7.25f, 3, 0.25f, 0));

					polygons.add(new Wood(10.5f, 8.25f, 0.25f, 10, NINETY_DEGREES));

					polygons.add(new Wood(10.5f, 8.75f, 2, 3, 0.25f));

					piggies.add(new Piggie(10.5f, 5.1f, BIG_PIGGIE));
					piggies.add(new Piggie(10.5f, 6.9f, SMALL_PIGGIE));
					piggies.add(new Piggie(8.5f, 3.9f, SMALL_PIGGIE));
					piggies.add(new Piggie(12.5f, 3.9f, SMALL_PIGGIE));

					// other
					polygons.add(new Stone(17.5f, 5.5f, 1, 4, 0.5f));
					polygons.add(new Stone(19.2f, 5.5f, 1, 4, 0.5f));
					polygons.add(new Wood(18f, 7.25f, 1, 3, 3, 0.75f, ONE_EIGHTY_DEGREES));

					piggies.add(new Piggie(16f, 4.5f, SMALL_PIGGIE));

					ropes.add(new Rope((Wood) polygons.get(polygons.size()-1), piggies.get(piggies.size()-1), new Vec2(-1.75f, 0), new Vec2(1.75f, 0)));
				}
				case LEVEL_3 -> {
					// left hill
					barriers.add(new Barrier(6, 0, 6, 3));
					barriers.add(new Barrier(6, 3, 9, 3));
					barriers.add(new Barrier(9, 3, 9, 0));

					// right hill
					barriers.add(new Barrier(17, 0, 17, 3));
					barriers.add(new Barrier(17, 3, WORLD_WIDTH, 3));

					// building foundation
					polygons.add(new Explosive(11.5f, 0.5f, 0.5f));
					polygons.add(new Explosive(14.5f, 0.5f, 0.5f));

					polygons.add(new Wood(11.5f, 2.5f, 2, 3, 3, 0.5f, ONE_EIGHTY_DEGREES));
					polygons.add(new Wood(14.5f, 2.5f, 2, 3, 3, 0.5f, ONE_EIGHTY_DEGREES));

					polygons.add(new Wood(10f, 3.25f, 0.25f, 6, NINETY_DEGREES));
					polygons.add(new Wood(13f, 3.25f, 0.25f, 6, NINETY_DEGREES));
					polygons.add(new Wood(16f, 3.25f, 0.25f, 6, NINETY_DEGREES));

					piggies.add(new Piggie(13f, BIG_PIGGIE, BIG_PIGGIE));
					piggies.add(new Piggie(10f, SMALL_PIGGIE, SMALL_PIGGIE));
					piggies.add(new Piggie(16f, SMALL_PIGGIE, SMALL_PIGGIE));

					// building
					polygons.add(new Glass(9f, 5f, 6, 0.25f, 0));
					polygons.add(new Glass(17f, 5f, 6, 0.25f, 0));

					polygons.add(new Stone(10f, 4f, 1, 4, 0.5f));
					polygons.add(new Stone(16f, 4f, 1, 4, 0.5f));
					polygons.add(new Explosive(13f, 4f, 0.5f));
					polygons.add(new Stone(13f, 5f, 1, 4, 0.5f));

					polygons.add(new Wood(13f, 6.75f, 0.25f, 18, NINETY_DEGREES));

					// ropes
					piggies.add(new Piggie(11.5f, 5f, SMALL_PIGGIE));
					ropes.add(new Rope((Wood) polygons.get(polygons.size()-1), piggies.get(piggies.size()-1), new Vec2(-1.5f, -0.25f), new Vec2(-0.25f, 1.5f)));

					piggies.add(new Piggie(14.5f, 5f, SMALL_PIGGIE));
					ropes.add(new Rope((Wood) polygons.get(polygons.size()-1), piggies.get(piggies.size()-1), new Vec2(1.5f, -0.25f), new Vec2(-0.25f, -1.5f)));

					// roof
					polygons.add(new Wood(10f, 7.5f, 2, 3, 0.5f));
					polygons.add(new Wood(16f, 7.5f, 2, 3, 0.5f));

					piggies.add(new Piggie(12f, 7.4f, SMALL_PIGGIE));
					piggies.add(new Piggie(14f, 7.4f, SMALL_PIGGIE));
				}
			}
		}

		// resets the mouse so the bird isn't fired
		MouseControls.setMouseButtonReleased(false);
	}

	// saves the time and score after a level is complete
	private void afterLevel() {
		scores.put(layout, currentScore);
		times.put(layout, (int) (System.currentTimeMillis() - start_time));
	}

	// the main method of the game
	// it defines the game object, the window, and anything inside the window
	public static void main(String[] args) {
		final AngryBirdsGame game = new AngryBirdsGame();
		final View view = new View(game);
		Window w = new Window(view, "Angry Birds");

		w.addKeyListener(new KeyControls());

		// there are two to allow for all methods to work
		view.addMouseListener(new MouseControls());
		view.addMouseMotionListener(new MouseControls());

		game.gameLoop(view); // starts the game
	}

	// this is the workings of a menu
	private void menu(LayoutMode current, View view) {
		while (layout == current) { // this loop keeps going until layout changes
			frame(view);
			if (KeyControls.isKeyPressed()) { // moves to the next level when enter is pressed
				layout = flow.get(current);
				KeyControls.setKeyPressed(false);
			}
		}
	}

	// this is the workings of a level
	private void level(LayoutMode current, View view) {
		while (layout == current) { // this loop keeps going until layout changes
			update(view);
			frame(view);
		}
	}

	// this handles the game loop of the whole game
	private void gameLoop(final View view) {
		sounds.loopMusic(); // play the theme music on loop

		menu(LayoutMode.MENU_1, view);
		reset();

		level(LayoutMode.LEVEL_1, view);
		reset();

		menu(LayoutMode.MENU_2, view);
		reset();

		level(LayoutMode.LEVEL_2, view);
		reset();

		menu(LayoutMode.MENU_3, view);
		reset();

		level(LayoutMode.LEVEL_3, view);
		reset();

		menu(LayoutMode.MENU_4, view);
		System.exit(0); // quit the game when enter is pressed
	}

	// this is the repaint for a single frame of the game
	private void frame(View view) {
		view.repaint();
		Toolkit.getDefaultToolkit().sync();
		try {
			Thread.sleep(DELAY);
		} catch (InterruptedException ignored) {
		}
	}

	// handles the updates of all entities in the world
	private void update(View view) {
		world.setContactListener(new CollisionHandler(this)); // must be defined here in the same scope as world.step

		if (bird == null) { // when there is no bird, generate a new one
			newBird();
		}

		destroyEntities();
		slingBird();
		levelWinConditions(view);

		world.step(DELTA_T, EULER_UPDATES_PER_FRAME, EULER_UPDATES_PER_FRAME); // advances the world by a timestep
	}

	// handles the destruction and updates of all entities
	private void destroyEntities() {
        explosions.replaceAll((k, v) -> v - 1); // acts as a timer for the explosion effect image

		if (bird != null) { // when there is a bird, update it
			bird.timestepUpdate();
			if (bird.setForDestruction) { // destroy the bird and play a sound
				SoundManager.playClip(sounds.birdSounds.get("bird_destroy"));
				currentScore -= bird.score;
				world.destroyBody(bird.body);
				bird = null;
			}
		}

		// searches for piggies to destroy
		Iterator<Piggie> itr1 = piggies.iterator();
		while (itr1.hasNext()) {
			Piggie p = itr1.next();
			p.timestepUpdate();
			if (p.setForDestruction) {
				SoundManager.playClip(sounds.piggieSounds.get("piggie_destroy"));
				currentScore += p.score;
				itr1.remove();
				world.destroyBody(p.body);
			}
		}

		// searches for ropes to destroy
		Iterator<Rope> itr2 = ropes.iterator();
		while (itr2.hasNext()) {
			Rope r = itr2.next();
			for (Polygon p: r.parts) {
				p.timestepUpdate();
			}
			if (r.setForDestruction) {
				for (Polygon p: r.parts) {
					world.destroyBody(p.body);
				}
				itr2.remove();
			}
		}

		// searches for polygons to destroy
		Iterator<Polygon> itr3 = polygons.iterator();
		while (itr3.hasNext()) {
			Polygon p = itr3.next();
			p.timestepUpdate();
			if (p.setForDestruction) {
				if (p instanceof Glass) {
					SoundManager.playClip(sounds.objectsSounds.get("glass_destroy"));
				}
				else if (p instanceof Wood) {
					SoundManager.playClip(sounds.objectsSounds.get("wood_destroy"));
				}
				else if (p instanceof Explosive) {
					explosions.put(p.body.getPosition(), 10);
					SoundManager.playClip(sounds.objectsSounds.get("tnt_explode"));
					explosiveEffect((Explosive) p); // when an explosive is destroyed, it applies a force to nearby objects
				}
				currentScore += p.score;
				itr3.remove();
				world.destroyBody(p.body);
			}
		}
	}

	private void slingBird() {
		if (MouseControls.isMouseButtonReleased()) { // when the mouse is released
			Vec2 start = MouseControls.getWorldCoordinatesOfMousePointerInit();
			if (bird.isMouseWithinBird(start)) { // was the mouse within the bird when clicked
				Vec2 end = MouseControls.getWorldCoordinatesOfMousePointerFinal();
				Vec2 force = start.sub(end).mul(5f); // this 5 is just a scale factor that was tuned

				// makes the bird fly
				bird.body.setType(BodyType.DYNAMIC);
				bird.body.setLinearVelocity(force);

				SoundManager.playClip(sounds.otherSounds.get("sling"));

				// plays a sound based on current bird type
				if (bird instanceof Red) {
					randomBirdSound("bird1");
				}
				else if (bird instanceof Blue) {
					randomBirdSound("bird2");
				}
				else if (bird instanceof Black) {
					randomBirdSound("bird3");
				}
				else if (bird instanceof Yellow) {
					randomBirdSound("bird4");
				}
			}
			// resets the mouse
			MouseControls.setMouseButtonReleased(false);
		}
	}

	// plays a sound randomly from 2 sounds
	private void randomBirdSound(String bird) {
		if (new Random().nextInt(2) == 0) {
			SoundManager.playClip(sounds.birdSounds.get(bird+"a"));
		}
		else {
			SoundManager.playClip(sounds.birdSounds.get(bird+"b"));
		}
	}

	// handles the completion of a level
	private void levelWinConditions(View view) {
		if (piggies.isEmpty()) { // the level is complete when there are no piggies left
			afterLevel(); // saves the time and score

			for (int i=0; i<50; i++) { // advances the world forward 50 updates for smoothness
				world.setContactListener(new CollisionHandler(this));

				// another bird cannot be slung

				destroyEntities(); // entities can still be destroyed during this time
				world.step(DELTA_T, EULER_UPDATES_PER_FRAME, EULER_UPDATES_PER_FRAME);
				frame(view);
			}

			// changes the level to the next menu
			layout = flow.get(layout);

			SoundManager.playClip(sounds.otherSounds.get("level_clear"));
		}
	}

	// checks all objects to see whether they should be influenced by the explosion
	private void explosiveEffect(Explosive explosive) {
		for (Polygon p: polygons) {
			// the following finds the distance from the explosive to the object
			float length = p.body.getPosition().sub(explosive.body.getPosition()).length();
			if (p != explosive && length < EXPLOSION_RADIUS) { // if the distance is within the blast radius
				if (p instanceof Glass || p instanceof Explosive) { // glass and explosives can't be pushed, they are just destroyed
					p.setForDestruction = true;
				}
				else if (p instanceof Wood) {
					((Wood) p).takeDamage(); // wood takes damage from explosions
					if (!p.setForDestruction) { // if the wood hasn't been destroyed, apply the force
						applyForce(explosive, p, length);
					}
				}
				else { // this is for stone
					applyForce(explosive, p, length);
				}
			}
		}

		for (Rope r: ropes) {
			for (Polygon p: r.parts) {
				float length = p.body.getPosition().sub(explosive.body.getPosition()).length();
				if (length < EXPLOSION_RADIUS) {
					r.setForDestruction = true;
					break; // when a part of a rope is destroyed, the whole thing is destroyed
				}
			}
		}

		for (Piggie p: piggies) { // piggies can't bne pushed, they are just destroyed
			float length = p.body.getPosition().sub(explosive.body.getPosition()).length();
			if (length < EXPLOSION_RADIUS) {
				p.setForDestruction = true;
			}
		}

		if (bird != null) {
			float length = bird.body.getPosition().sub(explosive.body.getPosition()).length();
			if (length < EXPLOSION_RADIUS) { // this is an adaptation of apply force but for Birds
				Vec2 normal = bird.body.getPosition().sub(explosive.body.getPosition());
				normal.normalize();
				float explosionFactor = EXPLOSION_POWER / length;
				bird.body.applyForceToCenter(normal.mul(explosionFactor));
			}
		}
	}

	// applies the explosion force to an object
	private void applyForce(Explosive explosive, Polygon entity, float length) {
		Vec2 normal = entity.body.getPosition().sub(explosive.body.getPosition()); // direction of the force
		normal.normalize();
		float explosionFactor = EXPLOSION_POWER / length; // objects further away are pushed less
		entity.body.applyForceToCenter(normal.mul(explosionFactor));
	}

	// assigns a new bird randomly
	private void newBird() {
		switch (new Random().nextInt(4) + 1) {
			case 1 -> bird = new Red();
			case 2 -> bird = new Blue();
			case 3 -> bird = new Yellow();
			case 4 -> bird = new Black();
		}
	}
}


