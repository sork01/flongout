package se.gunning.flongout;

import java.awt.Font;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;

/**
 * Controller settings screen. Includes input method selection
 * and input detection / remapping.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class ControllerSetupScene implements Scene
{
	/**
	 * Internal helper class for associating an InputMapper with a name.
	 * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-19
	 */
	private static class NamedInputMapper
	{
		/**
		 * Name given to the mapper.
		 */
		public String name;
		
		/**
		 * The actual mapper.
		 */
		public InputMapper mapper;
		
		/**
		 * Create a new named InputMapper.
		 * 
		 * @param name Name to assign
		 * @param mapper Actual mapper
		 */
		public NamedInputMapper(String name, InputMapper mapper)
		{
			this.name = name;
			this.mapper = mapper;
		}
	}
	
	/**
	 * Internal helper class for player entries in the menu.
	 * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-19
	 */
	private static class PlayerMenuEntry
	{
		/**
		 * Name/title of the player.
		 */
		public String name;
		
		/**
		 * Current named InputMapper used by this player.
		 */
		public NamedInputMapper namedMapper;
		
		/**
		 * Index of the named InputMapper.
		 */
		public int inputIndex;
		
		/**
		 * Create a new player menu entry.
		 * 
		 * @param name Name for the player
		 * @param mapper Named InputMapper in use by the player
		 * @param inputIndex Index of named InputMapper
		 */
		public PlayerMenuEntry(String name, NamedInputMapper mapper, int inputIndex)
		{
			this.name = name;
			this.namedMapper = mapper;
			this.inputIndex = inputIndex;
		}
	}
	
	/**
	 * Is the scene initialized?
	 */
	private boolean initialized = false;
	
	/**
	 * Reference to main game / scene controller.
	 */
	private Main mainGame;
	
	/**
	 * Additional keyboard input to use while in menus. 
	 */
	private InputMapper menuKeyboard;
	
	/**
	 * Player entries in the menu.
	 */
	private ArrayList<PlayerMenuEntry> players;
	
	/**
	 * Available input mappers.
	 */
	private ArrayList<NamedInputMapper> inputMappers;
	
	/**
	 * Font used for the menu text.
	 */
	private TrueTypeFont menuFont, helpFont;
	
	/**
	 * Index of selected menu item.
	 */
	private int selectedMenuItem;
	
	/**
	 * Coordinate transformer.
	 */
	private CoordinateTransformer coords;
	
	/**
	 * Repeat delay used to prevent processing buttons that are being held
	 * down too fast. The capacity needs to be atleast as big as the number
	 * of available inputs.
	 */
	private long[] repeatDelay = new long[16];
	
	/**
	 * Are we currently doing input detection / remapping?
	 */
	private boolean detectingInput = false;
	
	/**
	 * The mapper currently used for input detection (if any, can be null).
	 */
	private NamedInputMapper mapperDetectingInput;
	
	/**
	 * Stack of outputs needing inputs mapped.
	 */
	private Stack<InputMapper.Output> inputDetectionStack;
	
	/**
	 * Current output to map.
	 */
	private InputMapper.Output outputBeingDetected;
	
	/**
	 * Navigation sound.
	 */
	private Sound hit;
	
	/**
	 * Create a new controller settings screen.
	 * 
	 * @param main Main game / scene controller
	 */
	public ControllerSetupScene(Main main)
	{
		mainGame = main;
		menuKeyboard = new KeyboardMenuInput();
		players = new ArrayList<PlayerMenuEntry>();
		inputMappers = new ArrayList<NamedInputMapper>();
	}
	
	/**
	 * Add an input mapper.
	 * 
	 * @param name Name to use for the mapper
	 * @param mapper The actual mapper
	 */
	public void addInputMapper(String name, InputMapper mapper)
	{
		inputMappers.add(new NamedInputMapper(name, mapper));
	}
	
	/**
	 * Add a player entry to the menu.
	 * 
	 * @param name Name for the player
	 * @param mapper Mapper currently used by the player
	 * @throws IllegalStateException if the given mapper hasn't been added with addInputMapper
	 */
	public void addPlayer(String name, InputMapper mapper)
	{
		NamedInputMapper namedMapper = null;
		
		int nth = 0;
		
		// find the mapper index. this is pretty ugly.
		for (NamedInputMapper nim : inputMappers)
		{
			if (nim.mapper == mapper)
			{
				namedMapper = nim;
				break;
			}
			
			++nth;
		}
		
		if (namedMapper == null)
		{
			throw new IllegalStateException("Must addInputMapper before addPlayer");
		}
		
		players.add(new PlayerMenuEntry(name, namedMapper, nth));
	}
	
	/**
	 * Initialize the scene.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param ct Coordinate transformer
	 */
	@Override
	public void init(GameContainer gc, CoordinateTransformer ct) throws SlickException
	{
		coords = ct;
		
        try
        {
    		menuFont = new TrueTypeFont(new Font("monospace", java.awt.Font.PLAIN, 40), true);
    		helpFont = new TrueTypeFont(new Font("monospace", java.awt.Font.PLAIN, 18), true);
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
	 * Does the scene need to be initialized?
	 * 
	 * @return True if the scene needs to be initialized, false otherwise.
	 */
	@Override
	public boolean needsInit()
	{
		return !initialized;
	}
	
	/**
	 * Update the scene.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param delta Time since last update in milliseconds
	 * @param input Controllers to read input from
	 */
	@Override
	public void update(GameContainer gc, int delta, Controller[] input) throws SlickException
	{
		// extend the array of inputs and add the menu keyboard input mapper
		input = Arrays.copyOf(input, input.length + 1);
		input[input.length - 1] = new Controller();
		menuKeyboard.mapInput(gc.getInput(), input[input.length - 1]);
		
		// not detecting input, so do normal menu stuff
		if (!detectingInput)
		{
			// number of menu items = players + back item
			int numMenuItems = players.size() + 1;
			
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
					if (selectedMenuItem >= numMenuItems)
					{
						selectedMenuItem = 0;
					}
					
					if (selectedMenuItem < 0)
					{
						selectedMenuItem = numMenuItems - 1;
					}
					
					// navigate left / right (select input mappers)
					if (selectedMenuItem < players.size())
					{
						PlayerMenuEntry p = players.get(selectedMenuItem);
						
						if (input[i].leftAnalog.getDirection().x > 0.25)
						{
							++p.inputIndex;
							hit.play();
						}
						else if (input[i].leftAnalog.getDirection().x < -0.25)
						{
							--p.inputIndex;
							hit.play();
						}
						
						if (p.inputIndex >= inputMappers.size())
						{
							p.inputIndex = 0;
						}
						else if (p.inputIndex < 0)
						{
							p.inputIndex = inputMappers.size() - 1;
						}
						
						p.namedMapper = inputMappers.get(p.inputIndex);
						
						mainGame.setPlayerInput(selectedMenuItem, p.namedMapper.mapper);
					}
					
					// don't read from this input for 250ms
					repeatDelay[i] = System.currentTimeMillis() + 250;
				}
				else if (input[i].leftAnalog.getMagnitude() < 0.25)
				{
					// reset the repeat delay for this input because it's not being held
					repeatDelay[i] = 0;
				}
				
				// pressed button one?
				if (input[i].buttonOne.isPressed())
				{
					// pressed button one on a player entry, do input detection / remapping
					if (selectedMenuItem < players.size())
					{
						// only do input detection if the selected mapper supports it
						if (players.get(selectedMenuItem).namedMapper.mapper.supportsInputDetection())
						{
							// set up the stack of output that need mappings
							InputMapper.Output[] outputs = InputMapper.Output.values();
							inputDetectionStack = new Stack<InputMapper.Output>();
							
							for (int j = 0; j < outputs.length; ++j)
							{
								if (!outputs[j].equals(InputMapper.Output.NONE))
								{
									inputDetectionStack.push(outputs[j]);
								}
							}
							
							Collections.reverse(inputDetectionStack);
							
							// start input detection
							mapperDetectingInput = players.get(selectedMenuItem).namedMapper;
							mapperDetectingInput.mapper.beginInputDetection(gc.getInput());
							outputBeingDetected = inputDetectionStack.pop();
							detectingInput = true;
							
							// sleep a bit to avoid instantly detecting the very same input
							// that was used to trigger input detection
							try { Thread.sleep(200); } catch (InterruptedException e) {}
						}
					}
					else if (selectedMenuItem == numMenuItems - 1)
					{
						// go back to main menu
						selectedMenuItem = 0;
						mainGame.popScene();
						return;
					}
				}
			}
		}
		else if (detectingInput)
		{
			// has the mapper detected an input?
			if (mapperDetectingInput.mapper.detectInput(gc.getInput()))
			{
				// map the detected input
				mapperDetectingInput.mapper.mapDetectedInput(outputBeingDetected);
				
				// are there more outputs to map?
				if (!inputDetectionStack.empty())
				{
					outputBeingDetected = inputDetectionStack.pop();
					mapperDetectingInput.mapper.beginInputDetection(gc.getInput());
				}
				else
				{
					// done with input detection
					detectingInput = false;
					
					// sleep a bit to let the player release any controls before
					// showing the menu again to avoid unwanted navigation
					try { Thread.sleep(200); } catch (InterruptedException e) {}
				}
			}
		}
	}
	
	/**
	 * Render the scene.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param g Slick2D Graphics
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		// not detecting input? draw the menu
		if (!detectingInput)
		{
			int currentMenuItem = 0;
			
			// draw helptext
			drawHelpItem(g);
			
			// draw all player entries
			for (PlayerMenuEntry p : players)
			{
				drawMenuItem(g, p.name + ": " + p.namedMapper.name, currentMenuItem++, menuFont);
			}
			
			// draw the "back button"
			drawMenuItem(g, "<- Back", currentMenuItem++, menuFont);
		}
		else
		{
			// detecting input. draw the name of mapper being used and which output is being requested.
			drawMenuItem(g, "Configuring " + mapperDetectingInput.name, 0, menuFont, new Color(1.0f, 1.0f, 0.0f));
			drawMenuItem(g, "Press / Move controls for \"" + outputBeingDetected.getName() + "\"", 1, helpFont, new Color(1.0f, 1.0f, 1.0f));
		}
	}
	
	/**
	 * Internal helper method for drawing menu items.
	 * 
	 * @param g Slick2D Graphics
	 * @param text String to draw
	 * @param index Index of item to draw
	 */
	private void drawMenuItem(Graphics g, String text, int index, TrueTypeFont font)
	{
		// draw the selected item in bright colors, unselected items in dim colors
		if (index == selectedMenuItem)
		{
			g.setColor(new Color(1.0f, 1.0f, 1.0f));
		}
		else
		{
			g.setColor(new Color(0.5f, 0.5f, 0.5f));
		}
		
		// draw the text
		g.setFont(font);
		g.drawString(text, coords.toScreenX(-4.0), coords.toScreenY(2.0 - 1.0 * index));
	}
	
	/**
	 * Internal helper method for drawing menu items with a given color.
	 * 
	 * @param g Slick2D Graphics
	 * @param text String to draw
	 * @param index Index of item to draw
	 */
	private void drawMenuItem(Graphics g, String text, int index, TrueTypeFont font, Color color)
	{
		// draw the text
		g.setFont(font);
		g.setColor(color);
		g.drawString(text, coords.toScreenX(-4.0), coords.toScreenY(2.0 - 1.0 * index));
	}
	
	/**
	 * Internal helper method for drawing helptext.
	 * 
	 * @param g Slick2D Graphics
	 */
	private void drawHelpItem(Graphics g)
	{
		String text = "";
		
		if (selectedMenuItem == 0 || selectedMenuItem == 1)
		{
			text = "LEFT/RIGHT to select control method, ENTER to configure";
		}
		else
		{
			text = "Back to main menu";
		}
		
		// draw the text
		g.setFont(helpFont);
		g.setColor(new Color(1.0f, 1.0f, 1.0f));
		g.drawString(text, coords.toScreenX(-4.0), coords.toScreenY(3.0));
	}
	
	/**
	 * Destroy the scene.
	 */
	@Override
	public void destroy()
	{
		mainGame = null;
	}
}
