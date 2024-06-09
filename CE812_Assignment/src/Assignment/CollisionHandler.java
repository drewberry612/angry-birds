// RegNo. 2312089

package Assignment;

import Assignment.Characters.Piggie;
import Assignment.Objects.*;
import Assignment.Utilities.SoundManager;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.Random;

public class CollisionHandler implements ContactListener {
    // this class handles all the additional features of collisions in the game
    // must be defined in the correct scope to work

    private AngryBirdsGame game;
    private final float registerSpeed; // the speed at which a proper collision should be detected

    public CollisionHandler(AngryBirdsGame game) {
        this.game = game;
        this.registerSpeed = 3f;
    }

    // finds the object that a body refers to
    public Object findEntity(Body b, AngryBirdsGame game) {
        Object entity = null;
        switch ((String) b.getUserData()) { // uses the userdata to determine which object it is
            case "Bird" -> entity = game.bird; // only one bird at a time
            case "Polygon" -> {
                for (Polygon p: game.polygons) {
                    if (p.body == b) {
                        entity = p;
                        break;
                    }
                }
            }
            case "Piggie" -> {
                for (Piggie p: game.piggies) {
                    if (p.body == b) {
                        entity = p;
                        break;
                    }
                }
            }
            case "Rope" -> { // the body of a rope is defined by its 3 parts, this checks for the part and returns the rope
                for (Rope r: game.ropes) {
                    for (Polygon p: r.parts) {
                        if (p.body == b) {
                            entity = r;
                            break;
                        }
                    }
                }
            }
        }
        return entity;
    }

    // checks that the entity needs its collisions to be handled
    // as stone and barriers can't be destroyed they are ignored
    public boolean check(Object entity) {
        return entity instanceof Glass || entity instanceof Wood || entity instanceof Explosive || entity instanceof Piggie || entity instanceof Rope;
    }

    // handles all collisions done to a wood object
    public void handleWoodCollision(Wood w, Object other) {
        // first if already accommodates for barriers, as they don't move
        if (w.body.getLinearVelocity().length() > registerSpeed) {
            woodDamage(w, other);
        }
        else if (other != null) {
            if (other instanceof Bird) {
                if (((Bird) other).body.getLinearVelocity().length() > registerSpeed) {
                    woodDamage(w, other);
                }
            }
            else if (other instanceof Polygon) {
                if (((Polygon) other).body.getLinearVelocity().length() > registerSpeed) {
                    woodDamage(w, other);
                }
            }
        }
    }

    // makes the wood take damage and plays a sound clip
    private void woodDamage(Wood w, Object other) {
        w.takeDamage();
        if (!w.setForDestruction) {
            SoundManager.playClip(game.sounds.objectsSounds.get("wood_collide"));
        }
        collideSounds(other);
    }

    // handles all collisions done to a glass object
    public void handleGlassCollision(Glass g, Object other) {
        // this should only happen when glass falls, as it can't be moved normally
        if (g.body.getLinearVelocity().length() > registerSpeed) {
            g.setForDestruction = true;
            collideSounds(other);
        }
        else if (other != null) { // if the other is an object, and not a barrier
            if (other instanceof Bird) {
                if (((Bird) other).body.getLinearVelocity().length() > registerSpeed) {
                    g.setForDestruction = true;
                    collideSounds(other);
                }
            }
            else if (other instanceof Polygon) {
                if (((Polygon) other).body.getLinearVelocity().length() > registerSpeed) {
                    g.setForDestruction = true;
                    collideSounds(other);
                }
            }
        }
    }

    // handles all collisions done to an explosive object
    public void handleExplosiveCollision(Explosive e, Object other) {
        // first if is for falling explosives
        if (e.body.getLinearVelocity().length() > registerSpeed) {
            e.setForDestruction = true;
            collideSounds(other);
        }
        else if (other != null) { // if the other is an object, and not a barrier
            if (other instanceof Bird) {
                if (((Bird) other).body.getLinearVelocity().length() > registerSpeed) {
                    e.setForDestruction = true;
                    collideSounds(other);
                }
            }
            else if (other instanceof Polygon) {
                if (((Polygon) other).body.getLinearVelocity().length() > registerSpeed) {
                    e.setForDestruction = true;
                    collideSounds(other);
                }
            }
        }
    }

