package se.gunning.flongout;

import org.newdawn.slick.Graphics;

/**
 * EndlessGame implements a game ending mechanic that will never
 * signal the game as being over, thus making for an endless game.
 * 
 * @author Mikael Forsberg
 * @version 2015-05-31
 */
public class EndlessGame implements GameEndingMechanic
{
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void reset()
	{
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void update(GameScene game)
	{
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void render(Graphics g, CoordinateTransformer ct)
	{
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isGameOver()
	{
		return false;
	}
}
