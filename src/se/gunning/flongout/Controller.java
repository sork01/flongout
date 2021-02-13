package se.gunning.flongout;

/**
 * Controller models the ideal controller for Flongout.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-11
 */
public class Controller
{
	/**
	 * AnalogStick models an analog joystick used for the ideal controller.
	 * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-11
	 */
	public static class AnalogStick
	{
		/**
		 * Direction vector of the joystick
		 */
		private Vec2 direction;
		
		/**
		 * Create a new AnalogStick
		 */
		public AnalogStick()
		{
			direction = new Vec2(0, 0);
		}
		
		/**
		 * Set the direction vector of the joystick
		 * 
		 * @param v The new direction vector
		 */
		public void setDirection(Vec2 v)
		{
			this.direction = v;
		}
		
		/**
		 * Set the direction vector of the joystick
		 * 
		 * @param x X component of new direction
		 * @param y Y component of new direction
		 */
		public void setDirection(double x, double y)
		{
			this.direction = new Vec2(x, y);
		}
		
		/**
		 * Get the direction vector of the joystick
		 * 
		 * @return The current direction vector
		 */
		public Vec2 getDirection()
		{
			return direction;
		}
		
		/**
		 * Set the angle of the joystick
		 * 
		 * @param angle The new angle in radians
		 */
		public void setAngle(double angle)
		{
			direction = new Vec2(Math.cos(angle), Math.sin(angle));
		}
		
		/**
		 * Get the angle of the joystick
		 * 
		 * @return The angle of the joystick in radians
		 */
		public double getAngle()
		{
			return Math.atan2(direction.y, direction.x);
		}
		
		/**
		 * Rotate the joystick.
		 * 
		 * @param radians Angle in radians to rotate
		 */
		public void rotate(double radians)
		{
			setAngle(getAngle() + radians);
		}
		
		/**
		 * Set the magnitude of the joystick. The magnitude is how far from
		 * the center the joystick has been moved.
		 * 
		 * @param magn The new magnitude 
		 */
		public void setMagnitude(double magn)
		{
			direction = direction.normalize().scale(magn);
		}
		
		/**
		 * Get the magnitude of the joystick. The magnitude is how far from
		 * the center the joystick has been moved.
		 * 
		 * @return The current magnitude
		 */
		public double getMagnitude()
		{
			return direction.length();
		}
	}
	
	/**
	 * Models a pressable button on the ideal controller.
	 * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-11
	 */
	public static class Button
	{
		/**
		 * Pressed state. True if button is down, false otherwise.
		 */
		private boolean pressed;
		
		/**
		 * Create a new button. The button will be unpressed initially.
		 */
		public Button()
		{
			pressed = false;
		}
		
		/**
		 * Press the button.
		 */
		public void press()
		{
			pressed = true;
		}
		
		/**
		 * Release the button.
		 */
		public void release()
		{
			pressed = false;
		}
		
		/**
		 * Press or release the button.
		 */
		public void setPressed(boolean state)
		{
			pressed = state;
		}
		
		/**
		 * Check if the button is pressed.
		 * 
		 * @return True if the button is pressed, false otherwise.
		 */
		public boolean isPressed()
		{
			return pressed;
		}
	}
	
	/**
	 * Left analog stick on the controller.
	 */
	public AnalogStick leftAnalog;
	
	/**
	 * Right analog stick on the controller.
	 */
	public AnalogStick rightAnalog;
	
	/**
	 * Buttons on the controller.
	 */
	public Button buttonOne, buttonTwo, buttonThree;
	
	/**
	 * Create a new controller.
	 */
	public Controller()
	{
		leftAnalog = new AnalogStick();
		rightAnalog = new AnalogStick();
		
		buttonOne = new Button();
		buttonTwo = new Button();
		buttonThree = new Button();
	}
}
