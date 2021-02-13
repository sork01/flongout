package se.gunning.flongout;

import java.io.FileInputStream;
import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * The main menu for the game.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-31
 */
public class ControlsTutorialScene implements Scene
{
	/**
	 * Is the scene initialized?
	 */
	private boolean initialized = false;
	
	/**
	 * Coordinate transformer.
	 */
	private CoordinateTransformer coords;
	
	/**
	 * Background image.
	 */
	private Image back;
	
	/**
	 * Reference to main game / scene controller.
	 */
	private Main mainGame;
	
	/**
	 * Additional keyboard input to use while in menus. 
	 */
	private InputMapper menuKeyboard;
	
	/**
	 * Game to launch after having shown the tutorial.
	 */
	private GameScene game;
	
	/**
	 * Time to automatically proceed.
	 */
	private long autoProceedTimeout;
	
	/**
	 * Create a new controls tutorial scene.
	 * 
	 * @param main Main game / scene controller
	 * @param game Game to launch after the tutorial
	 */
	public ControlsTutorialScene(Main main, GameScene game)
	{
		// keep a reference to main for pushing/popping scenes
		mainGame = main;
		this.game = game;
		menuKeyboard = new KeyboardMenuInput();
	}
	
	/**
	 * Render the scene.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param g Slick2D Graphics
	 * @throws SlickException when the planets align
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		// draw the background image
		back.draw(coords.toScreenX(-8.0), coords.toScreenY(4.5));
	}
	
	/**
	 * Does this scene need to be initialized?
	 * 
	 * @return True if this scene needs to be initialized, false otherwise.
	 */
	public boolean needsInit()
	{
		return !initialized;
	}
	
	/**
	 * Initialize the scene.
	 * 
	 * @param gc Slick2D GameContainer
	 * @throws SlickException when the planets align
	 */
	@Override
	public void init(GameContainer gc, CoordinateTransformer ct) throws SlickException
	{
		coords = ct;
		
		// scale factor for scaling the graphics
		float scalefactor = gc.getWidth() / 1920f;
		
		// load assets
		try {
			back = new Image(new FileInputStream("assets/images/controls.png"), "controls", false);
			back = back.getScaledCopy(scalefactor);
		}
		catch (Exception e)
		{
			// can't handle any of the above failing, so exit
			e.printStackTrace();
			System.exit(1);
		}
		
		autoProceedTimeout = System.currentTimeMillis() + 6000;
		
		initialized = true;
	}
	
	/**
	 * Update the scene by reading and responding to inputs.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param delta Time since last update in milliseconds
	 * @param input Controllers to read input from
	 * @throws SlickException when the planets align
	 */
	public void update(GameContainer gc, int delta, Controller[] input) throws SlickException
	{
		// extend the array of inputs and add the menu keyboard input mapper
		input = Arrays.copyOf(input, input.length + 1);
		input[input.length - 1] = new Controller();
		menuKeyboard.mapInput(gc.getInput(), input[input.length - 1]);
		
		// read all inputs
		for (int i = 0; i < input.length; ++i)
		{
			if (input[i].buttonOne.isPressed() || input[i].buttonTwo.isPressed() || input[i].buttonThree.isPressed())
			{
				proceedToGame();
			}
		}
		
		if (System.currentTimeMillis() >= autoProceedTimeout)
		{
			proceedToGame();
		}
	}
	
	/**
	 * End the tutorial and launch the game.
	 * @throws SlickException when the planets align
	 */
	private void proceedToGame() throws SlickException
	{
		mainGame.popScene();
		mainGame.pushScene(game);
		mainGame = null;
		return;
	}
	
	/**
	 * Destroy the scene.
	 */
	public void destroy()
	{
	}
}
