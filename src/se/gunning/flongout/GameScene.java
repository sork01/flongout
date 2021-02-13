package se.gunning.flongout;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

/**
 * The main game scene.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-31
 */
public class GameScene implements Scene
{
	/**
	 * Scene initialization status.
	 */
	boolean initialized = false;
	
	/**
	 * Coordinate transformer used for converting between world and screen coordinates. 
	 */
	CoordinateTransformer coords;
	
	/**
	 * Physics simulation.
	 */
	Physics world;
	
	/**
	 * Main physics collision action callback used to detect when the ball leaves the screen.
	 */
	Physics.CollisionAction collisionAction;
	
	/**
	 * The ball.
	 */
	Ball ball;
	
	/**
	 * The left paddle.
	 */
	Paddle paddleOne;
	
	/**
	 * The right paddle.
	 */
	Paddle paddleTwo;
	
	/**
	 * Keeps track of which paddle last hit the ball, to determine which
	 * direction to send powerups.
	 */
	Paddle lastPaddleToHit;
	
	/**
	 * The brick grid contains and handles all the bricks.
	 */
	BrickGrid bricks;
	
	/**
	 * Brick break callback passed to the brick grid.
	 */
	BrickGrid.BrickBreakAction brickBreakAction;
	
	/**
	 * Brick collision action used in the physics simulation.
	 */
	Physics.CollisionAction brickAction;
	
	/**
	 * Time of last brick spawn.
	 */
	long lastBrickSpawned;
	
	/**
	 * Description of most recently picked powerup.
	 */
	String pupText = "";
	
	/**
	 * Ball graphics.
	 */
	Image ballpic;
	
	/**
	 * Left paddle graphics.
	 */
	Image paddlepic;
	
	/**
	 * Right paddle graphics.
	 */
	Image paddlepic2;
	
	/**
	 * Temporary store for scaled paddle graphics.
	 */
	Image paddlepic_scaled;
	
	/**
	 * Powerup (good) graphics.
	 */
	Image pup;
	
	/**
	 * Powerup (bad) graphics.
	 */
	Image pdown;
	
	/**
	 * Powerup (neutral) graphics.
	 */
	Image pneutral;
	
	/**
	 * Brick graphic for bricks at low HP.
	 */
	Image brickpic1;
	
	/**
	 * Brick graphic for bricks at mid HP.
	 */
	Image brickpic2;
	
	/**
	 * Brick graphic for bricks at full HP.
	 */
	Image brickpic3;
	
	/**
	 * Cache storage for left paddle graphic.
	 */
	Image originalpaddle;
	
	/**
	 * Cache storage for right paddle graphic.
	 */
	Image originalpaddle2;
	
	/**
	 * Sound for paddle-to-ball collision.
	 */
	Sound hit;
	
	/**
	 * Sound for the countdown.
	 */
	Sound cdown;
	
	/**
	 * Background music.
	 */
	Sound bgmusic;
	
	/**
	 * Brick-break sound.
	 */
	Sound breaking;
	
	/**
	 * Background animation.
	 */
	BackgroundAnimation bganim;
	
	/**
	 * List of visited points for drawing the ball's tail.
	 */
	LinkedList<Vec2> ballPoints;
	
	/**
	 * List of in-flight (not yet picked-up) powerups.
	 */
	ArrayList<Powerup> powerups;
	
	/**
	 * List of active (picked-up) powerups.
	 */
	ArrayList<Powerup> activePowerups;
	
	/**
	 * Factory for spawning random powerups.
	 */
	PowerupFactory powerupFactory;
	
	/**
	 * Left player score.
	 */
	Counter scoreP1;
	
	/**
	 * Right player score.
	 */
	Counter scoreP2;
	
	/**
	 * Font for score displays.
	 */
	TrueTypeFont scoreFont;
	
	/**
	 * Font for the countdown.
	 */
	TrueTypeFont countDownFont;
	
	/**
	 * Should we use font antialiasing?
	 */
	private boolean antiAlias = true;
	
