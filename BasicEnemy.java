import org.newdawn.slick.Input;

/**
 * Class to store image path and control behaviour of this Basic Enemy
 * @author Ashley
 */
public class BasicEnemy extends Enemy{
	private final static String PLAYER_SPRITE_PATH = "res/basic-enemy.png";
	private final static float Y_SPEED = 0.2f;
	private final static int SCORE = 50;
	
	public BasicEnemy(int x, int time) {
		super(PLAYER_SPRITE_PATH, x, time);
		
	}
	/**
	 * Update method scrolls BasicEnemy downscreen at constant speed when its set time to start moving reaches 0	 * 
	 */
	public void update(Input input, int delta) {
		super.update(input, delta);
		if(getDelayTime()<0.1) {
			move(0, Y_SPEED * delta);
		}
	}
	/**
	 * @return the score associated with killing this enemy
	 */
	public static int getScore() {return SCORE;};
}
