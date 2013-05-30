package com.garbri.proigo.core.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.controls.GamePadControls;
import com.garbri.proigo.core.controls.IControls;
import com.garbri.proigo.core.controls.KeyboardControls;
import com.garbri.proigo.core.objects.TeamSelectArea;
import com.garbri.proigo.core.utilities.SpriteHelper;
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
	    
	    private AdventureBall game;
	    
	    private TeamSelectArea area;
	
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

        this.vehicles = new ArrayList<Car>();
	}
	    
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0f, 0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        // tell the camera to update its matrices.
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        
        
        world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);

        world.clearForces();
        
        checkForInputs();
        
        for (Vehicle vehicle : this.vehicles) {
            vehicle.controlVehicle();
        }
        
        this.spriteBatch.begin();
        for (Vehicle vehicle : this.vehicles) {
            vehicle.updateSprite(spriteBatch, PIXELS_PER_METER);
        }
        this.spriteBatch.end();
        
        debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		spriteBatch = new SpriteBatch();

        world = new World(new Vector2(0.0f, 0.0f), true);
        
        this.area = new TeamSelectArea(world, worldWidth, worldHeight, center);
		
		createAllCars();
		
		this.activeControllers = new ArrayList<IControls>();
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
