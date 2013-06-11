package com.garbri.proigo.core.utilities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.garbri.proigo.core.objects.Player;

public class SpriteHelper {
	
	private static int CAR_SPRITE_WIDTH = 20;
	private static int CAR_SPRITE_LENGTH = 40;
	private static int CAR_SPRITE_ROWS = 3;
	private static int CAR_SPRITE_COLS = 6;
	
	private Texture carTexture;
	private Texture wheelTexture;
	private Texture ballTexture;
	private Texture finishLineTexture;
	private Texture pitch;
	
	private boolean[][] takenTeamCars;
	
	public SpriteHelper()
	{
		this.carTexture = new Texture(Gdx.files.internal("NewCars.png"));
		this.wheelTexture = new Texture(Gdx.files.internal("Wheel.png"));
		this.ballTexture = new Texture(Gdx.files.internal("Ball.png"));
		this.finishLineTexture = new Texture(Gdx.files.internal("finishLine.png"));
		this.pitch = new Texture(Gdx.files.internal("pitch.png"));
		
		takenTeamCars = new boolean[CAR_SPRITE_ROWS][CAR_SPRITE_COLS];
		
	}
	
	public static void updateSprite(Sprite sprite, SpriteBatch spriteBatch, int PIXELS_PER_METER, Body body)
	{
		setSpritePosition(sprite, PIXELS_PER_METER, body);

		sprite.draw(spriteBatch);
	}
	
	public static void updateSprite(Sprite sprite, SpriteBatch spriteBatch, float x, float y)
	{
		sprite.setPosition(x - sprite.getWidth()/2 , y - sprite.getHeight()/2);
		sprite.draw(spriteBatch);
	}
	
	
	public static void setSpritePosition(Sprite sprite, int PIXELS_PER_METER, Body body)
	{
		
		sprite.setPosition(PIXELS_PER_METER * body.getPosition().x - sprite.getWidth()/2,
				PIXELS_PER_METER * body.getPosition().y  - sprite.getHeight()/2);
		sprite.setRotation((MathUtils.radiansToDegrees * body.getAngle()));
	}
	
	//0=blue,1=red,2=green
	public Sprite getCarSprite(int colour)
	{
		//limit to max number of cars
		if(colour < 0 || colour > 3)
			colour = 0;
		
		return new Sprite(carTexture,(20*colour),0,20, 40);
	}
	
	public Sprite getTeamCarSprite(Player.Team team)
	{
		int row = 1;
		
		switch(team)
		{
			case blue:
				row = 1; //Blue cars are on line 2 of sprite sheet
				break;
			case red:
				row = 2; //Red cars are on line 3 of sprite sheet
				break;
		}
		
		int colour = getNextAvailableSpriteLocation(row);
		
		return new Sprite(carTexture,(CAR_SPRITE_WIDTH*colour), row*CAR_SPRITE_LENGTH , CAR_SPRITE_WIDTH, CAR_SPRITE_LENGTH);
	}
	
	public void resetAvailableSprites()
	{
		takenTeamCars = new boolean[CAR_SPRITE_ROWS][CAR_SPRITE_COLS];
	}
	
	private int getNextAvailableSpriteLocation(int row)
	{
		for(int i = 0; i < CAR_SPRITE_COLS; i++)
		{
			if(!takenTeamCars[row][i])
			{
				takenTeamCars[row][i] = true;
				return i;
			}
		}
		
		return 0;
	}
	
	public Sprite getBallSprite()
	{
		return new Sprite(ballTexture,
							0,  //X
							0,  //Y
							40, //Width
							40 //Height
							);
	}
	
	public Sprite getWheelSprite()
	{
		return new Sprite(wheelTexture,
							0,  //X
							0,  //Y
							4, //Width
							8 //Height
							);
	}
	
	public Sprite getFinishLineSprite(int width, int height)
	{
		return new Sprite(this.finishLineTexture,
				0,  //X
				0,  //Y
				width, //Width
				height //Height
				);
	}
	
	public Sprite getPitchSprite(int width, int height)
	{
		return new Sprite(this.pitch,
				0,  //X
				0,  //Y
				width, //Width
				height //Height
				);
	}
	
	public Sprite loadControllerSprite()
	{
		return new Sprite(new Texture(Gdx.files.internal("Images/Controls/Ouya/controller.png")));
	}
	
	public Sprite loadOverlaySprite()
	{
		return new Sprite(new Texture(Gdx.files.internal("Images/Controls/Ouya/textOverlay.png")));
	}
	
	public Sprite setPositionAdjusted(float x, float y , Sprite sp)
	{
		sp.setPosition(x - (sp.getWidth()/2), y - (sp.getHeight()/2));
		return sp;
	}
	
	
	public Sprite loadCreditsBgSprite()
	{
		return new Sprite(new Texture(Gdx.files.internal("Images/creditsBg.png")));
	}
	
	public Sprite loadCreditsTextSprite()
	{
		return new Sprite(new Texture(Gdx.files.internal("Images/creditsText.png")));
	}
	
	
}
