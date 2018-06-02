import org.newdawn.slick.Input;
import java.util.Random;
/**
 * Class to organise behaviour of the player
 * @author Ashley
 */
public class Player extends Sprite {
	private final static String PLAYER_SPRITE_PATH = "res/spaceship.png";
	private final static int PLAYER_INITIAL_X = 512;
	private final static int PLAYER_INITIAL_Y = 688;
	private final static int POWERUP_SHOT_DELAY = 150;
	private final static int NORMAL_SHOT_DELAY = 350;
	private final static int POWERUP_DURATION = 5000;
	private final static int NUMBER_POWERUP = 2;
	private final static int NUMBER_FOR_PERCENT = 100;
	private final static int THIS_PERCENT = 5;
	private static final boolean SHIELD = true;
	private static final boolean SHOT_SPEED = false;
	private final static float SPEED = 0.5f;	
	private   static Random random = new Random();
	private static int timer = 0;
	private static boolean pUpOn = false;
	private int shot_timer = 0;
	private static int score = 0;
	/**
	 * Constructor that passes the initial position to sprite super class and the image path to initialise
	 */
	public Player() {
		super(PLAYER_SPRITE_PATH, PLAYER_INITIAL_X, PLAYER_INITIAL_Y);
	}

	/**
	 * method to update the player according to input and speed
	 * method also implements the shot speed power up
	 */
	@Override
	public void update(Input input, int delta) {
		doMovement(input, delta, SPEED);
		/*shoot a player laser when the space bar pressed and
		enough time has elapsed depending on power up still present or not*/
		if (input.isKeyPressed(Input.KEY_SPACE) && shot_timer<1) {
			World.getInstance().addSprite(new LaserShot(getX(), getY()));
			//reset and begin shot timer
			shot_timer = 0;
			shot_timer += delta;
		}
		//begin shot timer after shot fired
		if(shot_timer > 0)
			shot_timer += delta;
		//reset shot timer sooner when power up is on
		if (pUpOn) {
			if(shot_timer > POWERUP_SHOT_DELAY) {
				shot_timer = 0;
			}
		}
		//reset shot time later otherwise
		else {
			if(shot_timer > NORMAL_SHOT_DELAY) {
				shot_timer = 0;
			}
		}
		//begin powerup timer when timer is set off
		if(timer>0)
			timer+=delta;
		//turn off powerup after certrain time
		if(timer > POWERUP_DURATION) {
			pUpOn = false;
			timer = 0;
		}
	}
	/**
	 * method to take the input and delta and pass variables on to move method to update position	
	 * @param input
	 * @param delta
	 * @param s
	 */
	private void doMovement(Input input, int delta, float s) {
		// handle horizontal movement
		float dx = 0;
		if (input.isKeyDown(Input.KEY_LEFT)) {
			dx -= s;
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			dx += s;
		}

		// handle vertical movement
		float dy = 0;
		if (input.isKeyDown(Input.KEY_UP)) {
			dy -= s;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			dy += s;
		}
		
		move(dx * delta, dy * delta);
		clampToScreen();
	}
	
	/**
	 * getter for the player's score
	 * @return
	 */
	public static int getScore() {return score;};
	/**
	 * setter to set the score
	 * @param s
	 */
	public static void setScore(int s) {score = s;};
	/**
	 * @return the starting x value as a getter
	 */
	public static float getInitX() {return PLAYER_INITIAL_X;};
	/**
	 * @return the starting y as a getter
	 */
	public static float getInitY() {return PLAYER_INITIAL_Y;};
	/**
	 * method to deal with player catching powerups.
	 */
	@Override
	public void contactSprite(Sprite other) {
		//if contact is made with the powerup,
		//stop rendering powerup image and call to begin powerup
		if (other instanceof ShotSpeedPowerUp) {
			other.deactivate();
			powerUpSpeed();
		}
		//if contact is made with the powerup,
		//turn the shield on
		if (other instanceof ShieldPowerUp) {
			other.deactivate();
			World.getShield().turnOnShield();
		}
	}
	/**
	 * turn the powerup on and prime the timer
	 */
	public void powerUpSpeed() {
		timer =1;
		pUpOn = true;
			
	}
	
	/**
	 * @return true if randomly generated number between 1 and 100
	 * are the numbers 1, 2, 3, 4, 5 hence 5% chance
	 */
	public static boolean powerUp() {
		//random number between 1 and 100
		int rand = random.nextInt(NUMBER_FOR_PERCENT) + 1;
		//5% chance
		if(rand >=1 && rand <= THIS_PERCENT) 
			return true;
		else return false;
	}
	/**
	 * @return which powerup gets dropped based on randomly generated number between 0 and 1
	 * 50% chance
	 */
	public static boolean whichPowerUp() {
		//random number between 0 and 1 for 50% chance 
		int which_powerup = random.nextInt(NUMBER_POWERUP);
		if(which_powerup==0)
			return SHIELD;
		else return SHOT_SPEED;
	}

}
