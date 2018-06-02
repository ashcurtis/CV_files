import org.newdawn.slick.Input;
/**
 * Class to control the enemy lasers
 * they are sprites so they have an x and a y and a image path to pass
 * @author Ashley
 */
public class EnemyLaser extends Sprite {
	private final static String ENEMY_SHOT_PATH = "res/enemy-shot.png";
	private final float SHOT_SPEED = 0.7f;
	/**
	 * constructor
	 * @param x
	 * @param y
	 */
	public EnemyLaser(float x, float y) {
		super(ENEMY_SHOT_PATH, x, y);
	}
	/**
	 * method that moves the laser down at constant speed
	 */
	public void update(Input input, int delta) {
		move(0, delta*SHOT_SPEED);
	}
	/**
	 * Method to override the contactSprite
	 * if contact is made with a player, player loses a life
	 */
	@Override
	public void contactSprite(Sprite other) {
		if (other instanceof Player && !Shield.getOn()) {
			World.getShield().turnOnShield();
			Lives.loseLife();
			deactivate();
			if (Lives.getLives()==0) {
				System.exit(0);
			}
		}	
	}

}
