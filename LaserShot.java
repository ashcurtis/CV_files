import org.newdawn.slick.Input;
/**
 * Class for the player's laser
 * @author Ashley
 */
public class LaserShot extends Sprite {
	public final static String SHOT_SPRITE_PATH = "res/shot.png";
	public final float SHOT_SPEED = -3f;
	/**
	 * Constructor to pass the x and y value and image path to super class
	 * @param x
	 * @param y
	 */
	public LaserShot(float x, float y) {
		super(SHOT_SPRITE_PATH, x, y);
	}
	/**
	 * update the position of the laser to move up the screen
	 */
	@Override
	public void update(Input input, int delta) {
		move(0, SHOT_SPEED*delta);
		if (!onScreen()) {
			deactivate();
		}
	}
}
