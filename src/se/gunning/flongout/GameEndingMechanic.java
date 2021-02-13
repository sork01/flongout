package se.gunning.flongout;

import org.newdawn.slick.Graphics;

/**
 * Interface for game ending mechanics.
 * 
 * @author Mikael Forsberg
 * @version 2015-05-31
 */
public interface GameEndingMechanic
{
	// TODO: pause
	/**
	 * Reset whatever conditions would cause the game to be over.
	 */
	public void reset();
	
	/**
	 * Track the state of the game to determine whether the game is over or not.
	 * @param game Game in progress
	 */
	public void update(GameScene game);
	
	/**
	 * Render some kind of display of the current state, such as time remaining.
	 * @param g Slick2D Graphics
	 * @param ct Coordinate transformer
	 */
	public void render(Graphics g, CoordinateTransformer ct);
	
	/**
	 * Return true if the game is over, false otherwise.
	 * @return
	 */
	public boolean isGameOver();
}
