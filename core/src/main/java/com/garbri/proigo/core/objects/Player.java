package com.garbri.proigo.core.objects;

import com.garbri.proigo.core.controls.IControls;

public class Player {
	
	public IControls controls;
	public String playerName;
	public int playerId;
	public Team playerTeam;
	public boolean active;
	
	public Player(String name, IControls controls, int id)
	{
		this.playerName = name;
		this.controls = controls;
		this.playerId = id;
	}
	
	public static enum Team{blue, red}
	
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
	
	public void setTeamBasedOnId()
	{
		if (this.playerId%2 == 0)
		{
			this.playerTeam = Player.Team.blue;
		}
		else
		{
			this.playerTeam = Player.Team.red;
		}
	}

}
