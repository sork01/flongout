package se.gunning.flongout;

import org.newdawn.slick.Input;

/**
 * An input mapper that does precisely nothing.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class NullInput implements InputMapper
{
	/**
	 * Don't populate the given controller.
	 */
	@Override
	public void mapInput(Input input, Controller c)
	{
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
