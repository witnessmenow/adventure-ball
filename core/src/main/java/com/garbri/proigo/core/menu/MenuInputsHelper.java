package com.garbri.proigo.core.menu;

import com.badlogic.gdx.Gdx;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.controls.GamePadControls;
import com.garbri.proigo.core.controls.IControls;
import com.garbri.proigo.core.controls.KeyboardControls;
import com.garbri.proigo.core.vehicles.Vehicle;

public class MenuInputsHelper {
	
	AdventureBall game;
	
	public boolean downPressed;
	public boolean upPressed;
	public boolean enterPressed;
	public boolean escapePressed;
	
	public MenuInputsHelper(AdventureBall game)
	{
		this.game = game;
		resetInputs();
	}
	
	public void resetInputs()
	{
		downPressed = false;
		upPressed = false;
		enterPressed = false;
		escapePressed = false;
	}
	
	public void checkForInputs()
	{
		//Clearing out booleans from last check
		resetInputs();
		
		for(IControls cont: this.game.getControllers())
		{
			if(cont instanceof GamePadControls)
			{
				ControlPadRead((GamePadControls)cont);
			}	
			else if (cont instanceof KeyboardControls)
			{
				keyboardControlRead((KeyboardControls)cont); 

			}
		}
		
	}
	
	private void ControlPadRead(GamePadControls gamePadControls)
	{
		//Read Up/Down
		if (gamePadControls.getUp())
			this.upPressed = true;
		else if (gamePadControls.getDown())
			this.downPressed = true;
		
		// Read enter/escape
		if (gamePadControls.getAccelerate())
			this.enterPressed = true;
		else if (gamePadControls.getStart())
		{
			Gdx.app.log("MenuInputsHelper", "Start is pressed");
			this.escapePressed = true;
		}

	}
	
	private void keyboardControlRead(KeyboardControls keyboardControls)
	{
		
		//Read Up/Down
		if (Gdx.input.isKeyPressed(keyboardControls.controlUp))
			this.upPressed = true;
		else if (Gdx.input.isKeyPressed(keyboardControls.controlDown))
			this.downPressed = true;
		
		// Read enter/escape
		if (Gdx.input.isKeyPressed(keyboardControls.enter))
			this.enterPressed = true;
		else if (Gdx.input.isKeyPressed(keyboardControls.pause))
			this.escapePressed = true;
		

	}
	
	

}
