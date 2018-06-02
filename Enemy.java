import org.newdawn.slick.Input;
/**
 * Abstract class that controls the enemies in the game
 * @author Ashley
 *
 */
public abstract class Enemy extends Sprite {
	private float timeOffScreen;
	private static final int INIT_OFF_SCREEN_Y = -64;
	private static boolean allowPowerUp = false;
	/**
	 * Constructor
	 * @param s is the string for the image path
	 * @param x is the initial x in the wave
	 * @param time is time before entering screen
	 */
	public Enemy(String s, int x, int time) {
		super(s, x, INIT_OFF_SCREEN_Y);
		timeOffScreen = time;
	}

	/**
	 * method to deal with enemy (all but Boss) contact with player and player laser
	 * @param other is a sprite which is only affected if it is player or laser
	 */
	@Override
	public void contactSprite(Sprite other) {
		/* Check if the enemy made contact with the player
		 and if the shield is not on, lose a player's life and turn on shield*/
		if (other instanceof Player && !Shield.getOn()) {
			World.getShield().turnOnShield();
			Lives.loseLife();
			deactivate();
			//end game if all lives lost
			if (Lives.getLives()==0) {
				System.exit(0);
			}
		}
		/*if an enemy (but not the boss) makes contact with player
		 remove both the enemy and the laser and update the player's score accordingly*/
		if (other instanceof LaserShot && !(this instanceof Boss)) {
			if (this instanceof BasicEnemy)
				Player.setScore(Player.getScore()+BasicEnemy. getScore());
			if (this instanceof SineEnemy)
				Player.setScore(Player.getScore()+SineEnemy.getScore());
			if (this instanceof BasicShooter)
				Player.setScore(Player.getScore()+BasicShooter.getScore());
			allowPowerUp=true;
			deactivate();
			other.deactivate(); 
		}  
	}
	/**
	 * decrement the time until the enemy enters the screen
	 */
	@Override
	public void update(Input input, int delta) {
		timeOffScreen-=delta;
	}
	/**
	 * @return the time until enemy enters the screen
	 */
	public float getDelayTime() {
		return timeOffScreen;
	}
	/**
	 * getter for allowPowerUp to determine if powerup can be dropped or if enemy died by colliding with Player
	 * @return
	 */
	public static boolean allowedPuP() {return allowPowerUp;};
	
	
}
