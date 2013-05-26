package com.garbri.proigo.core.objects;

import com.garbri.proigo.core.controls.IControls;

public class Player {
	
	public IControls controls;
	public String playerName;
	public int playerId;
	public team playerTeam;
	public boolean active;
	
	public Player(String name, IControls controls, int id)
	{
		this.playerName = name;
		this.controls = controls;
		this.playerId = id;
	}
	
	public static enum team{blue, red}
	
	public String getTeamName()
	{
		switch(this.playerTeam)
		{
			case blue:
				return "blue";
			case red:
				return "red";
		}
		
		return "a";
	}

}
