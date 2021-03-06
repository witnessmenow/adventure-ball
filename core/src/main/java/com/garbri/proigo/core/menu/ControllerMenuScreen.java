package com.garbri.proigo.core.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.controls.GenericControllerListener;
import com.garbri.proigo.core.screens.SoccerScreen;
import com.garbri.proigo.core.utilities.TextDisplayHelper;

public class ControllerMenuScreen implements Screen{

	private int screenWidth;
    private int screenHeight;
    private float worldWidth;
    private float worldHeight;
    private static int PIXELS_PER_METER = 10;
    
    private World world;
    
    private SpriteBatch spriteBatch;
    
    private TextDisplayHelper textDisplayer;
    public BitmapFont font;
	private OrthographicCamera camera;
	
	private List<String> menuOptions;
	
	private int selectedOption = 0;
	
	private AdventureBall game;
	
	private float movementCoolDown = 0f;
	
    public ControllerMenuScreen(AdventureBall game)
    {
    	this.game = game;
    	
    	
    	
    	this.font = new BitmapFont(Gdx.files.internal("Fonts/Const-50.fnt"), Gdx.files.internal("Fonts/Const-50.png"), false);
    	//Make text black
    	this.font.setColor(1f, 0f, 0f, 1.0f);
    	
    	this.screenWidth = 800;
    	this.screenHeight = 600;
    	
    	this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
        
        menuOptions = new ArrayList<String>(); 
        
        for(int i = 1; i <=4; i++)
        {
        	menuOptions.add(MenuOptionConstants.setPlayerControls + i);
        }
        menuOptions.add(MenuOptionConstants.done);
        

     }
    
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        
        //Check have users made any inputs
        this.game.menuInputs.checkForInputs();
        
        if(this.game.menuInputs.enterPressed)
        {
        	handleSelection();
        }
        else if(this.game.menuInputs.escapePressed)
        {
        	//Return to current screen, keeping state
        }
        
        if(this.movementCoolDown <= 0f)
        {
        	//We are ok to check for movement
        	if(this.game.menuInputs.downPressed)
        	{
        		if(this.selectedOption >= this.menuOptions.size()-1)
        		{
        			this.selectedOption = 0;
        		}
        		else
        		{
        			this.selectedOption++;
        		}
        		this.movementCoolDown = MenuOptionConstants.slowDownTimer;
       		}
        	else if(this.game.menuInputs.upPressed)
        	{
        		if(this.selectedOption == 0)
        		{
        			this.selectedOption = this.menuOptions.size()-1;
        		}
        		else
        		{
        			this.selectedOption--;
        		}
        		this.movementCoolDown = MenuOptionConstants.slowDownTimer;
        	}
        }
        else
        {
        	this.movementCoolDown -= delta;
        }
        
        this.spriteBatch.begin();
        int menuStart = this.screenHeight - 100;
        this.font.setColor(1f, 0f, 0f, 1.0f);
        
        for (int i = 0; i < this.menuOptions.size(); i++)
        {
        	if(i == this.selectedOption)
        	{
        		this.font.setColor(0f, 1f, 0f, 1.0f);
        	}
        	
        	this.font.draw(spriteBatch, this.menuOptions.get(i), 150, menuStart - i*50);
        	
        	if(i == this.selectedOption)
        	{
        		this.font.setColor(1f, 0f, 0f, 1.0f);
        	}
        }
        
        this.spriteBatch.end();
		
	}

	private void handleSelection() {
		
		String selectedOptionString = this.menuOptions.get(this.selectedOption);
		
		if(this.selectedOption < 4)
		{
			setControls(this.selectedOption);
		}
		else if(selectedOptionString.equals(MenuOptionConstants.configureControllers))
		{
			
		}

		// TODO Auto-generated method stub
		
		
		this.game.setScreen(this.game.activeScreen);
	}
	
	private GenericControllerListener setControls(int playerNumber)
	{
		if(this.game.players.size() <= playerNumber)
		{
			//throw error, or something!
		}
		
		GenericControllerListener listener = new GenericControllerListener();
		
		Controllers.addListener(listener);
		
		return listener;
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		spriteBatch = new SpriteBatch();
		
		this.world = new World(new Vector2(0.0f, 0.0f), true);
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

}
