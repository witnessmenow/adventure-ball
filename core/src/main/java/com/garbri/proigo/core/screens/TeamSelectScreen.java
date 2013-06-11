package com.garbri.proigo.core.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.collision.CollisionHelper;
import com.garbri.proigo.core.controls.GamePadControls;
import com.garbri.proigo.core.controls.IControls;
import com.garbri.proigo.core.controls.KeyboardControls;
import com.garbri.proigo.core.objects.Player;
import com.garbri.proigo.core.objects.TeamSelectArea;
import com.garbri.proigo.core.utilities.MusicHelper;
import com.garbri.proigo.core.utilities.SpriteHelper;
import com.garbri.proigo.core.utilities.TimerHelper;
import com.garbri.proigo.core.vehicles.Car;
import com.garbri.proigo.core.vehicles.Vehicle;

public class TeamSelectScreen implements Screen{

	 private OrthographicCamera camera;
	    private SpriteBatch spriteBatch;

	    private World world;

	    private Box2DDebugRenderer debugRenderer;

	    private int screenWidth;
	    private int screenHeight;
	    private float worldWidth;
	    private float worldHeight;
	    private static int PIXELS_PER_METER = 10;      //how many pixels in a meter
	    private Vector2 center;
	    
	    
	    private List<Car> vehicles;
	    private List<IControls> activeControllers;
	    
	    private SpriteHelper spriteHelper;
	    
	    private Sprite controllerSprite;
	    private Sprite textOverlaySprite;
	    
	    private AdventureBall game;
	    
	    private TeamSelectArea area;
	    
	    public BitmapFont font;
	    
	    private boolean gotReadyFlag;
	    
	    private TimerHelper timer;
	    
	
	public TeamSelectScreen(AdventureBall game) 
	{
		this.game = game;
		
        this.screenWidth = 1400;
        this.screenHeight = 900;
        
        this.worldWidth = this.screenWidth / PIXELS_PER_METER;
        this.worldHeight = this.screenHeight / PIXELS_PER_METER;

        this.center = new Vector2(worldWidth / 2, worldHeight / 2);
        
        this.spriteHelper = new SpriteHelper();
        
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);

        this.debugRenderer = new Box2DDebugRenderer();
        
        this.font = new BitmapFont(Gdx.files.internal("Fonts/Const-50.fnt"), Gdx.files.internal("Fonts/Const-50.png"), false);

        this.vehicles = new ArrayList<Car>();
        
        this.controllerSprite = this.spriteHelper.loadControllerSprite();
        this.textOverlaySprite = this.spriteHelper.loadOverlaySprite();
        
        this.controllerSprite = this.spriteHelper.setPositionAdjusted(this.screenWidth/2, 3*(this.screenHeight/4), this.controllerSprite);
        this.textOverlaySprite = this.spriteHelper.setPositionAdjusted(this.screenWidth/2, 3*(this.screenHeight/4), this.textOverlaySprite);
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
	        
	        // tell the camera to update its matrices.
	        camera.update();
	        spriteBatch.setProjectionMatrix(camera.combined);
	        
	        this.timer.progressTime();
	        
	        world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
	
	        world.clearForces();
	        
	        if(this.timer.elapsedTimeInSeconds > 1)
	        {
	        	checkForInputs();
	        }
	        
	        for (Vehicle vehicle : this.vehicles) {
	            vehicle.controlVehicle();
	        }
	        
	        this.area.checkTeams(vehicles);
	        
	        this.spriteBatch.begin();
	        
	        this.controllerSprite.draw(spriteBatch);
	        this.textOverlaySprite.draw(spriteBatch);
	        
	        this.font.setColor(1f, 1f, 1f, 1.0f);
	        
	        String message = "Join A Team!";
	        
	        this.font.draw(spriteBatch, message, this.screenWidth/2 - this.font.getBounds(message).width/2, this.screenHeight/4);
	        
