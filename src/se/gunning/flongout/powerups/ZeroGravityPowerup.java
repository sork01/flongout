package se.gunning.flongout.powerups;

import se.gunning.flongout.GameScene;
import se.gunning.flongout.Paddle;
import se.gunning.flongout.Powerup;
import se.gunning.flongout.Vec2;

/**
 * A powerup that removes the force of gravity.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class ZeroGravityPowerup extends Powerup
{
	/**
	 * Create a new "zero gravity" powerup.
	 * 
	 * @param pos Initial position
	 * @param dir Drop direction
	 */
	public ZeroGravityPowerup(Vec2 pos, DropDirection dir)
	{
		super(pos, dir);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void applyStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		// set gravity to zero
		game.getWorld().setGravity(0);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void removeStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		// restore default gravity
		game.getWorld().setGravity(-20.0);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Type getType()
	{
		return Type.NEUTRAL;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public String getText()
	{
		return "There goes gravity!";
	}
}
