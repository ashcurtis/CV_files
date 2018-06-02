import org.newdawn.slick.Input;
/**
 * class for the sine enemy of the game
 * holds its behaviour and attributes
 * @author Ashley
 */
public class SineEnemy extends Enemy{
	private final static String PLAYER_SPRITE_PATH = "res/sine-enemy.png";
	private final static int AMPLITUDE = 96;
	private final static int PERIOD = 1500;
	private final static float Y_SPEED = 0.15f;
	private final int CENTRE_X;
	private float t = 0;
	private final static int SCORE = 100;

	/**
	 * constructor
	 * @param x is passed to sprite super constructor
	 * @param time until enter screen passed to sprite super constructor
	 */
	public SineEnemy(int x, int time) {
		super(PLAYER_SPRITE_PATH, x, time);
		//CENTRE_X is stored as the anchor where the sine wave pivots around
		this.CENTRE_X = x;
	}
	/**
	 * update method to update postion of the sine enemy
	 * begins to move when time until enter screen is 0
	 */
	@Override
	public void update(Input input, int delta) {
		if(getDelayTime()<0.1) {
			//move takes the change to previous x and y value and the pivot point
			move(offset(t), delta*Y_SPEED, CENTRE_X);
			t+=delta;
		}
		//decrements time until enter screen
		super.update(input, delta);
	}
	/**
	 * @param t
	 * @return the offset of the enemy based on the sine rule and time since entered screen
	 */
	public float offset(float t) {
		float dx = (float)(AMPLITUDE*Math.sin(((2*Math.PI)/PERIOD)*t));
		return dx;
	}
	/**
	 * @return the score when Sine Enemy killed
	 */
	public static int getScore() {return SCORE;};
	

}
