package se.gunning.flongout;

import org.newdawn.slick.Graphics;

/**
 * TimeGame implements a timed game ending mechanic for having the game
 * automatically end after a given amount of time.
 * 
 * @author Mikael Forsberg
 * @version 2015-05-31
 */
public class TimedGame implements GameEndingMechanic
{
	/**
	 * Allotted time.
	 */
	private int seconds;
	
	/**
	 * Timer used for timekeeping.
	 */
	private Stopwatch timer;
	
	/**
	 * Create a new timed game ending mechanic.
	 * @param seconds Time in seconds to allot for the game
	 */
	public TimedGame(int seconds)
	{
		this.seconds = seconds;
		timer = new Stopwatch();
		timer.start();
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void reset()
	{
		timer.reset();
		timer.start();
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
		g.drawString("Time: " + new Integer(seconds - (int)timer.getSeconds()).toString(), ct.toScreenX(0.0), ct.toScreenY(4.0));
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isGameOver()
	{
		return (int)timer.getSeconds() >= seconds;
	}
}
