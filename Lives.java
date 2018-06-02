import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * class to store the lives of the player
 * @author Ashley
 */
public class Lives {
	private final static String  LIVES_PATH = "res/lives.png";
	private static Image image = null;
	private final static float X = 20;
	private final static float Y = 696;
	private static int n_lives = 3;
	private final static int SEPARATION = 40;
	/**
	 * constructor to initiate the image
	 */
	public Lives() {
		try {
			image = new Image(LIVES_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	/**
	 * method to render each remaining life to screen
	 */
	public void render() {
		for(int i=0; i<n_lives; i++) {
			image.drawCentered(X + i*SEPARATION, Y);
		}
	}
	/**
	 * method to lose a life
	 */
	public static void loseLife() {n_lives--;};
	/**
	 * @return remaining lives.
	 */
	public static int getLives() {return n_lives;};
}