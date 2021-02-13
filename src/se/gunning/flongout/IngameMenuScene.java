package se.gunning.flongout;

import java.awt.Font;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;

/**
 * The "in-game" pause menu.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-21
 */
public class IngameMenuScene implements Scene
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
	 * Font used to draw the menu text.
	 */
	private TrueTypeFont menuFont;
	
	/**
	 * Font used to draw the pause "logo" text.
	 */
	private TrueTypeFont pauseFont;
	
	/**
	 * Navigation sound.
	 */
	private Sound hit;
	
	/**
	 * List of menu items.
	 */
	private String[] menuItems;
	
	/**
	 * Selected menu item index.
	 */
	private int selectedMenuItem = 0;
	
	/**
	 * Repeat delay used to prevent processing buttons that are being held
	 * down too fast. The capacity needs to be atleast as big as the number
	 * of available inputs.
	 */
	private long[] repeatDelay = new long[16];
	
	/**
	 * Reference to main game / scene controller.
	 */
	private Main mainGame;
	
	/**
	 * Additional keyboard input to use while in menus. 
	 */
	private InputMapper menuKeyboard;
	
	/**
	 * Phase for color cycling the "PAUSE" text.
	 */
	private double colorCyclePhase;
	
	/**
	 * Create a new "in-game" pause menu.
	 * 
	 * @param main Main game / scene controller
	 */
	public IngameMenuScene(Main main)
	{
		// keep a reference to main for pushing/popping scenes
		mainGame = main;
		menuKeyboard = new KeyboardMenuInput();
		
		menuItems = new String[]{"Resume", "Quit"};
		
		colorCyclePhase = 0.0;
	}
	
	/**
	 * Render the menu.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param g Slick2D Graphics
	 * @throws SlickException when the planets align
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		// draw something a little more fun than just a static "pause" text
		g.setFont(pauseFont);
		
		Stack<String> pauseTextChars = new Stack<String>();
		
		pauseTextChars.push("D");
		pauseTextChars.push("E");
		pauseTextChars.push("S");
		pauseTextChars.push("U");
		pauseTextChars.push("A");
		pauseTextChars.push("P");
		
		int charIndex = 0;
		
		while (!pauseTextChars.isEmpty())
		{
			g.setColor(new Color(
					(float)Math.abs(Math.sin(colorCyclePhase + charIndex * 0.3)),
					(float)Math.abs(Math.sin(colorCyclePhase + charIndex * 0.3 + 2.0)),
					(float)Math.abs(Math.sin(colorCyclePhase + charIndex * 0.3 + 4.0))));
			
			g.drawString(pauseTextChars.pop(),
					coords.toScreenX(-1.5 + charIndex * 0.5 + Math.cos(3.0 * colorCyclePhase + charIndex * 0.3) * 0.2),
					coords.toScreenY(2.0 + Math.cos(5.0 * colorCyclePhase + charIndex * 0.3) * 0.3));
			
			colorCyclePhase += 0.005;
			++charIndex;
		}
		
		// draw the menu items
		g.setFont(menuFont);
		
		for (int i = 0; i < menuItems.length; ++i)
		{
			if (selectedMenuItem == i)
			{
				g.setColor(new Color(1.0f, 1.0f, 1.0f));
			}
			else
			{
				g.setColor(new Color(0.5f, 0.5f, 0.5f));
			}
			
			g.drawString(menuItems[i], coords.toScreenX(-1.5), coords.toScreenY(0.7 - 1.0 * i));
		}
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
		
		// load assets
		try {
			pauseFont = new TrueTypeFont(new Font("monospace", java.awt.Font.BOLD, 40), true);
			menuFont = new TrueTypeFont(new Font("monospace", java.awt.Font.PLAIN, 40), true);
			hit = new Sound(new FileInputStream("assets/sounds/hit.ogg"), "hit.ogg");
		}
		catch (Exception e)
		{
			// can't handle any of the above failing, so exit
			e.printStackTrace();
			System.exit(1);
		}
		
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
			if (repeatDelay[i] < System.currentTimeMillis() && input[i].leftAnalog.getMagnitude() > 0.25)
			{
				// navigate up / down
				if (input[i].leftAnalog.getDirection().y > 0.25)
				{
					--selectedMenuItem;
					hit.play();
				}
				else if (input[i].leftAnalog.getDirection().y < -0.25)
				{
					++selectedMenuItem;
					hit.play();
				}
				
				// keep selected index in range
				if (selectedMenuItem >= menuItems.length)
				{
					selectedMenuItem = 0;
				}
				
				if (selectedMenuItem < 0)
				{
					selectedMenuItem = menuItems.length - 1;
				}
				
				// don't read from this input for 250ms
				repeatDelay[i] = System.currentTimeMillis() + 250;
			}
			else if (!input[i].buttonOne.isPressed() && input[i].leftAnalog.getMagnitude() < 0.25)
			{
				// reset the repeat delay for this input because it's not being held
				repeatDelay[i] = 0;
			}
			
			// read button press and launch other scenes
			if (input[i].buttonOne.isPressed() && repeatDelay[i] < System.currentTimeMillis())
			{
				switch (menuItems[selectedMenuItem])
				{
				case "Resume":
					mainGame.popScene();
					break;
				
				case "Quit":
					mainGame.popScene();
					mainGame.popScene();
					mainGame = null;
					break;
				
				default:
					break;
				}
				
				repeatDelay[i] = System.currentTimeMillis() + 250;
			}
		}
	}
	
	/**
	 * Destroy the scene.
	 */
	public void destroy()
	{
	}
}
