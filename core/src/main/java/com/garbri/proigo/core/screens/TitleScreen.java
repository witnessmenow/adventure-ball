package com.garbri.proigo.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.objects.Ball;
import com.garbri.proigo.core.objects.Maze;
import com.garbri.proigo.core.utilities.SpriteHelper;
import com.garbri.proigo.core.vehicles.Vehicle;

public class TitleScreen implements Screen{
	
	private int screenWidth;
    private int screenHeight;
    private float worldWidth;
    private float worldHeight;
    private static int PIXELS_PER_METER = 10;
    
    private Vector2 center;
    
    private static String  MESSAGE = "To Begin, Press";
    
    public BitmapFont font;
    
    private World world;
    
    private SpriteBatch spriteBatch;
    
    private AdventureBall game;
    
    private OrthographicCamera camera;
    
    private float messageX;
    private float messageY;
    
    
    
    private Sprite enterButtonSprite;
    
    public TitleScreen(AdventureBall game)
    {
    	this.game = game;
    	
    	this.screenWidth = 1400;
    	this.screenHeight = 900;
    	
    	this.worldWidth = this.screenWidth / PIXELS_PER_METER;
		this.worldHeight = this.screenHeight / PIXELS_PER_METER;
		
		this.center = new Vector2(worldWidth/2, worldHeight/2);
    	
    	this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
        
        this.enterButtonSprite = loadButtonSprite();
        initializeFont();
        
        calculateTextPosition();
        this.enterButtonSprite.setSize(this.font.getBounds(this.MESSAGE).height * 2, this.font.getBounds(this.MESSAGE).height * 2);
        this.enterButtonSprite.setPosition((this.messageX +  this.font.getBounds(this.MESSAGE).width + 10), (screenHeight/4) - (this.enterButtonSprite.getHeight()/2));
    }
    
	private void initializeFont()
	{		
		
    	this.font = new BitmapFont(Gdx.files.internal("Fonts/Const-50.fnt"), Gdx.files.internal("Fonts/Const-50.png"), false);
    	//Make text black
    	this.font.setColor(1f, 1f, 1f, 1.0f);
    	
	}
    
	private Sprite loadButtonSprite()
	{
		if (this.game.isOuya)
		{
			return new Sprite(new Texture(Gdx.files.internal("Images/Buttons/Ouya/OUYA_O.png")));
		}
		else
		{
			return new Sprite(new Texture(Gdx.files.internal("Images/Buttons/Xbox/XBOX_A.png")));
		}
	}
	
	private void calculateTextPosition()
	{
		this.messageX = (screenWidth/2) - (this.font.getBounds(this.MESSAGE).width + 10 + this.enterButtonSprite.getWidth())/2;
		this.messageY = (screenHeight/4) + (this.font.getBounds(this.MESSAGE).height/2);
	}

	@Override
	public void render(float delta) {
		this.game.menuInputs.checkForInputs();
		
		if(!this.game.mainMenuOverlay.pauseMenuActive)
	    {
			
			
			if (this.game.mainMenuOverlay.pauseCoolDownActive)
			{
				this.game.mainMenuOverlay.reduceCoolDown(delta);
			}
			else if (this.game.menuInputs.enterPressed)
	        {
				this.game.mainMenuOverlay.attemptToPause();
	        }
			
			Gdx.gl.glClearColor(0, 0f, 0f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			camera.update();
			
			spriteBatch.setProjectionMatrix(camera.combined);
			
			this.spriteBatch.begin();
			
			this.font.draw(spriteBatch, MESSAGE, this.messageX, this.messageY);
			this.enterButtonSprite.draw(spriteBatch);
			
			this.spriteBatch.end();
			
	    }
		else
		{
			//Pause menu is active 
			
			Gdx.gl.glClearColor(0, 0f, 0f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			this.spriteBatch.begin();
			
			this.game.mainMenuOverlay.renderMenuScreen(delta, spriteBatch);
			
			this.spriteBatch.end();
			
			if(!this.game.mainMenuOverlay.pauseMenuActive)
			{
				//Game is about to resume
			}
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		
		this.game.mainMenuOverlay.setScreenCenter(center, PIXELS_PER_METER);
		
		this.game.mainMenuOverlay.activateCoolDown();

		//Leaving this here since creating a new world everytime we load might not be a bad idea from a clean up perspictive
		this.world = new World(new Vector2(0.0f, 0.0f), true);

		this.spriteBatch = new SpriteBatch();
		
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
