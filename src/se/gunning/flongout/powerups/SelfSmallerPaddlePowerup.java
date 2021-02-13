package se.gunning.flongout.powerups;

import org.newdawn.slick.Color;

import se.gunning.flongout.GameScene;
import se.gunning.flongout.Paddle;
import se.gunning.flongout.Powerup;
import se.gunning.flongout.Vec2;

/**
 * A nasty powerup (powerdown) that reduces the size of 
 * the paddle that picks it up.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class SelfSmallerPaddlePowerup extends Powerup
{
	/**
	 * Create a new "smaller paddle powerdown" powerup.
	 * 
	 * @param pos Initial position
	 * @param dir Drop direction
	 */
	public SelfSmallerPaddlePowerup(Vec2 pos, DropDirection dir)
	{
		super(pos, dir);
		
		color = new Color(1.0f, 0.0f, 0.0f);
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
		self.setScale(0.5);
		game.rescaleGraphics();
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void removeStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		if (self != null)
		{
			self.setScale(1.0);
			game.rescaleGraphics();
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Type getType()
	{
		return Type.BAD;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public String getText()
	{
		return self.getName() + " is tiny!";
	}
}
