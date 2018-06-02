import org.newdawn.slick.Input;
/**
 * @author Ashley
 *class for the shield power up itself
 */
public class ShieldPowerUp extends Sprite {
	private final static String PATH = "res/shield-powerup.png";
	private final static float Y_SPEED = 0.1f;
	/**
	 * constructor to pass the dead enemy's location to super sprite constructor
	 * as well as the image path
	 * @param x
	 * @param y
	 */
	public ShieldPowerUp(float x, float y) {
		super(PATH, x, y);
	}
	/**
	 * method to move the powerup image down the screen
	 */
	@Override
	public void update(Input input, int delta) {
		move(0, Y_SPEED*delta);
		
	}
	
	

}