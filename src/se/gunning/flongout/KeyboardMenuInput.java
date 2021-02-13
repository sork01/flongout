package se.gunning.flongout;

import org.newdawn.slick.Input;

/**
 * Keyboard input mapper with a fixed configuration used for menus.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class KeyboardMenuInput implements InputMapper
{
	/**
	 * Create a new keyboard input mapper for menus.
	 */
	public KeyboardMenuInput()
	{
	}
	
	/**
	 * Populate a controller.
	 * 
	 * @param in Slick2D Input
	 * @param c Controller to populate
	 */
	public void mapInput(Input in, Controller c)
	{
		c.leftAnalog.setMagnitude(0);
		c.rightAnalog.setMagnitude(0);
		
		if (in.isKeyDown(200)) // Uparrow
		{
			c.leftAnalog.setAngle(Math.PI / 2.0);
		}
		else if (in.isKeyDown(203)) // Leftarrow
		{
			c.leftAnalog.setAngle(Math.PI);
		}
		else if (in.isKeyDown(208)) // Downarrow
		{
			c.leftAnalog.setAngle(3.0 * Math.PI / 2.0);
		}
		else if (in.isKeyDown(205)) // Rightarrow
		{
			c.leftAnalog.setAngle(0.0);
		}
		
		c.buttonOne.release();
		c.buttonTwo.release();
		c.buttonThree.release();
		
		if (in.isKeyDown(28)) // RETURN
		{
			c.buttonOne.press();
		}
		
		if (in.isKeyDown(57)) // SPACE
		{
			c.buttonTwo.press();
		}
		
		if (in.isKeyDown(1)) // ESCAPE
		{
			c.buttonThree.press();
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean supportsInputDetection()
	{
		return false;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void beginInputDetection(Input input)
	{
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean detectInput(Input input) throws IllegalStateException
	{
		return false;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void mapDetectedInput(Output output) throws IllegalStateException
	{
	}
}