	/**
	 * Counter used for the round-begin countdown.
	 */
	Counter countDown;
	
	/**
	 * Stopwatch used for the round-begin countdown.
	 */
	Stopwatch countDownWatch;
	
	/**
	 * Counter used for the temporary onscreen powerup descriptions.
	 */
	Counter countDownText;
	
	/**
	 * Stopwatch used for the temporary onscreen powerup descriptions.
	 */
	Stopwatch countDownTextWatch;
	
	/**
	 * Is the round-begin countdown ongoing?
	 */
	boolean isCountdown;
	
	/**
	 * Is there a temporary powerup description on screen? 
	 */
	boolean isCountDownText;
	
	/**
	 * Reference to main game.
	 */
	Main mainGame;
	
	boolean backanim = true;
	
	/**
	 * Escape key input mapper used for leaving the game.
	 */
	InputMapper escapeKeyMapper;
	
	/**
	 *  Are we paused (in the in-game menu) ?
	 */
	boolean isPaused;
	
	/**
	 * Game ending mechanic used to determine whether the game is over.
	 */
	GameEndingMechanic gameEnder;
	
	/**
	 * Create a new gameplay scene in endless mode.
	 * @param main Main game / scene controller instance
	 * @param backanim Enable the background animation?
	 */
	public GameScene(Main main, boolean backanim)
	{
		this(main, backanim, new EndlessGame());
	}
	
	/**
	 * Create a new gameplay scene.
	 * 
	 * @param main Main game / scene controller instance
	 * @param backanim Enable the background animation?
 	 * @param ender Game ending mechanic
	 */
	public GameScene(Main main, boolean backanim, GameEndingMechanic ender)
	{
		gameEnder = ender;
		ender.reset();
		
		// TODO: add an escape-key-only inputmapper that hits button three, use in gamescene for exiting
		mainGame = main;
		
		isPaused = false;
		
		escapeKeyMapper = (InputMapper) new EscapeKeyInput(); 
		
		this.backanim = backanim;
		bganim = new BackgroundAnimation(162);
		
		powerupFactory = new PowerupFactory();
		
		scoreP1 = new Counter();
		scoreP2 = new Counter();
		
		world = new Physics();
		world.setGravity(-20.0);
		
		// left
		world.addWall("left", -14.0, -4.5, 5.0, 9.0);
		
		// right
		world.addWall("right", 9.0, -4.5, 5.0, 9.0);
		
		// top
		world.addWall("static", -9.0, 4.5, 18.0, 5.0);
		
		// bottom
		world.addWall("static", -9.0, -9.5, 18.0, 5.0);
		
		ball = new Ball(0.15);
		ballPoints = new LinkedList<Vec2>();
		
		paddleOne = new Paddle(1.0, Math.PI);
		paddleOne.setName("Player One");
		paddleOne.setArea(new Rect(-6.4, -3.0, 2.0, 6.0));
		paddleOne.setPosition(new Vec2(-6.0, 0.0));
		paddleOne.setVelocity(new Vec2(1, 0));
		paddleOne.setDirection(new Vec2(1, 0));
		paddleOne.setAngularVelocity(0);
		
		paddleTwo = new Paddle(1.0, 0.0);
		paddleTwo.setName("Player Two");
		paddleTwo.setArea(new Rect(4.4, -3.0, 2.0, 6.0));
		paddleTwo.setPosition(new Vec2(6.0, 0.0));
		paddleTwo.setVelocity(new Vec2(0, 0));
		paddleTwo.setDirection(new Vec2(1, 0));
		paddleTwo.setAngularVelocity(0);
		
		bricks = new BrickGrid(new Rect(-0.5, -4.5, 1.0, 4.5), 8, 2);
		brickAction = bricks.getCollisionAction();
	
		for (int i = 0; i < 16; ++i)
		{
			bricks.spawnRandomBrick(world);
		}
		
		lastBrickSpawned = System.currentTimeMillis();
		
		brickBreakAction = new BrickGrid.BrickBreakAction()
		{
			@Override
			public void onBrickBroken(BrickGrid grid, Rect brick)
			{
				Random rng = new Random();
				breaking.play();
				
				if (rng.nextDouble() > 0 && lastPaddleToHit != null)
				{
					if (lastPaddleToHit == paddleOne)
					{
						powerups.add(powerupFactory.createRandomPowerup(brick.getCenter(), Powerup.DropDirection.LEFT));
					}
					else
					{
						powerups.add(powerupFactory.createRandomPowerup(brick.getCenter(), Powerup.DropDirection.RIGHT));
					}
				}
			}
		};
		
		bricks.setBrickBreakAction(brickBreakAction);
		
		world.addBall(ball);
		world.addPaddle(paddleOne);
		world.addPaddle(paddleTwo);
		
		powerups = new ArrayList<Powerup>();
		activePowerups = new ArrayList<Powerup>();
		
		collisionAction = new Physics.CollisionAction()
		{
			@Override
			public void onPaddleHit(Ball ball, Paddle paddle, Physics world)
			{
				if (!hit.playing())
				{
					hit.play();
				}
				lastPaddleToHit = paddle;
			}
			
			@Override
			public void onCollision(Ball ball, Paddle paddle, Physics world)
			{
				if (!hit.playing())
				{
					hit.play();
				}
				lastPaddleToHit = paddle;
			}
			
			@Override
			public void onCollision(Ball ball, Rect wall, String group, Physics world)
			{
				if (group.equals("left"))
				{
					scoreP2.incrementByOne();
					beginRound();
				}
				else if (group.equals("right"))
				{
					scoreP1.incrementByOne();
					beginRound();
				}
				
			}
		};
		
		countDown = new Counter();
		countDownWatch = new Stopwatch();
	}
	
