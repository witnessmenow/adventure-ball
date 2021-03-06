package com.garbri.proigo.core.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.collision.CollisionHelper;
import com.garbri.proigo.core.controls.IControls;
import com.garbri.proigo.core.controls.ScreenDebug;
import com.garbri.proigo.core.objects.Ball;
import com.garbri.proigo.core.objects.Maze;
import com.garbri.proigo.core.objects.StartingPosition;
import com.garbri.proigo.core.utilities.SpriteHelper;
import com.garbri.proigo.core.utilities.TextDisplayHelper;
import com.garbri.proigo.core.utilities.TimerHelper;
import com.garbri.proigo.core.vehicles.Car;
import com.garbri.proigo.core.vehicles.Vehicle;

public class RaceScreen extends ScreenDebug {
private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	/**
	 * This is the main box2d "container" object. All bodies will be loaded in
	 * this object and will be simulated through calls to this object.
	 */
	private World world;
	/**
	 * This box2d debug renderer comes from libgdx test code. It draws lines
	 * over all collision boundaries, so it is immensely useful for verifying
	 * that the world collisions are as you expect them to be. It is, however,
	 * slow, so only use it for testing.
	 */
	private Box2DDebugRenderer debugRenderer;	

	private int screenWidth;
	private int screenHeight;	
	private float worldWidth;
	private float worldHeight;
	private static int PIXELS_PER_METER=10;      //how many pixels in a meter
	
	Ball ball;
	
	ArrayList<IControls> controls =  new ArrayList<IControls>() ;
	
	public Maze maze;
	
	Vector2 center;
	
	private SpriteHelper spriteHelper;
	
	private Sprite finishLine;
	
	private TextDisplayHelper textDisplayer;
	
	private Boolean displayWinMessage;
	private String winMessage;
	
	private AdventureBall game;
	
	private TimerHelper timer;
	
	private List<Car> vehicles;
	
	public RaceScreen(AdventureBall game)
	{
		this.game = game;
		
		this.screenWidth = 1400;
		this.screenHeight = 900;
		
		this.worldWidth = this.screenWidth / PIXELS_PER_METER;
		this.worldHeight = this.screenHeight / PIXELS_PER_METER;
		
		this.center = new Vector2(worldWidth/2, worldHeight/2);
		
		this.spriteHelper = new SpriteHelper();
		
		this.textDisplayer = new TextDisplayHelper();
		
	    this.camera = new OrthographicCamera();
	    this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
	    
	    this.debugRenderer = new Box2DDebugRenderer();
		
		this.vehicles = new ArrayList<Car>();
	}

    @Override
	protected void resetGame()
	{
		dispose();
		spriteBatch = new SpriteBatch();
		
		for (Vehicle vehicle:this.vehicles)
		{
			vehicle.destroyVehicle();
		}
		
		this.maze.gameFinished = false;
		this.displayWinMessage = false;
		
		this.timer.resetTimer();
		
		this.timer.startCountDown(3);
		
	    createAllCars();
	}

	
	private void createAllCars()
	{
		
		Car tempCar;
		
		this.vehicles.clear();
		spriteHelper.resetAvailableSprites();
		this.maze.resetStartingPositions();
		
		for( int i = 0; i < this.game.players.size(); i++)
		{
			StartingPosition startingPos = this.maze.getPlayerStartingPosition(this.game.players.get(i).playerTeam);
			
			tempCar = new Car(	this.game.players.get(i).controls,
            					this.game.players.get(i), 
								this.world, 
								startingPos.startingCoords, 
								startingPos.startingAngle,
								spriteHelper.getTeamCarSprite(this.game.players.get(i).playerTeam),
								spriteHelper.getWheelSprite());
			
			this.vehicles.add(tempCar);
		}
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}

