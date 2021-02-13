package se.gunning.flongout;

import java.awt.Font;
import java.io.FileInputStream;
import java.util.Arrays;

import org.lwjgl.openal.AL;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;

/**
 * The main menu for the game.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-31
 */
public class MainMenuScene implements Scene
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
	 * Background image.
	 */
	private Image back;
	
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
	 * Launch the game with the background animation enabled?
	 */
	boolean backgroundAnimationEnabled;
	
	/**
	 * Available game ending mechanics.
	 */
	private GameEndingMechanic[] gameEnders;
	
	/**
	 * Names assigned to game ending mechanics.
	 */
	private String gameEnderNames[];
	
	/**
	 * Currently selected game ending mechanic.
	 */
	private int selectedGameEnder;
	
	/**
	 * Create a new main menu scene.
	 * 
	 * @param main Main game / scene controller
	 */
	public MainMenuScene(Main main)
	{
		// keep a reference to main for pushing/popping scenes
		mainGame = main;
		backgroundAnimationEnabled = true;
		menuKeyboard = new KeyboardMenuInput();
		
		gameEnders = new GameEndingMechanic[]
				{ new EndlessGame(),
				  new TimedGame(60),
				  new TimedGame(300),
				  new ScoreLimitedGame(10),
				  new ScoreLimitedGame(50)};
		
		gameEnderNames = new String[]
				{ "Endless",
				  "One minute",
				  "Five minutes",
				  "First to 10",
				  "First to 50"};
		
		selectedGameEnder = 0;
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
		// draw the background image
		back.draw(coords.toScreenX(-8.0), coords.toScreenY(4.5));
		menuItems = new String[]{"Start Game", "Mode: " + gameEnderNames[selectedGameEnder], "Background: " + (backgroundAnimationEnabled ? "On" : "Off"), "Controller Setup", "Quit"};
		
		// draw the menu items
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
			
	
			g.setFont(menuFont);
			g.drawString(menuItems[i], coords.toScreenX(-2.5), coords.toScreenY(2.0 - 1.0 * i));
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
		
		// scale factor for scaling the graphics
		float scalefactor = gc.getWidth() / 1920f;
		
		// load assets
		try {
			menuFont = new TrueTypeFont(new Font("monospace", java.awt.Font.PLAIN, 40), true);
			hit = new Sound(new FileInputStream("assets/sounds/hit.ogg"), "hit.ogg");
			
			back = new Image(new FileInputStream("assets/images/back.png"), "back", false);
			back = back.getScaledCopy(scalefactor);
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
				case "Start Game":
					mainGame.pushScene(new ControlsTutorialScene(mainGame, new GameScene(mainGame, backgroundAnimationEnabled, gameEnders[selectedGameEnder])));
					break;
				
				case "Mode: Endless":
				case "Mode: One minute":
				case "Mode: Five minutes":
				case "Mode: First to 10":
					++selectedGameEnder;
					break;
				
				case "Mode: First to 50":
					selectedGameEnder = 0;
					break;
				
				case "Controller Setup":
					mainGame.pushScene(mainGame.getControllerSetupScene());
					break;
				
				case "Background: On":
				case "Background: Off":
					backgroundAnimationEnabled = !backgroundAnimationEnabled;
					break;
				
				case "Quit":
					AL.destroy();
					System.exit(0);
				
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
		mainGame = null;
	}
}
