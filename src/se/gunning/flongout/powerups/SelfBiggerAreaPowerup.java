package se.gunning.flongout.powerups;

import se.gunning.flongout.GameScene;
import se.gunning.flongout.Paddle;
import se.gunning.flongout.Powerup;
import se.gunning.flongout.Rect;
import se.gunning.flongout.Vec2;

/**
 * A nice powerup that increases the area of movement
 * for the paddle that picks it up.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class SelfBiggerAreaPowerup extends Powerup
{
	/**
	 * Create a new "bigger area" powerup.
	 * 
	 * @param pos Initial position
	 * @param dir Drop direction
	 */
	public SelfBiggerAreaPowerup(Vec2 pos, DropDirection dir)
	{
		super(pos, dir);
	}
	
	/**
	 * Keep track of affected paddle in order to be able to
	 * remove the effect later.
	 */
	private Paddle self;
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void applyStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		self = collector;
		if (self.getName().equalsIgnoreCase("player one"))
		{
			self.setArea(new Rect(-8.0, -4.5, 6, 8.0));
		}
		
		if (self.getName().equalsIgnoreCase("player two"))
		{
			self.setArea(new Rect(2, -4.5, 6.0, 8.0));
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void removeStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		if (self != null)
		{
			if (self.getName().equalsIgnoreCase("player one"))
			{
				self.setArea(new Rect(-6.4, -3.0, 2.0, 6.0));
			}
			
			if (self.getName().equalsIgnoreCase("player two"))
			{
				self.setArea(new Rect(4.4, -3.0, 2.0, 6.0));
			}

		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Type getType()
	{
		return Type.GOOD;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public String getText()
	{
		return self.getName() + " is all over the place!";
	}
}