	/**
	 * Start a new round.
	 */
	public void beginRound()
	{
		// clear powerup description text
		pupText = "";
		
		// initialize the round start countdown
		countDown = new Counter();
		countDown.setCount(3);
		countDownWatch = new Stopwatch();
		countDownWatch.start();
		isCountdown = true;
		cdown.play();
		
		// reset the ball
		ball.setPosition(new Vec2(0.0, 3.0));
		ball.setVelocity(new Vec2(0.0, 0.0));
		
		// reset paddle graphics
		paddlepic = originalpaddle;
		paddlepic2 = originalpaddle2;
		
		// remove active powerup effects
		for (Powerup p : activePowerups)
		{
			p.removeStaticEffects(paddleOne, paddleTwo, this);
		}
		
		// clear all powerups
		powerups.clear();
		activePowerups.clear();
		
		// clear ball's tail
		ballPoints.clear();
		
		rescaleGraphics();
		
		// setup cpu paddles
		if (mainGame.getPlayerInput(0) instanceof CPUInput)
		{
			setupCpuPaddle((CPUInput)mainGame.getPlayerInput(0), 0);
		}
		
		if (mainGame.getPlayerInput(1) instanceof CPUInput)
		{
			setupCpuPaddle((CPUInput)mainGame.getPlayerInput(1), 1);
		}
		
		// does this fix the hit sound not playing after a while? I dunno! but it bugs out countdown. stops playing and shit
		//hit.stop();
	}
	
