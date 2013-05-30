package com.garbri.proigo.core.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector3;
import com.garbri.proigo.core.controls.mappers.Xbox360WindowsMapper;

public class XboxListener implements ControllerListener {
	
	private final boolean debugLogging = true; 
	
	GamePadControls controls = new GamePadControls();
	
	public GamePadControls getControls() {
		return controls;
	}

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
		
		if(debugLogging)
			Gdx.app.log("XboxListener-DEBUG", "Axis moved: arg1=" + String.valueOf(arg1) + " arg2=" + String.valueOf(arg2));
		
		if(arg1 == Xbox360WindowsMapper.LEFT_ANALOG_X){
			if(arg2 < -0.2){
				controls.left = true;
				controls.right = false;
			} else if(arg2 > 0.2){
				controls.left = false;
				controls.right = true;
			} else{
				controls.left = false;
				controls.right = false;
			}
		} 
		else if (arg1 == Xbox360WindowsMapper.LEFT_ANALOG_Y){
			if(arg2 > 0.2){
				controls.down = true;
				controls.up = false;
			} else if(arg2 < -0.2){
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
		
		if(debugLogging)
			Gdx.app.log("XboxListener-DEBUG", "Button Down: buttonCode=" + String.valueOf(buttonCode));
		
		if(buttonCode == Xbox360WindowsMapper.A_BUTTON)
		{
			controls.accelerate  = true;
		}
		else if (buttonCode == Xbox360WindowsMapper.B_BUTTON)
		{
			controls.brake = true;
		}
		else if (buttonCode == Xbox360WindowsMapper.START_BUTTON)
		{
			controls.start = true;
		}
		
		return true;
	}

	@Override
	public boolean buttonUp(Controller arg0, int buttonCode) {
		
		if(buttonCode == Xbox360WindowsMapper.A_BUTTON)
		{
			controls.accelerate  = false;
		}
		else if (buttonCode == Xbox360WindowsMapper.B_BUTTON)
		{
			controls.brake = false;
		}
		else if (buttonCode == Xbox360WindowsMapper.START_BUTTON)
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

	
	//This is the D-PAD
	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {

		if(debugLogging)
			Gdx.app.log("XboxListener-DEBUG", "POV moved: arg1=" + String.valueOf(arg1) + " arg2=" + String.valueOf(arg2));
		
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
