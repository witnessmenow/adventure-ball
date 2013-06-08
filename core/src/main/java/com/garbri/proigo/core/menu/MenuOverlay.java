package com.garbri.proigo.core.menu;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.menu.MenuOptionConstants.pauseMenuOption;
import com.garbri.proigo.core.menu.PauseMenuItem.optionType;
import com.garbri.proigo.core.screens.TitleScreen;
import com.garbri.proigo.core.utilities.SpriteHelper;

public class MenuOverlay {
	
	private AdventureBall game;
	private BitmapFont optionsFont;
	
	//Font Colours
	private Color highlightedColour;
	private Color unHighlightedColour;
	
	private Sprite menuBackground;
	
	private int selectedOption = 0;
	private ArrayList<PauseMenuItem> menuItems;
	
	public boolean pauseMenuActive;
	
	private float movementCoolDown = 0f;
	private float pauseCoolDown = 0f;
	
	public boolean pauseCoolDownActive;

	private float menuCenterX;
	private float menuCenterY;
	private float menuOptionsX;
	private float menuOptionsY;
	
	public MenuOverlay (AdventureBall game, ArrayList<PauseMenuItem> menuItems)
	{
		this.game = game;
		
		initializeFont();
		
		this.menuBackground = loadMenuSprite();
		
		this.menuItems = menuItems;
		
		
		
	}
	
	private void initializeFont()
	{
		//Red
		this.highlightedColour = new Color(1f, 0f, 0f, 1.0f);
		
		//Green
		this.unHighlightedColour = new Color(0f, 1f, 0f, 1.0f);
		
		
		this.optionsFont = new BitmapFont(Gdx.files.internal("Fonts/arial-32-shadow.fnt"), Gdx.files.internal("Fonts/arial-32-shadow.png"), false);
	}
	
	private Sprite loadMenuSprite()
	{
		Texture menuTexture = new Texture(Gdx.files.internal("Images/MenuBackground.png"));
		return new Sprite(menuTexture);
	}
	
	public void setScreenCenter(Vector2 center, int pixelsPerMeter)
	{
		
		this.menuCenterX = center.x * pixelsPerMeter;
		this.menuCenterY = center.y * pixelsPerMeter;
		
		this.menuOptionsX = menuCenterX - ((this.menuBackground.getWidth()*4)/10);
		
		this.menuOptionsY = menuCenterY + ((this.menuBackground.getHeight()*3)/10);
		
	}
	
	public void renderMenuScreen(float delta, SpriteBatch spriteBatch)
	{
		//Check have users made any inputs
        this.game.menuInputs.checkForInputs();
        
        SpriteHelper.updateSprite(menuBackground, spriteBatch, this.menuCenterX, this.menuCenterY);
        
        displayOptions(spriteBatch);
        
        if(this.movementCoolDown > 0f)
        {
        	this.movementCoolDown -= delta;
        }
        processInputs();
    
        
        
	}
	
	private void processInputs()
	{
		processSelectBackInputs();
		
		if(this.movementCoolDown <= 0f)
		{
			processLeftRightInputs();
			processUpDownInputs();
		}
	}
	
	private void processSelectBackInputs()
	{
		if(this.game.menuInputs.enterPressed)
        {
			if(this.movementCoolDown <= 0f)
        	{
				//Only intesrested in enters if it is a link type
				if(this.menuItems.get(this.selectedOption).type == optionType.link)
				{
					handleLinks();
				}
        	}
        }
        else if(this.game.menuInputs.escapePressed)
        {
        	
        	if(this.movementCoolDown <= 0f)
        	{
        		remumeGame();
        	}
        }
	}
	
	private void processLeftRightInputs()
	{
		//Only intesrested in left right movement if it is a value type
		if(this.menuItems.get(this.selectedOption).type == optionType.value)
		{
			
		}
	}
	
	private void processUpDownInputs()
	{
    	//We are ok to check for movement
    	if(this.game.menuInputs.downPressed)
    	{
    		if(this.selectedOption >= this.menuItems.size()-1)
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
    			this.selectedOption = this.menuItems.size()-1;
    		}
    		else
    		{
    			this.selectedOption--;
    		}
    		this.movementCoolDown = MenuOptionConstants.slowDownTimer;
    	}
	}
	
	private void displayOptions(SpriteBatch spriteBatch)
	{
		this.optionsFont.setColor(this.unHighlightedColour);
        
        for (int i = 0; i < this.menuItems.size(); i++)
        {
        	if(i == this.selectedOption)
        	{
        		this.optionsFont.setColor(this.highlightedColour);
        	}
        	
        	this.optionsFont.draw(spriteBatch, this.menuItems.get(i).displayText, this.menuOptionsX, menuOptionsY - i*40);
        	
        	if(i == this.selectedOption)
        	{
        		this.optionsFont.setColor(this.unHighlightedColour);
        	}
        }
	}
	
	private void handleLinks()
	{
		PauseMenuItem selectedItem = this.menuItems.get(this.selectedOption);
		
		if(selectedItem.type == optionType.link)
		{
			switch(selectedItem.option)
			{
				case resume:
					remumeGame();
					break;
				case quit:
					quitToMenu();
					break;
				case teamSelect:
					goToTeamSelect();
					break;
				case controls:
					displayControls();
					break;
				case sound:
					configureSound();
					break;
				case credits:
					break;
				case exit:
					exitGame();
					break;
				case start:
					goToTeamSelect();
					break;
					
			}
		}
	}
	
	private void remumeGame()
	{
		this.pauseMenuActive  = false;
		this.selectedOption = 0;
		this.movementCoolDown =  MenuOptionConstants.slowDownTimer;
		this.pauseCoolDown = MenuOptionConstants.slowDownTimer;
		this.pauseCoolDownActive = true;
	}
	
	private void closeMenu()
	{
		this.pauseMenuActive  = false;
		this.selectedOption = 0;
		this.movementCoolDown =  MenuOptionConstants.slowDownTimer;
	}
	
	public void activateCoolDown()
	{
		this.pauseCoolDown = MenuOptionConstants.slowDownTimer;
		this.pauseCoolDownActive = true;
	}
	
	private void quitToMenu()
	{
		closeMenu();
		this.game.setScreen(this.game.titleScreen);
	}
	
	private void goToTeamSelect()
	{
		closeMenu();
		if(this.game.getScreen() != this.game.controllerSelectScreen)
		{
			this.game.setScreen(this.game.controllerSelectScreen);
		}
	}
	
	private void exitGame()
	{
		
		//TODO: At some stage an are you sure prompt should be added
		Gdx.app.exit();
	}
	
	private void displayControls()
	{
		
	}
	
	private void configureSound()
	{
		
	}
	
	public void reduceCoolDown(float delta)
	{
		if(this.pauseCoolDown > 0f)
        {
        	this.pauseCoolDown -= delta;
        }
		else
		{
			this.pauseCoolDownActive = false;
		}
	}
	
	public void attemptToPause()
	{
		this.pauseMenuActive = true;
		this.movementCoolDown =  MenuOptionConstants.slowDownTimer;
		this.selectedOption = 0;
	}
	
}