	/**
	 * (Re)scale graphics for objects that can change size. 
	 */
	public void rescaleGraphics()
	{
		// scale the ball graphic
		if (Math.floor(coords.widthToScreen(ball.getRadius())) != Math.floor(ballpic.getWidth()/2))
		{
			ballpic = ballpic.getScaledCopy((int)coords.widthToScreen(ball.getRadius()*2), (int)coords.widthToScreen(ball.getRadius()*2));
		}
		
		// scale the left paddle graphic
		if (Math.floor(coords.widthToScreen(paddleOne.getButtRadius()+paddleOne.getLength()+paddleOne.getTipRadius())) != Math.floor(paddlepic.getWidth()))
		{
			paddlepic = paddlepic.getScaledCopy((int) coords.widthToScreen(paddleOne.getWidth()), (int)(coords.heightToScreen(paddleOne.getHeight())));
			paddlepic.setCenterOfRotation((float)0.139130435*paddlepic.getWidth(), (float)0.503355705*paddlepic.getHeight());
		}
		
		// scale the right paddle graphic
		if (Math.floor(coords.widthToScreen(paddleTwo.getButtRadius()+paddleTwo.getLength()+paddleTwo.getTipRadius())) != Math.floor(paddlepic2.getWidth()))
		{
			paddlepic2 = paddlepic2.getScaledCopy((int) coords.widthToScreen(paddleTwo.getWidth()), (int)(coords.heightToScreen(paddleTwo.getHeight())));
			paddlepic2.setCenterOfRotation((float)0.139130435*paddlepic2.getWidth(), (float)0.503355705*paddlepic2.getHeight());
		}
	}
	
