import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.BoundingBox;
/**
 * Sprite class is abstract class for almost all moving objects
 * it stores the x and y values and generates boundingboxes
 * @author Ashley with some code taken from SWEN20003 Assignment 1 Solutions: Eleanor McMurtry
 */
public abstract class Sprite {
	private Image image = null;
	private float x;
	private float y;
	private BoundingBox bb;
	private boolean active = true;
	private final static int HALF_PLAYER_SIZE = 32;
	/**
	 * constructor initialises the image, x and y values
	 * and based on these values, a bounding box is created around the image
	 * @param imageSrc
	 * @param x
	 * @param y
	 */
	public Sprite(String imageSrc, float x, float y) {
		try {
			image = new Image(imageSrc);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		this.x = x;
		this.y = y;
		
		bb = new BoundingBox(image, x, y);
	}
	
	/**
	 * Returns true whenever the sprite is on screen.
	 */
	public boolean onScreen() {
		return x >= 0 && x <= App.SCREEN_WIDTH - bb.getWidth()
			&& y >= 0 && y <= App.SCREEN_HEIGHT - bb.getHeight();
	}
	
	/**
	 *  Forces the sprite to remain on the screen
	 */
	public void clampToScreen() {
		x = Math.max(x, HALF_PLAYER_SIZE);
		x = Math.min(x, App.SCREEN_WIDTH-HALF_PLAYER_SIZE);
		y = Math.max(y, HALF_PLAYER_SIZE);
		y = Math.min(y, App.SCREEN_HEIGHT-HALF_PLAYER_SIZE);
	}
	/**
	 * update method takes input from player and time elapsed, delta
	 * @param input
	 * @param delta
	 */
	public void update(Input input, int delta) {
	}
	
	/**
	 * renders sprites to screen
	 */
	public void render() {
		image.drawCentered(x, y);
	}
	
	/**
	 * Called whenever this Sprite makes contact with another.
	 */
	public void contactSprite(Sprite other) {
		
	}
	/**
	 * @return x as a getter
	 */
	public float getX() { return x; }
	/**
	 * @return y as a getter
	 */
	public float getY() { return y; }
	/**
	 * @param dx used to set x
	 */
	public void setX(float dx) {this.x = dx;};
	/**
	 * @param dy used to set y
	 */
	public void setY(float dy) {this.y = dy;};
	/**
	 * @return whether the sprite is active
	 */
	public boolean getActive() { return active; }
	/**
	 * set active boolean to false
	 */
	public void deactivate() { active = false; }
	/**
	 * @return bounding box for that particular sprite
	 */
	public BoundingBox getBoundingBox() {
		return new BoundingBox(bb);
	}
	/**
	 * changes x and y valus and boudingbox accordingly
	 * @param dx
	 * @param dy
	 */
	public void move(float dx, float dy) {
		x += dx;
		y += dy;
		bb.setX(x);
		bb.setY(y);
	}
	/**
	 * overloaded method specifically for sine enemy
	 * @param dx
	 * @param dy
	 * @param pivot
	 */
	public void move(float dx, float dy, int pivot) {
		x = dx + pivot;
		y += dy;
		bb.setX(x);
		bb.setY(y);
	}
}
