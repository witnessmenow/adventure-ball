package com.garbri.proigo.core.collision;

import com.garbri.proigo.core.objects.Player;
import com.garbri.proigo.core.objects.Player.Team;

public class CollisionInfo {
	
	public String text;
	public CollisionObjectType type;
	public Team team;
	
	
	public CollisionInfo(String text, CollisionObjectType type)
	{
		this.text = text;		
		this.type = type;
		this.team = null;
	}
	
	public CollisionInfo(String text, CollisionObjectType type, Team team)
	{
		this.text = text;		
		this.type = type;
		this.team = team;
	}
	
	public static enum CollisionObjectType{car, ball, wall};

}