	/**
	 * Render the game.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param g Slick2D Graphics
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		if (isPaused)
		{
			return;
		}
		
		if (backanim)
		{
			// draw the background animation
			bganim.render(g, coords);
		}
			
		// draw the ball's tail
		Vec2 lastPt = null;
		
		if (ballPoints.size() > 10)
		{
			ballPoints = new LinkedList<Vec2>(ballPoints.subList(ballPoints.size() - 10, ballPoints.size()));
		}
		
		g.setLineWidth(2.0f);
		g.setColor(new Color(1.0f, 1.0f, 1.0f));
		
		for (Vec2 pt : ballPoints)
		{
			pt = coords.toScreen(pt);
			
			if (lastPt != null)
			{
				g.drawLine((float)pt.x, (float)pt.y, (float)lastPt.x, (float)lastPt.y); 
			}
			
			lastPt = pt;
		}
		
		// draw the ball
		ballpic.draw(coords.toScreenX(ball.getPosition().x - ball.getRadius()), coords.toScreenY(ball.getPosition().y + ball.getRadius()));
		
		// draw the paddles
		paddlepic.setRotation((float)(paddleOne.getAngle()*-57.2957795));
		paddlepic2.setRotation((float)(paddleTwo.getAngle()*-57.2957795));
		paddlepic.draw(coords.toScreenX(paddleOne.getPosition().x - paddleOne.getButtRadius()), coords.toScreenY(paddleOne.getPosition().y + paddleOne.getButtRadius()));
		paddlepic2.draw(coords.toScreenX(paddleTwo.getPosition().x - paddleTwo.getButtRadius()), coords.toScreenY(paddleTwo.getPosition().y + paddleTwo.getButtRadius()));
		
		// draw the bricks
		bricks.brickDraw(g, coords, brickpic1, brickpic2, brickpic3);
		
		// draw in-flight powerups
		for (Powerup p : powerups)
		{
			switch (p.getType())
			{
			case GOOD:
				pup.draw(coords.toScreenX(p.getPosition().x - ball.getRadius()), coords.toScreenY(p.getPosition().y + ball.getRadius()));
				break;
			case BAD:
				pdown.draw(coords.toScreenX(p.getPosition().x - ball.getRadius()), coords.toScreenY(p.getPosition().y + ball.getRadius()));
				break;
			case NEUTRAL:
				pneutral.draw(coords.toScreenX(p.getPosition().x - ball.getRadius()), coords.toScreenY(p.getPosition().y + ball.getRadius()));
				break;
			default:
				break;
			}
		}
		
		// draw score display
		g.setFont(scoreFont);
		g.setColor(new Color(1.0f, 1.0f, 1.0f));
		g.drawString(scoreP1.toString(), coords.toScreenX(-7.0), coords.toScreenY(4.0));
		g.drawString(scoreP2.toString(), coords.toScreenX(7.0), coords.toScreenY(4.0));
		
		// draw powerup description
		if (isCountDownText)
		{
			if (!countDownText.isZero())
			{
				g.drawString(pupText, coords.toScreenX(0.0 - (pupText.length()*0.15)), coords.toScreenY(3));
			}
		}
		
		// draw round start countdown
		if (!countDown.isZero())
		{
			g.setFont(countDownFont);
			g.setColor(new Color(1.0f, 1.0f, 0.0f));
			g.drawString(countDown.toString(), coords.toScreenX(-0.3), coords.toScreenY(2.0));
		}
		
		g.setFont(scoreFont);
		
		if (!gameEnder.isGameOver())
		{
			gameEnder.render(g, coords);
		}
		else
		{
			if (scoreP1.getCount() > scoreP2.getCount())
			{
				g.drawString("Player One Wins!", coords.toScreenX(-1.0), coords.toScreenY(4.0));
			}
			else if (scoreP2.getCount() > scoreP1.getCount())
			{
				g.drawString("Player Two Wins!", coords.toScreenX(-1.0), coords.toScreenY(4.0));
			}
			else
			{
				g.drawString("It's a Draw!", coords.toScreenX(-1.0), coords.toScreenY(4.0));
			}
		}
	}
	
	public boolean needsInit()
	{
		return !initialized;
	}
	
	/**
	 * Initialize the game.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param ct Coordinate transformer
	 */
	@Override
	public void init(GameContainer gc, CoordinateTransformer ct) throws SlickException
	{
		coords = ct;
		
		// scale factor for scaling the graphics
		float scalefactor = gc.getWidth() / 1920f;
		
		try
		{
			//Loads fonts
            InputStream inputStream = ResourceLoader.getResourceAsStream("assets/data-latin.ttf");
            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont = awtFont.deriveFont(80f); // set font size
            countDownFont = new TrueTypeFont(awtFont, antiAlias);
            
            inputStream = ResourceLoader.getResourceAsStream("assets/data-latin.ttf");
            Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont2 = awtFont2.deriveFont(40f); // set font size
            scoreFont = new TrueTypeFont(awtFont2, antiAlias);
            
            //Loads picture for the ball
            ballpic = new Image(new FileInputStream("assets/images/ball.png"), "ball", false);
            ballpic = ballpic.getScaledCopy(scalefactor);
            
            //Loads picture for the powerup
            pup = new Image(new FileInputStream("assets/images/pup.png"), "pup", false);
            pup = pup.getScaledCopy(scalefactor);
            
            //Loads picture for the powerdown
            pdown = new Image(new FileInputStream("assets/images/pdown.png"), "pdown", false);
            pdown = pdown.getScaledCopy(scalefactor);
            
            //Loads picture for the neutral powerup
            pneutral = new Image(new FileInputStream("assets/images/pneutral.png"), "pneutral", false);
            pneutral = pneutral.getScaledCopy(scalefactor);
            
            //Loads and prepares pictures for the paddles
            paddlepic = new Image(new FileInputStream("assets/images/paddle.png"), "paddle", false);
            paddlepic2 = new Image(new FileInputStream("assets/images/paddle.png"), "paddle", false);
            
    		paddlepic = paddlepic.getScaledCopy(scalefactor);
    		paddlepic2 = paddlepic2.getScaledCopy(scalefactor);		
    		paddlepic.setCenterOfRotation((float)0.139130435*paddlepic.getWidth(), (float)0.503355705*paddlepic.getHeight());
    		paddlepic2.setCenterOfRotation((float)0.139130435*paddlepic.getWidth(), (float)0.503355705*paddlepic.getHeight());
    		originalpaddle = paddlepic;
    		originalpaddle2 = paddlepic2;
            
            //Loads picture for the bricks
            brickpic1 = new Image(new FileInputStream("assets/images/brick1.png"), "brick1", false);
            brickpic2 = new Image(new FileInputStream("assets/images/brick2.png"), "brick2", false);
            brickpic3 = new Image(new FileInputStream("assets/images/brick3.png"), "brick3", false);
            
            brickpic1 = brickpic1.getScaledCopy(scalefactor);
            brickpic2 = brickpic2.getScaledCopy(scalefactor);
            brickpic3 = brickpic3.getScaledCopy(scalefactor);
            
            //Loads sounds
            hit = new Sound(new FileInputStream("assets/sounds/hit.ogg"), "hit.ogg");
            cdown = new Sound(new FileInputStream("assets/sounds/cdown.ogg"), "cdown.ogg");
            bgmusic = new Sound(new FileInputStream("assets/sounds/bullcactus.ogg"), "bullcactus.ogg");
            breaking = new Sound(new FileInputStream("assets/sounds/break.ogg"), "break.ogg");
        }
		catch (Exception e)
		{
            e.printStackTrace();
            System.exit(1);
        }
		
		initialized = true;
		
		beginRound();
	}
	
