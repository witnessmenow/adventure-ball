package com.garbri.proigo.core.menu;

import com.garbri.proigo.core.menu.MenuOptionConstants.pauseMenuOption;

public class PauseMenuItem {

	public String displayText;
	public pauseMenuOption option;
	public float value;
	public optionType type;
	
	
	//Constructor for Link items
	public PauseMenuItem(String display, pauseMenuOption option)
	{
		this.displayText = display;
		this.option = option;
		this.type = optionType.link;
	}
	
	//Constructor for Value items
	public PauseMenuItem(String display, float value)
	{
		this.displayText = display;
		this.value = value;
		this.type = optionType.value;
	}
	
	public static enum optionType{link, value}
	
}
