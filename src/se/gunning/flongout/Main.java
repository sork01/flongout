package se.gunning.flongout;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Main controller for scenes and inputs. "The thing that runs the other things".
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class Main extends BasicGame
{
	// program entry point
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Main("Flongout!"));
			appgc.setDisplayMode(1024, 768, true);
			appgc.setVSync(true);
			appgc.setTargetFrameRate(60);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(GameScene.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Stack of scenes.
	 */
	public Stack<Scene> sceneStack;
	
	/**
	 * Controllers to pass to scenes.
	 */
	Controller[] controllers;
	
	/**
	 * Slick2D game container.
	 */
	GameContainer gameContainer;
	
	/**
	 * List of available input mappers.
	 */
	private LinkedHashMap<String, InputMapper> availableInputs;
	
	/**
	 * Active input mappers for the two players / paddles.
	 */
	private InputMapper[] playerInput;
	
	/**
	 * Instance of the controller setup scene, kept around to avoid the
	 * fairly complex setup routine.
	 */
	private ControllerSetupScene controllerSetup;
	
	/**
	 * Coordinate transformer.
	 */
	private CoordinateTransformer coords;
	
	/**
	 * Let's go!
	 * 
	 * @param title Window title
	 */
	public Main(String title)
	{
		super(title);
		
		// initialize the scene stack
		sceneStack = new Stack<Scene>();
		
		// initialize two controllers
		controllers = new Controller[]{new Controller(), new Controller()};
	}
	
	/**
	 * Get the controller setup screen scene. This scene gets special treatment to
	 * avoid having to redo the fairly complex setup routine needed for it.
	 * 
	 * @return The controller setup screen scene
	 */
	public ControllerSetupScene getControllerSetupScene()
	{
		return controllerSetup;
	}
	
	/**
	 * Assign an input mapper to a certain player.
	 * 
	 * @param player Player for whom to set an input mapper, 0 is player one, 1 is player two
	 * @param in Input mapper to use
	 * @throws IllegalArgumentException if the requested player index is out of range
	 */
	public void setPlayerInput(int player, InputMapper in)
	{
		if (player < 0 || player > playerInput.length)
		{
			throw new IllegalArgumentException();
		}
		
		playerInput[player] = in;
	}
	
	/**
	 * Get the assigned input mapper for a given player.
	 * 
	 * @param player Player to get input mapper for
	 * @return Input mapper used by the given player
	 * @throws IllegalArgumentException if the requested player index is out of range
	 */
	public InputMapper getPlayerInput(int player)
	{
		if (player < 0 || player > playerInput.length)
		{
			throw new IllegalArgumentException();
		}
		
		return playerInput[player];
	}
	
	/**
	 * Push a scene onto the scene stack, switching to it immediately.
	 * 
	 * @param scene Scene to switch to
	 * @throws SlickException when the planets align
	 */
	public void pushScene(Scene scene) throws SlickException
	{
		// initialize the scene if needed
		if (scene.needsInit())
		{
			scene.init(gameContainer, coords);
		}
		
		sceneStack.push(scene);
		
		// sleep for a bit to avoid re-reading the input that (probably) caused a scene switch
		try { Thread.sleep(200); } catch (InterruptedException e) { }
	}
	
	/**
	 * Pop the active scene off the scene stack, instantly returning to the previous one.
	 */
	public void popScene()
	{
		Scene scene = sceneStack.pop();
		
		// don't destroy the controller setup scene
		if (scene != controllerSetup)
		{
			scene.destroy();
		}
		
		// sleep for a bit to avoid re-reading the input that (probably) caused a scene switch
		try { Thread.sleep(200); } catch (InterruptedException e) { }
	}
	
	/**
	 * Render the current scene.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param g Slick2D Graphics
	 * @throws SlickException when the planets align
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		sceneStack.peek().render(gc, g);
		
		// draw widescreen black bars (if any)
		g.setColor(new Color(0.0f, 0.0f, 0.0f));
		g.fillRect(0, 0, gc.getWidth(), coords.getOffsetY());
		g.fillRect(0, gc.getHeight() - coords.getOffsetY(), gc.getWidth(), coords.getOffsetY());
	}
	
	/**
	 * Initialize self.
	 * 
	 * @param gc Slick2D GameContainer
	 * @throws SlickException when the planets align
	 */
	@Override
	public void init(GameContainer gc) throws SlickException
	{
		// initialize coordinate transformer
		double height = 9.0 * gc.getWidth() / 16.0;
		coords = new CoordinateTransformer(gc.getWidth(), height, 0.0, (gc.getHeight() - height)/2.0, -8.0, 8.0, -4.5, 4.5);
		
		// grab a reference to the game container. hopefully it does not change
		gameContainer = gc;
		
		// assemble the available input mappers
		availableInputs = new LinkedHashMap<String, InputMapper>();
		availableInputs.put("Keyboard 1", new KeyboardInput());
		availableInputs.put("Keyboard 2", new KeyboardInput());
		
		// any gamepads attached?
		for (int i = 0; i < gc.getInput().getControllerCount(); ++i)
		{
			availableInputs.put("Gamepad " + (i + 1), new GamepadInput(i));
		}
		
		// TODO: CPUInput doesn't work too well for left player
		// add two cpu players
		availableInputs.put("CPU 1", new CPUInput());
		availableInputs.put("CPU 2", new CPUInput());
		
		// initialize the controller setup screen scene
		controllerSetup = new ControllerSetupScene(this);
		
		for (String inputName : availableInputs.keySet())
		{
			controllerSetup.addInputMapper(inputName, availableInputs.get(inputName));
		}
		
		playerInput = new InputMapper[]{availableInputs.get("Keyboard 1"), availableInputs.get("CPU 1")};
		controllerSetup.addPlayer("Player One", playerInput[0]);
		controllerSetup.addPlayer("Player Two", playerInput[1]);
		
		// start the main menu
		pushScene(new MainMenuScene(this));
	}
	
	/**
	 * Update the current scene.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param delta Time since last update in milliseconds
	 * @throws SlickException when the planets align
	 */
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
		// get input from mappers onto controllers
		playerInput[0].mapInput(gc.getInput(), controllers[0]);
		playerInput[1].mapInput(gc.getInput(), controllers[1]);
		
		// update the scene
		sceneStack.peek().update(gc, delta, controllers);
	}
}
