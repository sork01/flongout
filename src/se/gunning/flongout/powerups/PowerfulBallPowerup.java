package se.gunning.flongout.powerups;

import se.gunning.flongout.GameScene;
import se.gunning.flongout.Paddle;
import se.gunning.flongout.Powerup;
import se.gunning.flongout.Vec2;

/**
 * A powerup that increases the penetration power of the ball.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class PowerfulBallPowerup extends Powerup
{
	/**
	 * Create a new "powerful ball" powerup.
	 * 
	 * @param pos Initial position
	 * @param dir Drop direction
	 */
	public PowerfulBallPowerup(Vec2 pos, DropDirection dir)
	{
		super(pos, dir);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void applyStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		// increase the power of the ball
		game.getBall().setPenetrationPower(3);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void removeStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		// reset the power of the ball
		game.getBall().setPenetrationPower(1);
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
		return "The ball breaks the bricks with ease!";
	}
}
