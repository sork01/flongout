package se.gunning.flongout.powerups;

import org.newdawn.slick.Color;

import se.gunning.flongout.GameScene;
import se.gunning.flongout.Paddle;
import se.gunning.flongout.Powerup;
import se.gunning.flongout.Vec2;

/**
 * A powerup that decreases the size of the ball.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class SmallerBallPowerup extends Powerup
{
	/**
	 * Create a new "larger ball" powerup.
	 * 
	 * @param pos Initial position
	 * @param dir Drop direction
	 */
	public SmallerBallPowerup(Vec2 pos, DropDirection dir)
	{
		super(pos, dir);
		
		color = new Color(1.0f, 0.0f, 0.0f);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void applyStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		// make the ball smaller
		game.getBall().setRadius(0.08);
		game.rescaleGraphics();
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void removeStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		// set the normal size
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
		return "Such a tiny ball!";
	}
}
