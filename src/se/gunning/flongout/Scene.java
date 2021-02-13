package se.gunning.flongout;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Represents a scene in the game, such as a menu or a playing field.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-11
 */
public interface Scene
{
    /**
     * Init is called once before the scene will begin to receive update and render requests.
     * 
     * @param gc Root GameContainer
     * @throws SlickException
     */
    public void init(GameContainer gc, CoordinateTransformer ct) throws SlickException;
    
    /**
     * Return true if the scene needs to be initialized, false otherwise.
     */
    public boolean needsInit();
    
    /**
     * Update is called each frame. This version of update is used by Flongout.
     * 
     * @param gc Root GameContainer
     * @param delta Time delta (?)
     * @param input Array of controllers
     * @throws SlickException
     */
    public void update(GameContainer gc, int delta, Controller[] input) throws SlickException;
    
    /**
     * Render is called for each frame.
     * 
     * @param gc Root GameContainer
     * @throws SlickException
     */
    public void render(GameContainer gc, Graphics g) throws SlickException;
    
    /**
     * Release resources for this scene.
     */
    public void destroy();
}