	/**
	 * Update the state of the game.
	 * 
	 * @param gc Slick2D GameContainer
	 * @param delta Time elapsed since last update
	 * @param input Controllers to read input from
	 */
	public void update(GameContainer gc, int delta, Controller[] input) throws SlickException
	{
		gameEnder.update(this);
		
		// returning from pause?
		if (isPaused)
		{
			isPaused = false;
			beginRound();
			return;
		}
		
		// extend the array of inputs and add the escape key input mapper
		input = Arrays.copyOf(input, input.length + 1);
		input[input.length - 1] = new Controller();
		escapeKeyMapper.mapInput(gc.getInput(), input[input.length - 1]);
		
		// start/repeat the background music
		if (!bgmusic.playing())
		{
			bgmusic.play();
		}
		
		// if ball comes to a halt (probably on the floor), start a new round
		if (!isCountdown && ball.getVelocity().length() < 0.1)
		{
			beginRound();
			return;
		}
		
		// run the powerup description text countdown
		if (isCountDownText)
		{
			countDownText.setCount(2 - (int)countDownTextWatch.getSeconds());
			if (countDownText.isZero())
			{
				isCountDownText = false;
			}
		}
		
		// run the round start countdown
		if (isCountdown)
		{
			// keep the ball still during the countdown
			ball.setPosition(new Vec2(0.0, 3.0));
			ball.setVelocity(new Vec2(0.0, 0.0));
			
			countDown.setCount(3 - (int)countDownWatch.getSeconds());
			
			// start the game
			if (countDown.isZero())
			{
				isCountdown = false;
				
				// shoot the ball to the left or right
				Random rng = new Random();
				
				double xvel = 6 + rng.nextInt(6);
				double yvel = 4 + rng.nextInt(4);
				
				if (rng.nextDouble() > 0.5)
				{
					ball.setVelocity(new Vec2(-xvel, yvel));
				}
				else
				{
					ball.setVelocity(new Vec2(xvel, yvel));
				}
			}
		}
		
		Paddle[] paddles = new Paddle[]{paddleOne, paddleTwo};
		
		// read input
		for (int i = 0; i < input.length; ++i)
		{
			// leave the game
			if (input[i].buttonThree.isPressed())
			{
				bgmusic.stop();
				cdown.stop();
				
				// de-setup cpu paddles
				for (int j = 0; j < 2; ++j)
				{
					if (mainGame.getPlayerInput(j) instanceof CPUInput)
					{
						((CPUInput)mainGame.getPlayerInput(j)).reset();
					}
				}
				
				mainGame.pushScene(new IngameMenuScene(mainGame));
				isPaused = true;
				return;
			}
			
			if (gameEnder.isGameOver())
			{
				continue;
			}
			
			// control the paddles with the first two given controllers
			if (i < 2)
			{
				// paddle movement
				paddles[i].setVelocity(input[i].leftAnalog.getDirection().scale(14.0));
				
				// paddle rotation
				if (input[i].rightAnalog.getMagnitude() > 0.25)
				{
					paddles[i].rotateTowardsAngle(input[i].rightAnalog.getAngle(), 30.0);
				}
				else
				{
					paddles[i].rotateTowardsAngle(paddles[i].getRestingAngle(), 30.0);
				}
				
				// rotation button shortcuts
				if (input[i].buttonOne.isPressed())
				{
					if (paddles[i].getRestingAngle() == Math.PI)
					{
						paddles[i].rotateTowardsAngle(Math.PI / 5.0, 30.0);
					}
					else if (paddles[i].getRestingAngle() == 0.0)
					{
						paddles[i].rotateTowardsAngle(4.0 * Math.PI / 5.0, 30.0);
					}
					
				}
				else if (input[i].buttonTwo.isPressed())
				{
					if (paddles[i].getRestingAngle() == Math.PI)
					{
						paddles[i].rotateTowardsAngle(-Math.PI / 5.0, 30.0);
					}
					else if (paddles[i].getRestingAngle() == 0.0)
					{
						paddles[i].rotateTowardsAngle(-4.0 * Math.PI / 5.0, 30.0);
					}
				}
			}
		}
		
		if (!gameEnder.isGameOver())
		{
			// run physics
			for (int j = 0; j < 10; ++j)
			{
				world.step(1.0/1000.0, new Physics.CollisionAction[]{brickAction, collisionAction});
			}
		}
		
		if (!isCountdown)
		{
			// add ball position to tail
			ballPoints.add(ball.getPosition());
			
			// time to spawn a new brick?
			if (System.currentTimeMillis() > lastBrickSpawned + 5000 && bricks.getRect().shortestVectorToPoint(ball.getPosition()).length() > ball.getRadius())
			{
				bricks.spawnRandomBrick(world);
				lastBrickSpawned = System.currentTimeMillis();
			}
			
			// move and handle powerups (they're not handled by physics at all)
			Iterator<Powerup> it = powerups.iterator();
			
			while (it.hasNext())
			{
				Powerup p = it.next();
				p.move();
				
				if (paddleOne.getShortestVectorToPoint(p.getPosition()).length() < p.getPickupRadius())
				{
					activePowerups.add(p);
					it.remove();
					p.becomePickedUpBy(paddleOne);
					p.applyStaticEffects(paddleOne, paddleTwo, this);
					
					pupText = p.getText();
					displayPupText();
				}
				else if (paddleTwo.getShortestVectorToPoint(p.getPosition()).length() < p.getPickupRadius())
				{
					activePowerups.add(p);
					it.remove();
					p.becomePickedUpBy(paddleTwo);
					p.applyStaticEffects(paddleTwo, paddleOne, this);
					
					pupText = p.getText();
					displayPupText();
				}
			}
		}
	}
	
