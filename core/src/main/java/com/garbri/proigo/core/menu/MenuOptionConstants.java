package com.garbri.proigo.core.menu;

public class MenuOptionConstants 
{
	
	//PAUSE MENU OPTIONS
	public static final String changeLevel = "Change Level";
	public static final String numberOfPlayers = "Number Of Players";
	public static final String resetGame = "Reset Game";
	public static final String startServer = "Start Server";
	public static final String startClient = "Start Client";
	
	public static final String RESUME = "Resume";
	public static final String SOUND = "Sound";
	public static final String TEAMSELECT = "Team Select";
	public static final String CONTROLS = "Controls";
	public static final String QUIT = "Quit to Main Menu";
	
	public static final String START = "Start Game";
	public static final String CREDITS = "Credits";
	public static final String EXIT = "Exit Game";
	
	
	//MAIN MENU OPTIONS
	public static final String startGame = "Start Game!";
	public static final String configureControllers = "Configure Controllers";
	public static final String credits = "Credits";
	
	public static final String setPlayerControls = "Set Controls for player ";
	
	
	public static final String done = "Done";
	
	//Timer that needs to elapse between option movements
	public static final float slowDownTimer = 0.2f;
	
	public static enum pauseMenuOption{resume, sound, teamSelect, controls, quit, credits, start, exit}

}
