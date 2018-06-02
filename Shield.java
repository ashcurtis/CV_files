import org.newdawn.slick.Input;
/**
 *class to keep track of the shield itself, when to be active and when to render
 * @author Ashley
 */
public class Shield extends Sprite {
	private final static String SHIELD_PATH = "res/Shield.png";
	private int timer = 0;
	private int timer2 = 1;
	private static final int SHIELD_ST_TIME = 2300;
	private static final int POWERUP_TIME = 5000;
	private static boolean finished_init = false;
	private static boolean pUpOn = true;
	/**
	 * constructor
	 * @param x
	 * @param y
	 */
	public Shield(float x, float y) {
		super(SHIELD_PATH, x, y);
	}
	/**
	 * method to update the shield at the beginning of the game and each time it is activated
	 */
	@Override
	public void update(Input input, int delta) {
		//turns the shield on at beginning and turns off after some time
		if (timer2>0) {
			timer2+=delta;
			pUpOn=true;
		} if(timer2>SHIELD_ST_TIME) {
			pUpOn=false;
			timer2=0;
			finished_init=true;
		}
		
		
		//when shield is activated, timer begins
		if(pUpOn && finished_init) {
			timer+=delta;
		}
		//shield turns off after set time
		if(timer > POWERUP_TIME) {
			pUpOn = false;
			timer = 0;
		}		
		//update shields position based on player's location
		for (Sprite sprite : World.getSprites()) {
			if (sprite instanceof Player) {
				this.setX(sprite.getX());
				this.setY(sprite.getY());
			}
		}
	}
	/**
	 * method to activate the shield
	 */
	public void turnOnShield() {
		//reset shield timer
		timer=0;
		pUpOn = true;
	}
	/**
	 * @return whether the shield power up is on or not
	 */
	public static boolean getOn() {return pUpOn;};
}
