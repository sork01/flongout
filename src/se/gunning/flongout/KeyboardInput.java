package se.gunning.flongout;

import java.util.HashMap;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

/**
 * Keyboard-only input mapper.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class KeyboardInput implements InputMapper, KeyListener
{
	/**
	 * Are we currently running input detection?
	 */
	private boolean detectingInput = false;
	
	/**
	 * Key detected during input detection.
	 */
	private int detectedKey;
	
	/**
	 * Our Input-Output keymap.
	 */
	private HashMap<Integer, Output> keyMap;
	
	/**
	 * Create a new keyboard-only input mapper.
	 */
	public KeyboardInput()
	{
		// set up a default configuration
		keyMap = new HashMap<Integer, Output>();
		
		keyMap.put(17, Output.LEFTANALOG_Y_PLUS); // W
		keyMap.put(30, Output.LEFTANALOG_X_MINUS); // A
		keyMap.put(31, Output.LEFTANALOG_Y_MINUS); // S
		keyMap.put(32, Output.LEFTANALOG_X_PLUS); // D
		
		keyMap.put(200, Output.RIGHTANALOG_Y_PLUS); // Uparrow
		keyMap.put(203, Output.RIGHTANALOG_X_MINUS); // Leftarrow
		keyMap.put(208, Output.RIGHTANALOG_Y_MINUS); // Downarrow
		keyMap.put(205, Output.RIGHTANALOG_X_PLUS); // Rightarrow
		
		keyMap.put(35, Output.BUTTON1); // H
		keyMap.put(36, Output.BUTTON2); // J
		keyMap.put(37, Output.BUTTON3); // K
	}
	
	/**
	 * Populate a controller.
	 * 
	 * @param in Slick2D Input
	 * @param c Controller to populate
	 */
	public void mapInput(Input in, Controller c)
	{
		Vec2 leftAnalog = new Vec2(0, 0);
		Vec2 rightAnalog = new Vec2(0, 0);
		
		Vec2 xplus = new Vec2(1.0, 0.0);
		Vec2 xminus = new Vec2(-1.0, 0.0);
		Vec2 yplus = new Vec2(0.0, 1.0);
		Vec2 yminus = new Vec2(0.0, -1.0);
		
		c.buttonOne.release();
		c.buttonTwo.release();
		c.buttonThree.release();
		
		for (Integer key : keyMap.keySet())
		{
			if (in.isKeyDown(key))
			{
				switch (keyMap.get(key))
				{
				case LEFTANALOG_Y_PLUS:
					leftAnalog = leftAnalog.add(yplus);
					break;
				case LEFTANALOG_Y_MINUS:
					leftAnalog = leftAnalog.add(yminus);
					break;
				case LEFTANALOG_X_PLUS:
					leftAnalog = leftAnalog.add(xplus);
					break;
				case LEFTANALOG_X_MINUS:
					leftAnalog = leftAnalog.add(xminus);
					break;
				case RIGHTANALOG_Y_PLUS:
					rightAnalog = rightAnalog.add(yplus);
					break;
				case RIGHTANALOG_Y_MINUS:
					rightAnalog = rightAnalog.add(yminus);
					break;
				case RIGHTANALOG_X_PLUS:
					rightAnalog = rightAnalog.add(xplus);
					break;
				case RIGHTANALOG_X_MINUS:
					rightAnalog = rightAnalog.add(xminus);
					break;
				case BUTTON1:
					c.buttonOne.press();
					break;
				case BUTTON2:
					c.buttonTwo.press();
					break;
				case BUTTON3:
					c.buttonThree.press();
					break;
				default:
					break;
				}
			}
		}
		
		c.leftAnalog.setDirection(leftAnalog.normalize());
		c.rightAnalog.setDirection(rightAnalog.normalize());
	}
	
	/**
	 * We support input detection.
	 */
	@Override
	public boolean supportsInputDetection()
	{
		return true;
	}
	
	/**
	 * Begin input detection.
	 * 
	 * @param input Slick2D Input
	 */
	@Override
	public void beginInputDetection(Input input)
	{
		// we'll catch the input acting as a Slick2D KeyListener
		input.addKeyListener(this);
		detectingInput = true;
		detectedKey = -1;
	}
	
	/**
	 * Continue input detection.
	 * 
	 * @param input Slick2D Input
	 * @throws IllegalStateException if called without having first called beginInputDetection
	 */
	@Override
	public boolean detectInput(Input input) throws IllegalStateException
	{
		if (!detectingInput)
		{
			throw new IllegalStateException("Must call beginInputDetection before detectInput");
		}
		
		// detected a key already?
		if (detectedKey != -1)
		{
			// stop listening
			input.removeKeyListener(this);
			
			// return with confidence
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Listen to keypresses for input detection.
	 * 
	 * @param key Key code
	 * @param c Character typed
	 */
	@Override
	public void keyPressed(int key, char c)
	{
		detectedKey = key;
	}
	
	/**
	 * Map the detected input to a given output.
	 * 
	 * @param output Output to map
	 * @throws IllegalStateException if called without having first called beginInputDetection
	 */
	@Override
	public void mapDetectedInput(Output output) throws IllegalStateException
	{
		// get rid of the current mapping for the given output
		keyMap.values().remove(output);
		
		// insert the new one
		keyMap.put(detectedKey, output);
		
		detectingInput = false;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void setInput(Input input)
	{
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isAcceptingInput()
	{
		return true;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void inputEnded()
	{
	}

	@Override
	public void inputStarted()
	{
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void keyReleased(int key, char c)
	{
	}
}