	/**
	 * Get the world (the physics simulation).
	 * 
	 * @return The physics world
	 */
	public Physics getWorld()
	{
		return world;
	}
	
	/**
	 * Get the ball.
	 * 
	 * @return The ball
	 */
	public Ball getBall()
	{
		return ball;
	}
	
	/**
	 * Destroy the scene.
	 */
	public void destroy()
	{
		mainGame = null;
	}
	
	/**
	 * Configure CPU paddles.
	 * 
	 * @param in CPU input mapper to configure
	 * @param paddle Paddle to assign to CPU
	 */
	public void setupCpuPaddle(CPUInput in, int paddle)
	{
		in.setBall(ball);
		
		if (paddle == 0)
		{
			in.setPaddle(paddleOne);
		}
		else
		{
			in.setPaddle(paddleTwo);
		}
	}
	
	/**
	 * Begin showing a powerup description.
	 */
	public void displayPupText()
	{
		isCountDownText = true;
		countDownText = new Counter();
		countDownText.setCount(2);
	
		countDownTextWatch = new Stopwatch();
		countDownTextWatch.start();
	}
	
	/**
	 * Get the current score.
	 * 
	 * @param player 0 for player one, 1 for player two
	 * @return The current score, or -1 if given player index out of range
	 */
	public int getPlayerScore(int player)
	{
		switch (player)
		{
		case 0:
			return scoreP1.getCount();
		case 1:
			return scoreP2.getCount();
		default:
			return -1;
		}
	}
}
