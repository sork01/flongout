package se.gunning.flongout.powerups;

import se.gunning.flongout.GameScene;
import se.gunning.flongout.Paddle;
import se.gunning.flongout.Powerup;
import se.gunning.flongout.Vec2;

/**
 * A powerup that reduces the size of the opponents paddle.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class OpponentSmallerPaddlePowerup extends Powerup
{
	/**
	 * Create a new "opponent gets smaller paddle" powerup.
	 * 
	 * @param pos Initial position
	 * @param dir Drop direction
	 */
	public OpponentSmallerPaddlePowerup(Vec2 pos, DropDirection dir)
	{
		super(pos, dir);
	}
	
	/**
	 * Keep track of the opponent in order to be able to
	 * remove the effect later.
	 */
	private Paddle opponent;
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void applyStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		opponent = other;
		other.setScale(0.7);
		game.rescaleGraphics();
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void removeStaticEffects(Paddle collector, Paddle other, GameScene game)
	{
		if (opponent != null)
		{
			opponent.setScale(1.0);
			game.rescaleGraphics();
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
		return opponent.getName() + " is tiny!";
	}
}
