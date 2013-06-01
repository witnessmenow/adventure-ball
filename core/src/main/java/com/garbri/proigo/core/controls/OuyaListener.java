package com.garbri.proigo.core.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector3;

public class OuyaListener implements ControllerListener{
	
	GamePadControls controls = new GamePadControls();
	
	public GamePadControls getControls() {
		Gdx.app.log("OuyaListener", "GetControls - return controls");
		return controls;
	}
	
	private final float DeadZone = 0.2f;
	
	//Looking at the Java file on Github for The Ouya class Ouya.Button_A should be 97, and this is what the UI is sending when the a button is pressed
	//but from testing it seems that Ouya.Button_A is set to 99
	private int brakeButton = Ouya.BUTTON_A;
	private int accelerateButton = Ouya.BUTTON_O;
	private int startButton = 82; //OUYA BUTTON in middle of controller

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
		if(arg1 == Ouya.AXIS_LEFT_X){
			if(arg2 < (-1)*DeadZone){
				controls.left = true;
				controls.right = false;
			} else if(arg2 > DeadZone){
				controls.left = false;
				controls.right = true;
			} else{
				controls.left = false;
				controls.right = false;
			}
		}
		else if (arg1 == Ouya.AXIS_LEFT_Y){
			if(arg2 > DeadZone){
				controls.down = true;
				controls.up = false;
			} else if(arg2 < (-1)*DeadZone){
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
		
		Gdx.app.log("OuyaListener", "ButtonCode: " + String.valueOf(buttonCode));
		
		if(buttonCode == accelerateButton)
		{
			controls.accelerate  = true;
		}
		else if (buttonCode == brakeButton)
		{
			controls.brake = true;
		}
		else if (buttonCode == startButton || buttonCode == 108)
		{
			//This is causing issues on Ouya anyways, start button never seems to be false after clicking start once
			//controls.start = true;
			Gdx.app.log("OuyaListener", "Start is pressed");
		}
		else if (buttonCode == 103)
		{
			controls.rightBumper = true;
		}
		else if (buttonCode == 102)
		{
			controls.leftBumper = true;
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
		else if (buttonCode == 103)
		{
			controls.rightBumper = false;
		}
		else if (buttonCode == 102)
		{
			controls.leftBumper = false;
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