	@Override
	public void render(float delta) {
        
		this.game.menuInputs.checkForInputs();

		if(!this.game.pauseOverlay.pauseMenuActive)
	    {
			if (this.game.pauseOverlay.pauseCoolDownActive)
			{
				this.game.pauseOverlay.reduceCoolDown(delta);
			}
			else if (this.game.menuInputs.escapePressed)
	        {
				this.game.pauseOverlay.attemptToPause();
	        }
			
			Gdx.gl.glClearColor(0, 0f, 0f, 1);
	    
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
	
			this.timer.progressTime();
			
		    // tell the camera to update its matrices.
		    camera.update();
		    
			spriteBatch.setProjectionMatrix(camera.combined);
	
			this.spriteBatch.begin();
	
			if (this.timer.countDownTimer == 0)
			{
				
				for (Vehicle vehicle:this.vehicles)
				{
					vehicle.controlVehicle();
				}
				
			}
			else
			{
				if(!this.displayWinMessage)
					textDisplayer.font.draw(spriteBatch, "RACE TO THE MIDDLE TO GAIN THE ADVANTAGE" , (center.x * PIXELS_PER_METER) - ("RACE TO THE MIDDLE TO GAIN THE ADVANTAGE   ".length() * 3) , (center.y - 10f) * PIXELS_PER_METER);
			}
	
			this.ball.update();
			
			if (!this.displayWinMessage)
			{
				//Player 1 wins if both cars reach it at the same time - MNaybe we should randomize this
			
				for (Vehicle vehicle:this.vehicles)
				{
					if (this.maze.checkForWin(vehicle.body.getPosition(), vehicle.player.playerName))
					{
						this.displayWinMessage = true;
						this.winMessage = vehicle.player.getTeamName().toUpperCase() + " TEAM WINS";
						this.timer.startCountDown(3);
					}
				}
				
			}
			
			
			
			
			/**
			 * Have box2d update the positions and velocities (and etc) of all
			 * tracked objects. The second and third argument specify the number of
			 * iterations of velocity and position tests to perform -- higher is
			 * more accurate but is also slower.
			 */
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
			
			world.clearForces();
			
			this.finishLine.setPosition((this.center.x * PIXELS_PER_METER) - this.finishLine.getWidth()/2, (this.center.y * PIXELS_PER_METER) - this.finishLine.getHeight()/2);
			this.finishLine.draw(spriteBatch);
			
			
			for (Vehicle vehicle:this.vehicles)
			{
				vehicle.updateSprite(spriteBatch, PIXELS_PER_METER);
			}
			
			//Update Ball
			SpriteHelper.updateSprite(ball.sprite, spriteBatch, PIXELS_PER_METER, ball.body);
			
			if (this.displayWinMessage)
			{
				textDisplayer.font.draw(spriteBatch, this.winMessage , (center.x * PIXELS_PER_METER) - (this.winMessage.length() * 3) , center.y * PIXELS_PER_METER);
				
				if(this.timer.countDownTimer == 0)
				{
					if(this.winMessage.equals("RED TEAM WINS"))
					{
						this.game.soccerScreen.ballOffsetX = 40f; 
					}
					else
					{
						this.game.soccerScreen.ballOffsetX = -40f; 
					}
					
					this.game.setScreen(this.game.soccerScreen);
					
				}
			}
			
			String temp = this.timer.getElapsedTimeAsString();
	
			textDisplayer.font.draw(spriteBatch, temp , (center.x * PIXELS_PER_METER) - (temp.length() * 3) ,(worldHeight-1f) * PIXELS_PER_METER);
			
			this.spriteBatch.end();
			
			/**
			 * Draw this last, so we can see the collision boundaries on top of the
			 * sprites and map.
			 */
			debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
	    }
		else
		{
			//Pause menu is active 
			
			this.spriteBatch.begin();
			
			this.game.pauseOverlay.renderMenuScreen(delta, spriteBatch);
			
			this.spriteBatch.end();
			
			if(!this.game.pauseOverlay.pauseMenuActive)
			{
				//Game is about to resume
				
				this.timer.resumeTimer();
			}
		}
	}

	@Override
	public void show() {
		
		this.game.activeScreen = this;
		
		this.game.pauseOverlay.setScreenCenter(center, PIXELS_PER_METER);

		//Leaving this here since creating a new world everytime we load might not be a bad idea from a clean up perspictive
		this.world = new World(new Vector2(0.0f, 0.0f), true);
		
		world.setContactListener(this.game.colHelper);
		
		this.maze = new Maze(world, worldWidth, worldHeight, center, 6);
		this.ball = new Ball(world, center.x, center.y, spriteHelper.getBallSprite());

		createAllCars();

		this.spriteBatch = new SpriteBatch();
		
	    this.finishLine = spriteHelper.getFinishLineSprite(20, (int) (worldHeight/(this.maze.numberOfInnerWalls+1))*PIXELS_PER_METER);
	    		

		this.displayWinMessage = false;
		this.timer = new TimerHelper();	
		this.timer.startCountDown(3);
		
		
		
		
	}



}