import org.newdawn.slick.Input;
import java.util.Random;

/**
 * Class that performs behaviour of boss and walks through each step of its behaviour
 * @author Ashley
 */
public class Boss extends Enemy{
	private final static String PLAYER_SPRITE_PATH = "res/boss.png";
	private final static int INIT_Y_STOP = 72;
	private final static float Y_SPEED = 0.05f;
	private final static float X_SPEED = 0.2f;
	private final static float X_SPEED2 = 0.1f;
	private static int timer1 = 0;
	private static int lives = 60;
	private static int x_rand1;
	private final static int SCORE = 5000;	
	private Random random = new Random();
	private int shot_time_counter = 0;
	private int time_shooting = 0;
	private boolean step1 = true;
	private boolean step2 = false;
	private boolean step3 = false;
	private boolean step4 = false;
	private final static int STEP_1_TIME = 5000;
	private final static int STEP_3_TIME = 2000;
	private final static int SHOOTING_TIME = 3000;
	private final static int SHOT_TIME_SEP = 200;
	private final static int MAX_X = 896;
	private final static int MIN_X = 128;
	private final static int X_BUFFER = 2;
	private final static int BULLET_OFFSET1 = 97;
	private final static int BULLET_OFFSET2 = 74;
	/**
	 * @param x takes the x value of initial position
	 * @param time takes its time to wait before entering the screen
	 */
	public Boss(int x, int time) {
		super(PLAYER_SPRITE_PATH, x, time);
	}
	/**
	 * Method to update boss behaviour 
	 * @param input looks for user input
	 * @param delta keeps track of time in game
	 */
	public void update(Input input, int delta) {
		//super update decreases the time before entering the screen
		super.update(input, delta);
		//move the boss to its y position once time to enter is 0
		if(getDelayTime()<0.1 && getY() < INIT_Y_STOP) {
			move(0, delta*Y_SPEED);
		}
		//step 1 is initialised as true but only begins when boss is in y position
		if(step1 && getY()>=INIT_Y_STOP) {
			timer1+=delta;
			//wait 5 seconds
			if(timer1>STEP_1_TIME) {
				//end step 1, start step 2 and reset timer1
				step1=false;
				step2=true;
				timer1=0;
				//generate the first random x for step 2
				x_rand1 = random.nextInt(MAX_X + 1 - MIN_X) + MIN_X;
			}
		}
		//once step 1 is finished, start step 2
		if(step2) {
			/* if the random x is greater than current x, move boss to right
			  and vice versa at the first speed*/
			if(getX()>=x_rand1)
				move(-X_SPEED*delta, 0);
			if(getX()<x_rand1)
				move(X_SPEED*delta, 0);
			//once boss x is at the random x value, end step2 and start step3
			if(getX()>x_rand1-X_BUFFER && getX()<x_rand1+X_BUFFER) {
				step2=false;
				step3=true;
			}
		}
		//start step3 when step2 finished
		if(step3) {
			timer1+=delta;
			//wait 2 seconds, end step3, start step4 and generate another random x, reset timer
			if(timer1>STEP_3_TIME) {
				step3=false;
				step4=true;
				timer1=0;
				x_rand1 = random.nextInt(MAX_X + 1 - MIN_X) + MIN_X;
			}
		}
		//start step4 when step3 finished
		if(step4) {
			/* if the random x is greater than current x, move boss to right
			  and vice versa at the 2nd speed*/
			if(getX()>=x_rand1)
				move(-X_SPEED2*delta, 0);
			if(getX()<x_rand1)
				move(X_SPEED2*delta, 0);
			//keeps track of time since shooting step began
			time_shooting+=delta;
			//keeps track of time between shots
			shot_time_counter+=delta;
			//only shoots if within 3 seconds and every 200 milliseconds
			if(shot_time_counter > SHOT_TIME_SEP && time_shooting<SHOOTING_TIME) {
				World.getInstance().addSprite(new EnemyLaser(getX()-BULLET_OFFSET1, getY()));
				World.getInstance().addSprite(new EnemyLaser(getX()-BULLET_OFFSET2, getY()));
				World.getInstance().addSprite(new EnemyLaser(getX()+BULLET_OFFSET1, getY()));
				World.getInstance().addSprite(new EnemyLaser(getX()+BULLET_OFFSET2, getY()));
				shot_time_counter = 0;
			}
			/*end step4 and start step1 again when x is in the new random x
			 position and been shooting for full 3 seconds*/
			if(getX()>x_rand1-X_BUFFER && getX()<x_rand1+X_BUFFER && time_shooting>SHOOTING_TIME) {
				step4=false;
				step1=true;
				time_shooting=0;
				shot_time_counter=0;
			}
		}	
	}
	/**
	 * method to decrease Boss lives when hit by laser and kill when lives are 0
	 * and update the player's score
	 * calls on super class of contactSprite
	 * @param other. Interaction between Boss and Player's laser
	 */
	@Override
	public void contactSprite(Sprite other) {
		super.contactSprite(other);
		if (other instanceof LaserShot && this.onScreen()) {
			lives--;
			if (lives==0) {
				deactivate();
				Player.setScore(Player.getScore()+SCORE);
			}
			//remove laser from screen
			other.deactivate();
		}
	}
}
