package com.garbri.proigo.core.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector3;

public class GenericControllerListener implements ControllerListener{
	
	private final boolean debugLogging = true;
	
	GamePadControls controls = new GamePadControls();
	
	public GamePadControls getControls() {
		Gdx.app.log("GenericListener", "GetControls - return controls");
		return controls;
	}
	
	
	public GenericControllerListener()
	{
		setController = false;
		setX = false;
		setY = false;
		setBrakeButton = false;
		setAccelerateButton = false;
		setStartButton = false;
	}
	
	public boolean setController;
	public boolean setX;
	public boolean setY;
	public boolean setBrakeButton;
	public boolean setAccelerateButton;
	public boolean setStartButton;
	
	private int xAxis;
	private int yAxis;
	
	private int brakeButton;
	private int accelerateButton;
	private int startButton; 
	
	private Controller controller;

	public void setControls(GamePadControls controls) {
		this.controls = controls;
	}

	@Override
	public boolean accelerometerMoved(Controller arg0, int arg1, Vector3 arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean axisMoved(Controller arg0, int arg1, float arg2) {
		
		if(setX)
		{
			if(arg2 < -0.15 || (arg2 > 0.15))
			{
				xAxis = arg1;
			}
		}
		else if(setY)
		{
			if(arg2 < -0.15 || (arg2 > 0.15))
			{
				xAxis = arg1;
			}
		}
		
		if(arg1 == xAxis){
			if(arg2 < -0.15){
				controls.left = true;
				controls.right = false;
			} else if(arg2 > 0.15){
				controls.left = false;
				controls.right = true;
			} else{
				controls.left = false;
				controls.right = false;
			}
		}
		else if (arg1 == yAxis){
			if(arg2 > 0.15){
				controls.down = true;
				controls.up = false;
			} else if(arg2 < -0.15){
				controls.down = false;
				controls.up = true;
			} else{
				controls.down = false;
				controls.up = false;
			}
		}
		
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int buttonCode) {
		
		Gdx.app.log("GenericListener", "ButtonCode: " + String.valueOf(buttonCode));
		
		if(setController)
		{
			this.controller = arg0;
		}
		
		if(setBrakeButton)
		{
			brakeButton = buttonCode;
		}
		else if (setAccelerateButton)
		{
			accelerateButton = buttonCode;
		}
		else if (setStartButton)
		{
			startButton = buttonCode;
		}
			
		
		if(buttonCode == accelerateButton)
		{
			controls.accelerate  = true;
		}
		else if (buttonCode == brakeButton)
		{
			controls.brake = true;
		}
		else if (buttonCode == startButton)
		{
			controls.start = true;
		}
		
		return true;
	}

	@Override
	public boolean buttonUp(Controller arg0, int buttonCode) {
		
		if(buttonCode == accelerateButton)
		{
			controls.accelerate  = false;
		}
		else if (buttonCode == brakeButton)
		{
			controls.brake = false;
		}
		else if (buttonCode == startButton)
		{
			controls.start = false;
		}
		return true;
	}

	@Override
	public void connected(Controller arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected(Controller arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller arg0, int arg1, boolean arg2) {
		controls.left = true;
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
