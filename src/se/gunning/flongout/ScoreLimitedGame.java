package se.gunning.flongout;

import org.newdawn.slick.Graphics;

/**
 * ScoreLimitedGame implements a game ending mechanic for having the game
 * automatically end after one of the players reaches a set number of points.
 * 
 * @author Mikael Forsberg
 * @version 2015-05-31
 */
public class ScoreLimitedGame implements GameEndingMechanic
{
	/**
	 * Is the game over?
	 */
	private boolean gameOver;
	
	/**
	 * Number of points to reach in order to win the game.
	 */
	private int scoreLimit;
	
	/**
	 * Create a new score limited game ending mechanic.
	 * @param limit Number of points to win the game
	 */
	public ScoreLimitedGame(int limit)
	{
		gameOver = false;
		scoreLimit = limit;
	}
	
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
		if (game.getPlayerScore(0) >= scoreLimit || game.getPlayerScore(1) >= scoreLimit)
		{
			gameOver = true;
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void render(Graphics g, CoordinateTransformer ct)
	{
		g.drawString("First to " + scoreLimit, ct.toScreenX(0.0), ct.toScreenY(4.0));
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isGameOver()
	{
		return gameOver;
	}
}
