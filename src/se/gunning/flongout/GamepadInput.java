package se.gunning.flongout;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.Input;

/**
 * Input handler / mapper for gamepads.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class GamepadInput implements InputMapper
{
	/**
	 * Internal helper class for Input-Output mappings.
	 * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-19
	 */
    private static class Mapping
    {
    	/**
    	 * Enumeration of types of input.
    	 * 
		 * @author Mikael Forsberg
		 * @author Robin Gunning
		 * @author Jonathan Yao Håkansson
		 * @version 2015-05-19
    	 */
        public enum InputType
        {
            AXIS_PLUS, AXIS_MINUS, BUTTON
        }
        
        /**
         * Type of input.
         */
        private InputType inputType;
        
        /**
         * Slick2D / LWJGL ID of input.
         */
        private int inputId;
        
        /**
         * Mapped output.
         */
        private Output output;
        
        /**
         * Create a new mapping.
         * 
         * @param type Type of input
         * @param id Slick2D / LWJGL ID of input
         * @param out Output
         */
        public Mapping(InputType type, int id, Output out)
        {
            inputType = type;
            inputId = id;
            output = out;
        }
        
        /**
         * Get the input type of this mapping.
         * 
         * @return Input type
         */
        public InputType getInputType()
        {
        	return inputType;
        }
        
        /**
         * Get the Slick2D / LWJGL input ID of this mapping.
         * 
         * @returnSlick2D / LWJGL input ID
         */
        public int getInputId()
        {
        	return inputId;
        }
        
        /**
         * Set the output for this mapping.
         * 
         * @param out Output to set
         */
        @SuppressWarnings("unused")
		public void setOutput(Output out)
        {
        	output = out;
        }
        
        /**
         * Get the output of this mapping.
         * 
         * @return Output of the mapping
         */
        public Output getOutput()
        {
        	return output;
        }
    }
    
    /**
     * Internal helper class for accumulating changes to some variable.
     * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-19
     */
    private static class DifferenceAccumulator implements Comparable<DifferenceAccumulator>
    {
    	/**
    	 * Current accumulated sum.
    	 */
        public double sum;
        
        /**
         * Previous value given to update().
         */
        public double previousValue;
        
        /**
         * Create a new difference accumulator.
         * 
         * @param initialValue Value to start with
         */
        public DifferenceAccumulator(double initialValue)
        {
            previousValue = initialValue;
        }
        
        /**
         * Update the accumulator.
         *  
         * @param newValue New value
         */
        public void update(double newValue)
        {
            sum += Math.abs(previousValue - newValue);
            previousValue = newValue;
        }
        
        /**
         * Compare this accumulator to another one.
         * 
         * @return 1 if this accumulator has a larger sum than the other, -1 otherwise.
         */
        public int compareTo(DifferenceAccumulator other)
        {
        	return new Double(sum).compareTo(new Double(other.sum));
        }
    }
    
    /**
     * Internal helper class for input detection.
     * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-19
     */
    private static class InputDetectionEntry implements Comparable<InputDetectionEntry>
    {
    	/**
    	 * The mapping of this entry.
    	 */
    	public Mapping map;
    	
    	/**
    	 * Difference accumulator for the mapping.
    	 */
    	public DifferenceAccumulator acc;
    	
    	/**
    	 * Create a new input detection entry.
    	 * 
    	 * @param mapping The mapping that determines which input to look at. 
    	 */
    	public InputDetectionEntry(Mapping mapping)
    	{
    		map = mapping;
    		acc = new DifferenceAccumulator(0.0);
    	}
    	
    	/**
    	 * Update this entry.
    	 * 
    	 * @param newValue New value of input
    	 */
    	public void update(double newValue)
    	{
    		acc.update(newValue);
    	}
    	
    	/**
    	 * Compare this entry to another.
    	 * 
    	 * @return 1 if this entry has seen more input than the other one, -1 otherwise.
    	 */
    	public int compareTo(InputDetectionEntry other)
    	{
    		return acc.compareTo(other.acc);
    	}
    }
    
    /**
     * Slick2D / LWJGL controller ID.
     */
	private int controllerId;
	
	/**
	 * Actual Input-Output map to use for populating the controller.
	 */
	private ArrayList<Mapping> map;
	
	/**
	 * Set of inputs to keep track of when doing input detection.
	 */
	private ArrayList<InputDetectionEntry> inputDetectionSet;
	
	/**
	 * Create a new gamepad input mapper.
	 * 
	 * @param id Slick2D / LWJGL controller ID
	 */
	public GamepadInput(int id)
	{
		controllerId = id;
		map = new ArrayList<Mapping>();
		
		// mikaels gamepad
		addMapping(new Mapping(Mapping.InputType.AXIS_MINUS, 1, Output.LEFTANALOG_X_MINUS));
		addMapping(new Mapping(Mapping.InputType.AXIS_PLUS, 1, Output.LEFTANALOG_X_PLUS));
		addMapping(new Mapping(Mapping.InputType.AXIS_MINUS, 2, Output.LEFTANALOG_Y_PLUS));
		addMapping(new Mapping(Mapping.InputType.AXIS_PLUS, 2, Output.LEFTANALOG_Y_MINUS));
		
		addMapping(new Mapping(Mapping.InputType.AXIS_MINUS, 4, Output.RIGHTANALOG_X_MINUS));
		addMapping(new Mapping(Mapping.InputType.AXIS_PLUS, 4, Output.RIGHTANALOG_X_PLUS));
		addMapping(new Mapping(Mapping.InputType.AXIS_MINUS, 5, Output.RIGHTANALOG_Y_PLUS));
		addMapping(new Mapping(Mapping.InputType.AXIS_PLUS, 5, Output.RIGHTANALOG_Y_MINUS));
		
		addMapping(new Mapping(Mapping.InputType.BUTTON, 2, Output.BUTTON1));
		addMapping(new Mapping(Mapping.InputType.BUTTON, 0, Output.BUTTON2));
		addMapping(new Mapping(Mapping.InputType.BUTTON, 1, Output.BUTTON3));
	}
	
	/**
	 * Get the Slick2D / LWJGL controller ID of this gamepad mapper.
	 * 
	 * @return Slick2D / LWJGL controller ID
	 */
	public int getControllerId()
	{
		return controllerId;
	}
	
	/**
	 * Internal helper method for getting the number of available buttons.
	 * 
	 * @param input Slick2D input
	 * @return Number of buttons
	 */
	private int getButtonCount(Input input)
	{
		int buttons = 0;
		
		// this is horrible but seems to be the only way
		try
		{
			for (int i = 0; i < 32; ++i)
			{
				// this will throw an exception for a non-existant button id
				input.isButtonPressed(i, controllerId);
				++buttons;
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		}
		
		return buttons;
	}
	
	/**
	 * Reports that GamepadInput supports input detection.
	 * 
	 * @return True
	 */
	@Override
	public boolean supportsInputDetection()
	{
		return true;
	}
	
	/**
	 * Begin input detection.
	 * 
	 * @param input Slick2D input
	 */
	@Override
    public void beginInputDetection(Input input)
    {
    	inputDetectionSet = new ArrayList<InputDetectionEntry>();
    	
    	// count our axis and buttons
    	int numaxis = input.getAxisCount(controllerId);
    	int numbuttons = getButtonCount(input);
    	
    	int i = 0;
    	
    	for (; i < numaxis; ++i)
    	{
    		// for each axis, listen for 1) positive and 2) negative inputs
    		inputDetectionSet.add(new InputDetectionEntry(new Mapping(Mapping.InputType.AXIS_PLUS, i, Output.NONE)));
    		inputDetectionSet.add(new InputDetectionEntry(new Mapping(Mapping.InputType.AXIS_MINUS, i, Output.NONE)));
    	}
    	
    	for (; i < numaxis + numbuttons; ++i)
    	{
    		// listen to each button
    		inputDetectionSet.add(new InputDetectionEntry(new Mapping(Mapping.InputType.BUTTON, i - numaxis, Output.NONE)));
    	}
    }
    
	/**
	 * Continue input detection.
	 * 
	 * @param input Slick2D input
	 * @throws IllegalStateException if called before beginInputDetection
	 */
	@Override
    public boolean detectInput(Input input) throws IllegalStateException
    {
    	if (inputDetectionSet == null)
    	{
    		throw new IllegalStateException("must call beginInputDetection before calling detectInput");
    	}
    	
    	// count our axis and buttons again
    	int numaxis = input.getAxisCount(controllerId);
    	int numbuttons = getButtonCount(input);
    	int i = 0;
    	
    	for (; i < 2 * numaxis; i += 2)
    	{
    		// check all axis
    		if (input.getAxisValue(controllerId, i / 2) > 0.0)
    		{
    			inputDetectionSet.get(i).update(input.getAxisValue(controllerId, i / 2));
    		}
    		else if (input.getAxisValue(controllerId, i / 2) < 0.0)
    		{
    			inputDetectionSet.get(i + 1).update(input.getAxisValue(controllerId, i / 2));
    		}
    	}
    	
    	// check all buttons
    	for (; i < 2 * numaxis + numbuttons; ++i)
    	{
    		inputDetectionSet.get(i).update(input.isButtonPressed(i - 2 * numaxis, controllerId) ? 1.0 : 0.0);
    	}
    	
//    	System.out.println("==========================");
//    	
//    	for (InputDetectionEntry e : inputDetectionSet)
//    	{
//    		System.out.println(e.map.getInputType() + "(" + e.map.getInputId() + ") " + e.acc.sum);
//    	}
    	
    	// we're confident once one of the inputs have accumulated a total difference of 2.0
    	// this is an arbitrary number, more or less
    	return Collections.max(inputDetectionSet).acc.sum >= 2.0;
    }
    
	/**
	 * Finish input detection, binding the detected input to a given output.
	 * 
	 * @param input Slick2D input
	 * @throws IllegalStateException if called before beginInputDetection
	 */
	@Override
	public void mapDetectedInput(Output output) throws IllegalStateException
	{
		// grab the entry that saw the most input and map it
		Mapping map = Collections.max(inputDetectionSet).map;
		map.output = output;
		
		addMapping(map);
	}
	
	/**
	 * Add an Input-Output mapping.
	 * 
	 * @param mapping Mapping to add
	 */
    public void addMapping(Mapping mapping)
    {
    	map.add(mapping);
    }
	
    /**
     * Clear all Input-Output mappings.
     */
    public void clearMappings()
    {
    	map.clear();
    }
    
	/**
	 * Read the given input and populate the given controller with
	 * appropriate settings according to the input/output map.
	 *  
	 * @param input Slick2D Input to read
	 * @param c Controller to populate
	 */
	public void mapInput(Input input, Controller c)
	{
		Vec2 leftAnalog = c.leftAnalog.getDirection();
		Vec2 rightAnalog = c.rightAnalog.getDirection();
		
		double left_x = leftAnalog.x;
		double left_y = leftAnalog.y;
		double right_x = rightAnalog.x;
		double right_y = rightAnalog.y;
		
		double axisvalue;
		
		// TODO: use axis to press buttons? use buttons to move axis?
		for (Mapping mapping : map)
		{
			switch (mapping.getInputType())
			{
			case AXIS_PLUS:
				axisvalue = input.getAxisValue(controllerId, mapping.getInputId());
				
				if (axisvalue < 0.0)
				{
					break;
				}
				
				switch (mapping.getOutput())
				{
				case LEFTANALOG_X_PLUS:
					left_x = axisvalue;
					break;
				case LEFTANALOG_X_MINUS:
					left_x = -axisvalue;
					break;
				case LEFTANALOG_Y_PLUS:
					left_y = axisvalue;
					break;
				case LEFTANALOG_Y_MINUS:
					left_y = -axisvalue;
					break;
				case RIGHTANALOG_X_PLUS:
					right_x = axisvalue;
					break;
				case RIGHTANALOG_X_MINUS:
					right_x = -axisvalue;
					break;
				case RIGHTANALOG_Y_PLUS:
					right_y = axisvalue;
					break;
				case RIGHTANALOG_Y_MINUS:
					right_y = -axisvalue;
					break;
				default:
					break;
				}
			case AXIS_MINUS:
				axisvalue = input.getAxisValue(controllerId, mapping.getInputId());
				
				if (axisvalue > 0.0)
				{
					break;
				}
				
				switch (mapping.getOutput())
				{
				case LEFTANALOG_X_PLUS:
					left_x = -axisvalue;
					break;
				case LEFTANALOG_X_MINUS:
					left_x = axisvalue;
					break;
				case LEFTANALOG_Y_PLUS:
					left_y = -axisvalue;
					break;
				case LEFTANALOG_Y_MINUS:
					left_y = axisvalue;
					break;
				case RIGHTANALOG_X_PLUS:
					right_x = -axisvalue;
					break;
				case RIGHTANALOG_X_MINUS:
					right_x = axisvalue;
					break;
				case RIGHTANALOG_Y_PLUS:
					right_y = -axisvalue;
					break;
				case RIGHTANALOG_Y_MINUS:
					right_y = axisvalue;
					break;
				default:
					break;
				}
			case BUTTON:
				switch (mapping.output)
				{
				case BUTTON1:
					c.buttonOne.setPressed(input.isButtonPressed(mapping.getInputId(), controllerId));
					break;
				case BUTTON2:
					c.buttonTwo.setPressed(input.isButtonPressed(mapping.getInputId(), controllerId));
					break;
				case BUTTON3:
					c.buttonThree.setPressed(input.isButtonPressed(mapping.getInputId(), controllerId));
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
		
		c.leftAnalog.setDirection(left_x, left_y);
		c.rightAnalog.setDirection(right_x, right_y);
	}
}
