package com.garbri.proigo.core.controls;

public class KeyboardControls implements IControls {

	public int controlUp;
	public int controlDown;
	public int controlLeft;
	public int controlRight;
	
	public int enter;
	public int pause;
	
	
	public KeyboardControls(int controlUp, int controlDown, int controlLeft,
			int controlRight, int enter, int pause) {
		super();
		this.controlUp = controlUp;
		this.controlDown = controlDown;
		this.controlLeft = controlLeft;
		this.controlRight = controlRight;
		this.enter = enter;
		this.pause = pause;
	}
}