    // handles all collisions done to a piggie object
    public void handlePiggieCollision(Piggie p, Object other) {
        // first if already accommodates for barriers, as they don't move
        if (p.body.getLinearVelocity().length() > registerSpeed) {
            p.setForDestruction = true;
            collideSounds(other);
        }
        else if (other != null) { // if the other is an object, and not a barrier
            if (other instanceof Bird) {
                if (((Bird) other).body.getLinearVelocity().length() > registerSpeed) {
                    p.setForDestruction = true;
                    collideSounds(other);
                }
            }
            else if (other instanceof Polygon) {
                if (((Polygon) other).body.getLinearVelocity().length() > registerSpeed) {
                    p.setForDestruction = true;
                    collideSounds(other);
                }
            }
        }
        else {
            SoundManager.playClip(game.sounds.piggieSounds.get("piggie_collision"));
        }
    }

    // handles all collisions done to a rope object
    public void handleRopeCollision(Rope r, Object other) {
        // first if already accommodates for barriers, as they don't move
        for (Polygon p: r.parts) {
            if (p.body.getLinearVelocity().length() > registerSpeed) {
                r.setForDestruction = true;
                collideSounds(other);
                break;
            }
            else if (other != null) { // if the other is an object, and not a barrier
                if (other instanceof Bird) {
                    if (((Bird) other).body.getLinearVelocity().length() > registerSpeed) {
                        r.setForDestruction = true;
                        collideSounds(other);
                        break;
                    }
                }
                else if (other instanceof Polygon) {
                    if (((Polygon) other).body.getLinearVelocity().length() > registerSpeed) {
                        r.setForDestruction = true;
                        collideSounds(other);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void beginContact(Contact contact) {
        // this is the method that will occur whenever a collision happens
        AngryBirdsGame game;
        synchronized (this) {
            game = this.game;
        }

        // find the bodies in question
        Object entity1 = findEntity(contact.getFixtureA().getBody(), game);
        Object entity2 = findEntity(contact.getFixtureB().getBody(), game);

        // if any are wood, glass, explosive, rope, or piggie continue, otherwise end
        if (check(entity1) || check(entity2)) {
            handleCollisions(entity1, entity2);
            handleCollisions(entity2, entity1);
        }
    }

    // plays the sound of any bird or stone collision
    private void collideSounds(Object entity) {
        if (entity instanceof Bird) {
            ((Bird) entity).collided = true; // this changes the image of the bird

            switch (new Random().nextInt(3) + 1) { // uses a random sound
                case 1 -> SoundManager.playClip(game.sounds.birdSounds.get("bird_collision1"));
                case 2 -> SoundManager.playClip(game.sounds.birdSounds.get("bird_collision2"));
                case 3 -> SoundManager.playClip(game.sounds.birdSounds.get("bird_collision3"));
            }
        }
        else if (entity instanceof Stone) {
            SoundManager.playClip(game.sounds.objectsSounds.get("rock_collide"));
        }
    }

    // passes the entity into the collision methods so that they don't need to be cast in every reference
    private void handleCollisions(Object entity, Object other) {
        if (entity instanceof Wood) {
            handleWoodCollision((Wood) entity, other);
        }
        else if (entity instanceof Glass) {
            handleGlassCollision((Glass) entity, other);
        }
        else if (entity instanceof Explosive) {
            handleExplosiveCollision((Explosive) entity, other);
        }
        else if (entity instanceof Piggie) {
            handlePiggieCollision((Piggie) entity, other);
        }
        else if (entity instanceof Rope) {
            handleRopeCollision((Rope) entity, other);
        }
    }

    // the following three functions have to be implemented but serve no purpose
    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
