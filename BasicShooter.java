import org.newdawn.slick.Input;

/**
 * Class to store image path and control behaviour of the Basic Shooter
 * @author Ashley
 */
public class BasicShooter extends Enemy{
	private final static String PLAYER_SPRITE_PATH = "res/basic-shooter.png";
	private final int Y_STOP;
	private final static float Y_SPEED = 0.2f;
	private int shot_time_counter = 0;
	private final static int SCORE = 200;


	
	public BasicShooter(int x, int time, int y) {
		super(PLAYER_SPRITE_PATH, x, time);
		//store the randomly generated y-value from instantiation as the Y value to stop at
		this.Y_STOP = y;
	}
	
	/**
	 * update method beings moving sprite downward when its timer reaches 0.
	 * when it is in its final position, the shot timer begins and shoots every 3500 milliseconds
	 */
	public void update(Input input, int delta) {
		if(getY() >= Y_STOP-10) {
			shot_time_counter += delta;
			if(shot_time_counter >= 3500) {
				//create new EnemyLaser sprite at the same position as the Basic Shooter
				World.getInstance().addSprite(new EnemyLaser(getX(), getY()));
				shot_time_counter = 0;
			}
		}
		//starts moving when the delay from waves reaches 0 and stops when it reaches Y_STOP
		if(getDelayTime()<0.1 && getY() <= Y_STOP) {
			move(0, delta*Y_SPEED);
		}
		super.update(input, delta);
	}
	/**
	 * @return the score for killing Basic Shooter
	 */
	public static int getScore() {return SCORE;};

	

}
