package com.garbri.proigo.core.controls;

public class GamePadControls implements IControls{
	Boolean left = false;
	Boolean right = false;
	Boolean accelerate = false;
	Boolean brake = false;
	
	Boolean up = false;
	Boolean down = false;
	Boolean start = false;
	
	public Boolean getUp() {
		return up;
	}
	public void setUp(Boolean up) {
		this.up = up;
	}
	public Boolean getDown() {
		return down;
	}
	public void setDown(Boolean down) {
		this.down = down;
	}
	public Boolean getStart() {
		return start;
	}
	public void setStart(Boolean start) {
		this.start = start;
	}	
	
	public Boolean getBrake() {
		return brake;
	}
	public void setBrake(Boolean brake) {
		this.brake = brake;
	}
	public Boolean getLeft() {
		return left;
	}
	public void setLeft(Boolean left) {
		this.left = left;
	}
	public Boolean getRight() {
		return right;
	}
	public void setRight(Boolean right) {
		this.right = right;
	}
	public Boolean getAccelerate() {
		return accelerate;
	}
	public void setAccelerate(Boolean accelerate) {
		this.accelerate = accelerate;
	}

}
