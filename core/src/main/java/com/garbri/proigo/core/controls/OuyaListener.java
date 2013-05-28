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
	
	private int brakeButton = 97;
	private int accelerateButton = Ouya.BUTTON_O;

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
		
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int buttonCode) {
		
		Gdx.app.log("OuyaListener", "ButtonCode: " + String.valueOf(buttonCode));
		
		if(buttonCode == accelerateButton){
		controls.accelerate  = true;}
		else if (buttonCode == brakeButton){
			//Looking at the Java file on Github for The Ouya class Ouya.Button_A should be 97, and this is what the UI is sending when the a button is pressed
			//but from testing it seems that Ouya.Button_A is set to 99
			controls.brake = true;
		}
		
		return true;
	}

	@Override
	public boolean buttonUp(Controller arg0, int buttonCode) {
		if(buttonCode == accelerateButton){
			controls.accelerate  = false;}
			else if (buttonCode == brakeButton){
				controls.brake = false;
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
