package se.gunning.flongout.powerups;

import se.gunning.flongout.GameScene;
import se.gunning.flongout.Paddle;
import se.gunning.flongout.Powerup;
import se.gunning.flongout.Vec2;

/**
 * A powerup that increases the size of the ball.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class LargerBallPowerup extends Powerup
{
	/**
	 * Create a new "larger ball" powerup.
	 * 
	 * @param pos Initial position
	 * @param dir Drop direction
	 */
	public LargerBallPowerup(Vec2 pos, DropDirection dir)
	{
		super(pos, dir);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void applyStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		// make the ball larger
		game.getBall().setRadius(0.6);
		game.rescaleGraphics();
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void removeStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		// make the ball normal size
		game.getBall().setRadius(0.15);
		game.rescaleGraphics();
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
		return "The ball is huge!";
	}
}
