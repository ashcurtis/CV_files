import org.newdawn.slick.Input;
/**
 * class for the shotspeed powerup itself
 * @author Ashley
 */
public class ShotSpeedPowerUp extends Sprite {
	private final static String PATH = "res/shotspeed-powerup.png";
	private final static float Y_SPEED = 0.1f;
	/**
	 * constructor to pass dead enemy position and image path to super sprite constructor
	 * @param x
	 * @param y
	 */
	public ShotSpeedPowerUp(float x, float y) {
		super(PATH, x, y);
	}
	/**
	 * method to update the icon position down the screen
	 */
	@Override
	public void update(Input input, int delta) {
		move(0, Y_SPEED*delta);		
	}
	
	//public static boolean getOn() {return pUpOn;};

}