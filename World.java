import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.FileReader;
import org.newdawn.slick.Graphics;
//import java.io.IOException;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
/**
 * World class combines all the sprite classes and shield class, updates and renders
 * @author Ashley  with some code taken from SWEN20003 Assignment 1 Solutions: Eleanor McMurtry
 *
 */
public class World {
	private static final String BACKGROUND_PATH = "res/space.png";
	private static final float BACKGROUND_SCROLL_SPEED = 0.2f;
	private float backgroundOffset = 0;
	private Image background;
	private Lives lives = new Lives();
	private static Shield shield = new Shield(Player.getInitX(), Player.getInitY());
	private static World world;
	private static  ArrayList<Sprite> sprites = new ArrayList<>();
	/**
	 * @return instance of World in order to have non-static effect on it
	 */
	public static World getInstance() {
		if (world == null) {
			world = new World();
		}
		return world;
	}
	/**
	 * @return the shield of the game created in World class in order to render separate to sprite
	 */
	public static Shield getShield() {return shield;};
	/**
	 * @return the ArrayList sprites as a getter
	 */
	public static ArrayList<Sprite> getSprites(){return sprites;};
	/**
	 * Method to add a sprite to the ArrayList
	 * @param sprite
	 */
	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}
	
	/**
	 * Method to initialise components of the world like the background and read in the waves.txt file
	 */
	public World() {
		//random variable used for instantiating sine enemy y value
		Random random = new Random();
		//initialise background image
		try {
			background = new Image(BACKGROUND_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		//read in enemy waves file
		try (Scanner scanner = new Scanner(new FileReader("res/waves.txt"))) {
			while(scanner.hasNextLine()) {
				String s[] = new String[3];
				//split each line based on commas
				s = scanner.nextLine().split(",");
				//skip comment lines
				if(s[0].charAt(0)=='#')
					continue;
				//pass x value and time before entering screen value for all enemy
				else if(s[0].equals("BasicEnemy")){
					addSprite(new BasicEnemy(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
				}
				//BasicShooter has additional argument of the random y value
				else if(s[0].equals("BasicShooter")){
					int randomNumber = random.nextInt(464 + 1 - 48) + 48;
					addSprite(new BasicShooter(Integer.parseInt(s[1]), Integer.parseInt(s[2]), randomNumber));
				}
				else if(s[0].equals("SineEnemy")){
					addSprite(new SineEnemy(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
				}
				else {
					addSprite(new Boss(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//add the Player to the ArrayList
		sprites.add(new Player());		
		world = this;
		
	}
	/**
	 * update method that updates all sprites and background and shield
	 * @param input
	 * @param delta
	 */
	public void update(Input input, int delta) {
		//exit game if ESC pressed
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
		//speed up game if S pressed
		if (input.isKeyDown(Input.KEY_S)) {
			delta*=5;
		}
		//update the shield
		shield.update(input, delta);
		
		 
		// Update all sprites
		for (int i = 0; i < sprites.size(); ++i) {
			sprites.get(i).update(input, delta);
		}
		// Handle collisions
		for (Sprite sprite : sprites) {
			for (Sprite other : sprites) {
				if (sprite != other && sprite.getBoundingBox().intersects(other.getBoundingBox())) {
					sprite.contactSprite(other);
				}
			}
		}
		// Clean up inactive sprites
		for (int i = 0; i < sprites.size(); ++i) {
			if (sprites.get(i).getActive() == false) {
				//if the 5% chance of powerup returns true and the
				//enemy is onscreen and the dead sprite is an enemy
				//and the enemy died by bullet and not player colision
				//then coose a powerup
				if(Player.powerUp() && sprites.get(i).getY()<App.SCREEN_HEIGHT && sprites.get(i) instanceof Enemy
						&& Enemy.allowedPuP()) {
					//depending on the 50/50 chance, either powerup could be added to sprties ArrayList
					if(Player.whichPowerUp())
						sprites.add(new ShieldPowerUp(sprites.get(i).getX(), sprites.get(i).getY()));
					else {
						sprites.add(new ShotSpeedPowerUp(sprites.get(i).getX(), sprites.get(i).getY()));
					}
				}
				sprites.remove(i);
				// decrement counter to make sure we don't miss any
				--i;
			}
		} 
		//update the background scroll
		backgroundOffset += BACKGROUND_SCROLL_SPEED * delta;
		backgroundOffset = backgroundOffset % background.getHeight();
	}
	/**
	 * render method to draw all components of the game to the screen
	 * @param g
	 */
	public void render(Graphics g) {
		// Tile the background image
		for (int i = 0; i < App.SCREEN_WIDTH; i += background.getWidth()) {
			for (int j = -background.getHeight() + (int)backgroundOffset; j < App.SCREEN_HEIGHT; j += background.getHeight()) {
				background.draw(i, j);
			}
		}
		// Draw all sprites
		for (Sprite sprite : sprites) {
			sprite.render();
		}
		//draw the shield if it is on
		if(Shield.getOn())
			shield.render();
		//draw the lives icons
		lives.render();
		//draw the current score
		g.drawString(Integer.toString(Player.getScore()), 20, 738);
	}
}
