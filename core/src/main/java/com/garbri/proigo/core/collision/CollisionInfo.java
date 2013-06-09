package com.garbri.proigo.core.collision;

public class CollisionInfo {
	
	public String text;
	public CollisionObjectType type;
	
	
	public CollisionInfo(String text, CollisionObjectType type)
	{
		this.text = text;		
		this.type = type;
	}
	
	public static enum CollisionObjectType{car, ball, wall};

}
