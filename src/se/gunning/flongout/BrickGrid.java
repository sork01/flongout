package se.gunning.flongout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Represents a grid where each cell can contain a brick.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-16
 */
public class BrickGrid
{
    /**
     * Callback interface for getting information about broken bricks.
     *
     * @author Mikael Forsberg
     * @author Robin Gunning
     * @author Jonathan Yao Håkansson
     * @version 2015-05-16
     */
	public interface BrickBreakAction
	{
		public void onBrickBroken(BrickGrid grid, Rect brick);
	}
    
    /**
     * Internal helper class for grid indices.
     *
     * @author Mikael Forsberg
     * @author Robin Gunning
     * @author Jonathan Yao Håkansson
     * @version 2015-05-16
     */
	private static class Index
	{
        /**
         * Row of this index.
         */
		public final int row;
        
        /**
         * Column of this index.
         */
		public final int column;
		
        /**
         * Create a new index.
         *
         * @param row Row
         * @param column Column
         */
		public Index(int row, int column)
		{
			this.row = row;
			this.column = column;
		}
	}
	
    /**
     * Rectangle occupied by the grid as a whole.
     */
	private Rect rect;
	
    /**
     * Number of rows in the grid.
     */
	private int rows;
    
    /**
     * Number of columns in the grid.
     */
	private int columns;
	
    /**
     * Cell contents.
     */
	private ArrayList<ArrayList<Brick>> bricks;
    
    /**
     * List of free indices.
     */
	private Stack<Index> freeIndices;
	
    /**
     * Brick break callback. Can be null.
     */
	private BrickBreakAction brickBreakAction;
	
    /**
     * Create a new grid.
     *
     * @param rect Rectangle occupied by the grid as a whole
     * @param rows Number of rows in the grid
     * @param columns Number of columns in the grid
     */
	public BrickGrid(Rect rect, int rows, int columns)
	{
		this.rect = rect;
		this.rows = rows;
		this.columns = columns;
		
        // initialize cell contents and set all indices as free
		bricks = new ArrayList<ArrayList<Brick>>(rows);
		freeIndices = new Stack<Index>();
        
		for (int i = 0; i < rows; ++i)
		{
			ArrayList<Brick> row = new ArrayList<Brick>();
			
			for (int j = 0; j < columns; ++j)
			{
				freeIndices.add(new Index(i, j));
				row.add(null);
			}
			
			bricks.add(row);
		}
		
        // shuffle to get random indices later
		Collections.shuffle(freeIndices);
	}
	
    /**
     * Get the rectangle occupied by the grid as a whole.
     *
     * @return The rectangle occupied by the grid as a whole
     */
	public Rect getRect()
	{
		return rect;
	}
	
    /**
     * Set a callback that will be called whenever a brick is broken.
     *
     * @param action Callback
     */
	public void setBrickBreakAction(BrickBreakAction action)
	{
		brickBreakAction = action;
	}
	
    /**
     * Spawn a brick at a random free index. If there are no free indices
     * this method will do nothing.
     *
     * @param world Physics world to create walls in
     */
	public void spawnRandomBrick(Physics world)
	{
        // no free slots
		if (freeIndices.size() < 1)
		{
			return;
		}
		
        // grab a random index
		Index idx = freeIndices.pop();
		
        // construct a rectangle for the selected index
		Rect r = new Rect(
				rect.getX() + (double)idx.column * rect.getWidth() / (double)columns,
				rect.getY() + (double)idx.row * rect.getHeight() / (double)rows,
				rect.getWidth() / (double)columns,
				rect.getHeight() / (double) rows);
		
        // add wall to physics world
		world.addWall("brickgrid-brick", r);
		
        // add brick to cell contents
		bricks.get(idx.row).set(idx.column, new Brick(r));
	}
	
    /**
     * Retrieve a physics collision callback suitable for keeping track of
     * bricks being hit and becoming broken, including calling a brick-break
     * callback when appropriate.
     *
     * @return A physics collision callback
     */
	public Physics.CollisionAction getCollisionAction()
	{
        // need a reference to brickgrid instance for the closure
		final BrickGrid grid = this;
		
		return new Physics.CollisionAction()
		{
			@Override
			public void onCollision(Ball ball, Rect wall, String group, Physics world)
			{
                // don't care about walls that aren't our bricks
				if (!group.equals("brickgrid-brick"))
				{
					return;
				}
				
                // scan our cells for the brick that's been hit
				for (int i = 0; i < rows; ++i)
				{
					for (int j = 0; j < columns; ++j)
					{
						Brick b = bricks.get(i).get(j);
						
						if (b != null && b.getRect().equals(wall))
						{
                            // found the brick, decrement hp
							b.setHp(b.getHp() - ball.getPenetrationPower());
							
                            // brick broken?
							if (b.getHp() <= 0)
							{
                                // remove it
								bricks.get(i).set(j, null);
								world.removeWall("brickgrid-brick", wall);
								
                                // make the index available
								freeIndices.add(new Index(i, j));
								Collections.shuffle(freeIndices);
								
                                // call the brick-break callback
								if (brickBreakAction != null)
								{
									brickBreakAction.onBrickBroken(grid, wall);
								}
							}
						}
					}
				}
			}
			
			@Override
			public void onCollision(Ball ball, Paddle paddle, Physics world)
			{
			}
			
			@Override
			public void onPaddleHit(Ball ball, Paddle paddle, Physics world)
			{
			}
		};
	}
	
    /**
     * Draw a visual representation of the grid. Not intended for production.
     *
     * @param g Slick2D Graphics instance to draw with
     * @param ct Coordinate transformer
     */
	public void debugDraw(Graphics g, CoordinateTransformer ct)
	{
		for (int i = 0; i < rows; ++i)
		{
			for (int j = 0; j < columns; j++)
			{
				if (bricks.get(i).get(j) != null)
				{
					bricks.get(i).get(j).getRect().debugDraw(g, ct);
				}
			}
		}
	}
	
	/**
	 * Draw the brick graphics.
	 * 
	 * @param g Slick2D Graphics to draw on / with
	 * @param ct Coordinate transformer
	 * @param img Image for bricks with hp == 1
	 * @param img2 Image for bricks with hp == 2
	 * @param img3 Image for bricks with hp == 3
	 */
	public void brickDraw(Graphics g, CoordinateTransformer ct, Image img, Image img2, Image img3)
	{
		for (int i = 0; i < rows; ++i)
		{
			for (int j = 0; j < columns; j++)
			{
				if (bricks.get(i).get(j) != null)
				{
					Rect r = bricks.get(i).get(j).getRect();
					if (bricks.get(i).get(j).getHp() == 1)
					{
						img.draw(ct.toScreenX((float)r.getCenter().x - r.getWidth()/2), ct.toScreenY((float)(r.getCenter().y) + r.getHeight()/2));
					}
					else if (bricks.get(i).get(j).getHp() == 2)
					{
						img2.draw(ct.toScreenX((float)r.getCenter().x - r.getWidth()/2), ct.toScreenY((float)(r.getCenter().y) + r.getHeight()/2));
					}
					else if (bricks.get(i).get(j).getHp() == 3)
					{
						img3.draw(ct.toScreenX((float)r.getCenter().x - r.getWidth()/2), ct.toScreenY((float)(r.getCenter().y) + r.getHeight()/2));
					}
					
				}
			}
		}
	}
}
