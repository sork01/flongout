package se.gunning.flongout;

import org.newdawn.slick.Input;

public class EscapeKeyInput implements InputMapper
{
	/**
	 * Create a new keyboard input mapper that only reads the escape key.
	 */
	public EscapeKeyInput()
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
