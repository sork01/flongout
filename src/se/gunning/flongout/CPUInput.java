package se.gunning.flongout;

import org.newdawn.slick.Input;

/**
 * A not-so-intelligent AI that can control a controller.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class CPUInput implements InputMapper
{
	/**
	 * Timeout for next change to the left analog stick.
	 */
	private long nextLeftStickChange = 0;
	
	/**
	 * Timeout for next change to the right analog stick.
	 */
	private long nextRightStickChange = 0;
	
	/**
	 * CPU needs to know where its paddle is.
	 */
	private Paddle paddle;
	
	/**
	 * CPU needs to know where the ball is.
	 */
	private Ball ball;
	
	/**
	 * CPU ready to play?
	 */
	private boolean initialized;
	
	/**
	 * Create a new CPU player.
	 */
	public CPUInput()
	{
		// not ready until we have a paddle and a ball
		initialized = false;
	}
	
	/**
	 * Set the paddle to be controlled by this CPU.
	 * 
	 * @param p Paddle to control
	 */
	public void setPaddle(Paddle p)
	{
		paddle = p;
		
		if (ball != null)
		{
			initialized = true;
		}
	}
	
	/**
	 * Set the ball to be tracked by this CPU.
	 * 
	 * @param b Ball to track
	 */
	public void setBall(Ball b)
	{
		ball = b;
		
		if (paddle != null)
		{
			initialized = true;
		}
	}
	
	/**
	 * Reset this CPU player. Use this to avoid the CPU giving inputs in the menus!
	 */
	public void reset()
	{
		ball = null;
		paddle = null;
		initialized = false;
	}
	
	/**
	 * Populate a controller with some not-so-intelligent inputs.
	 */
	public void mapInput(Input in, Controller c)
	{
		// if not ready to play, configure the controller as if we're not touching anything
		if (!initialized)
		{
			c.leftAnalog.setDirection(new Vec2(0, 0));
			c.rightAnalog.setDirection(new Vec2(0, 0));
			c.buttonOne.release();
			c.buttonTwo.release();
			c.buttonThree.release();
			
			return;
		}
		
		long time = System.currentTimeMillis();
		
		// time to move the left stick? the left stick moves the paddle.
		if (nextLeftStickChange < time)
		{
			// try to stay level with the ball
			if (ball.getPosition().y > paddle.getPosition().y)
			{
				c.leftAnalog.setAngle(Math.PI / 2.0);
			}
			else
			{
				c.leftAnalog.setAngle(-Math.PI / 2.0);
			}
			
			// don't move the left stick for a while
			nextLeftStickChange = time + 150;
		}
		
		// time to move the right stick? the right stick spins the paddle.
		if (nextRightStickChange < time)
		{	
			
			// try to keep the paddle pointed in the direction of the ball
			if (ball.getPosition().y > paddle.getPosition().y)
			{
				if (ball.getPosition().x > paddle.getPosition().x)
				{
					c.rightAnalog.setAngle(Math.PI * 0.75);
				}
				else
				{
					c.rightAnalog.setAngle(Math.PI / 4.0);
				}
			}
			else
			{
				if (ball.getPosition().x > paddle.getPosition().x)
				{
					c.rightAnalog.setAngle(-Math.PI * 0.75);
				}
				else
				{
					c.rightAnalog.setAngle(-Math.PI / 4.0);
				}
			}
			
			// if the ball is close enough, try to hit it
			if (ball.getPosition().subtract(paddle.getPosition()).length() < 1.5)
			{
				if (ball.getPosition().y - paddle.getPosition().y < 0.5)
				{
					c.leftAnalog.setAngle(-Math.PI / 2.0);
					if (paddle.getRestingAngle() == 0.0)
					{
						c.rightAnalog.setAngle(Math.PI);
					}
				}
				else if (ball.getPosition().y - paddle.getPosition().y > 0.5)
				{
					c.leftAnalog.setAngle(Math.PI / 2.0);
					if (paddle.getRestingAngle() == Math.PI)
					{
						c.rightAnalog.setAngle(0.0);
					}
				}
			}
			
			// don't move the right stick for a while
			nextRightStickChange = time + 25;
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
