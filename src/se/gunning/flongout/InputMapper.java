package se.gunning.flongout;

import org.newdawn.slick.Input;

/**
 * Interface for various input methods / mappers.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public interface InputMapper
{
	/**
	 * Enumeration of available outputs.
	 * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-19
	 */
	public enum Output
	{
		LEFTANALOG_X_MINUS("Move Paddle Left"),
		LEFTANALOG_X_PLUS("Move Paddle Right"),
		LEFTANALOG_Y_PLUS("Move Paddle Up"),
		LEFTANALOG_Y_MINUS("Move Paddle Down"),
		RIGHTANALOG_X_MINUS("Spin Paddle Left"),
		RIGHTANALOG_X_PLUS("Spin Paddle Right"),
		RIGHTANALOG_Y_PLUS("Spin Paddle Up"),
		RIGHTANALOG_Y_MINUS("Spin Paddle Down"),
		BUTTON1("Quick Spin Up"),
		BUTTON2("Quick Spin Down"),
		BUTTON3("Back / Quit"),
		NONE("None");
		
		/**
		 * Readable name of the output.
		 */
		private String name;
		
		private Output(String name)
		{
			this.name = name;
		}
		
		/**
		 * Get a human-readable name for the output.
		 * 
		 * @return A readable name
		 */
		public String getName()
		{
			return name;
		}
	}
	
	/**
	 * Read the given input and populate the given controller with
	 * appropriate settings according to some input/output map.
	 *  
	 * @param input Slick2D Input to read
	 * @param c Controller to populate
	 */
	public void mapInput(Input input, Controller c);
	
	/**
	 * Determine whether or not this InputMapper supports input detection.
	 * 
	 * @return True if the InputMapper supports input detection, false otherwise.
	 */
	public boolean supportsInputDetection();
	
	/**
	 * Start input detection.
	 * 
	 * @param input Slick2D input
	 */
	public void beginInputDetection(Input input);
	
	/**
	 * Continue input detection.
	 * 
	 * @param input Slick2D input
	 * @return True if a single input has been detected with confidence, false otherwise.
	 * @throws IllegalStateException if called before beginInputDetection
	 */
	public boolean detectInput(Input input) throws IllegalStateException;
	
	/**
	 * Finish input detection, binding the detected input to a given output.
	 * 
	 * @param output Output to bind to
	 * @throws IllegalStateException if called before beginInputDetection
	 */
	public void mapDetectedInput(Output output) throws IllegalStateException;
}