	        this.area.displayNumbersInTeam(font, spriteBatch);
	        
	        for (Vehicle vehicle : this.vehicles) {
	            vehicle.updateSprite(spriteBatch, PIXELS_PER_METER);
	        }
	        this.spriteBatch.end();
	        
	        
	        
	        debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
		
	        if(this.gotReadyFlag)
	        {
	        	startGame();
	        }
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
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	private void startGame()
	{
		int numPlayers = this.activeControllers.size();
		
		this.game.changeNumberPlayers(numPlayers, null);
		
		for (int i = 0; i < numPlayers; i++)
		{
			this.game.players.get(i).controls = this.activeControllers.get(i);
			
			Vehicle vehicle = getVehicleFromController(this.activeControllers.get(i));
			
			if(this.area.checkVehicleInBlue(vehicle))
			{
				this.game.players.get(i).playerTeam = Player.team.blue;
			}
			else
			{
				this.game.players.get(i).playerTeam = Player.team.red;
			}
		}
		
		this.game.music = MusicHelper.playGameMusic(this.game.music);
		this.game.setScreen(this.game.raceScreen);
	}
	
	private Vehicle getVehicleFromController(IControls control)
	{
		for(Vehicle vehicle : this.vehicles)
		{
			if (vehicle.controls.equals(control))
			{
				return vehicle;
			}
		}
		
		return null;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		this.gotReadyFlag = false;
		
		this.game.pauseOverlay.setScreenCenter(center, PIXELS_PER_METER);
		
		spriteBatch = new SpriteBatch();

        world = new World(new Vector2(0.0f, 0.0f), true);
        
        world.setContactListener(this.game.colHelper);
        
        this.area = new TeamSelectArea(world, worldWidth, worldHeight, center);
		
		createAllCars();
		
		this.activeControllers = new ArrayList<IControls>();
		
		this.timer = new TimerHelper();
	}
	
	public void checkForInputs()
	{
		
		for(IControls cont: this.game.getControllers())
		{
			//Only interested in controllers that dont already have a car
			if(!this.activeControllers.contains(cont))
			{
				if(cont instanceof GamePadControls)
				{
					if(((GamePadControls) cont).getAccelerate())
					{
						addControlsToVehicle(cont);
					}
				}	
				else if (cont instanceof KeyboardControls)
				{
					KeyboardControls keyCont = (KeyboardControls)cont;
					
					if(Gdx.input.isKeyPressed(keyCont.controlUp))
					{
						addControlsToVehicle(cont);
					}  
	
				}
			}
			else
			{
				//These are active controllers
				if(cont instanceof GamePadControls)
				{
					if (((GamePadControls) cont).getRightBumper())
					{
						this.gotReadyFlag = true;
					}
				}	
				else if (cont instanceof KeyboardControls)
				{
					KeyboardControls keyCont = (KeyboardControls)cont;
					
					//If keyboard exited menu they pressed enter for that
					if(!this.game.pauseOverlay.pauseCoolDownActive)
					{
						if(Gdx.input.isKeyPressed(keyCont.enter))
						{
							this.gotReadyFlag = true;
						}
					}
	
				}
				
			}
		}
		
	}
	
	private void addControlsToVehicle(IControls cont)
	{
		for (Vehicle vehicle : this.vehicles) {
            if(vehicle.controls == null)
            {
            	vehicle.controls = cont;
            	this.activeControllers.add(cont);
            	return;
            }
        }
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private void createAllCars()
	{
		
		Car tempCar;
		
		this.vehicles.clear();
		spriteHelper.resetAvailableSprites();
			
		for( int i = 0; i < 4; i++)
		{
			
			tempCar = new Car(	null,
            					null, 
								this.world, 
								this.area.getStartPosition(i), 
								0f,
								spriteHelper.getCarSprite(i),
								spriteHelper.getWheelSprite());
			
			this.vehicles.add(tempCar);
		}
	}

}
