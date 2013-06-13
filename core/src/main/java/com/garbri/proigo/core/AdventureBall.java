package com.garbri.proigo.core;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.garbri.proigo.core.collision.CollisionHelper;
import com.garbri.proigo.core.controls.IControls;
import com.garbri.proigo.core.controls.KeyboardControls;
import com.garbri.proigo.core.controls.OuyaListener;
import com.garbri.proigo.core.controls.XboxListener;
import com.garbri.proigo.core.menu.MenuInputsHelper;
import com.garbri.proigo.core.menu.MenuOptionConstants;
import com.garbri.proigo.core.menu.MenuOptionConstants.pauseMenuOption;
import com.garbri.proigo.core.menu.PauseMenuItem;
import com.garbri.proigo.core.menu.MenuOverlay;
import com.garbri.proigo.core.networking.client.GameClient;
import com.garbri.proigo.core.networking.server.GameServer;
import com.garbri.proigo.core.objects.Player;
import com.garbri.proigo.core.screens.TeamSelectScreen;
import com.garbri.proigo.core.screens.NetworkedSoccerScreen;
import com.garbri.proigo.core.screens.RaceScreen;
import com.garbri.proigo.core.screens.SoccerScreen;
import com.garbri.proigo.core.screens.TitleScreen;
import com.garbri.proigo.core.utilities.MusicHelper;

public class AdventureBall extends Game {

    public RaceScreen raceScreen;
    public SoccerScreen soccerScreen;
    public NetworkedSoccerScreen networkedSoccerScreen;
    
    public TeamSelectScreen controllerSelectScreen;
    
    public TitleScreen titleScreen;
    
    public MenuOverlay pauseOverlay;
    public MenuOverlay mainMenuOverlay;

    //Number of players;
    public ArrayList<Player> players;
    public ArrayList<Player> NetworkPlayers;

    public ArrayList<IControls> controls = new ArrayList<IControls>();

    public GameServer gameServer;
    public GameClient gameClient;
    
    public Screen activeScreen;
    
    public MenuInputsHelper menuInputs;
    
    public CollisionHelper colHelper;
    
    public Music music;
    
    public boolean isOuya;

    @Override
    public void create() {
    	
    	this.isOuya = Ouya.runningOnOuya;
    	
    	this.music = MusicHelper.playMenuMusic(music);
    	
    	this.menuInputs = new MenuInputsHelper(this);
    	
        this.initilizeControls();
        
        int numPlayers;
        
        if(this.controls.size() != 4)
        {
        	numPlayers = 2;
        }
        else
        {
        	numPlayers = 4;
        }
        
        createPlayers(numPlayers);
        
        colHelper = new CollisionHelper(Gdx.audio.newSound(Gdx.files.internal("Sound/Effects/ballBounce.mp3")));        
        
        //Init Screens
        raceScreen = new RaceScreen(this);
        soccerScreen = new SoccerScreen(this);
        this.soccerScreen.ballOffsetX = 0f;
        networkedSoccerScreen = new NetworkedSoccerScreen(this);
        
        //Init Menu Screens
        this.titleScreen = new TitleScreen(this);
        createPauseMenu();
        createMainMenu();
        
        controllerSelectScreen = new TeamSelectScreen(this);
        
        setScreen(titleScreen);
    }

    private void createPlayers(int numberOfPlayers) {
        this.players = new ArrayList<Player>();
        Player tempPlayer;

        for (int i = 0; i < numberOfPlayers; i++) {
            tempPlayer = new Player("Player " + String.valueOf(i + 1), this.controls.get(i), i);

            tempPlayer.active = true;

            if (i % 2 == 0) {
                tempPlayer.playerTeam = Player.Team.blue;
            } else {
                tempPlayer.playerTeam = Player.Team.red;
            }

            this.players.add(tempPlayer);
        }
    }

    public void changeNumberPlayers(int numberOfPlayers, Screen screen) {
        
//    	if(numberOfPlayers > this.controls.size())
//    	{
//    		numberOfPlayers = 2;
//    		Gdx.app.log("Main", "Not enough controllers, changing to 2 player");
//    	}
    	
    	createPlayers(numberOfPlayers);
        
    	if(screen != null)
    	{
    		setScreen(screen);
    	}
    }

    public void startServer() {
        try {
            this.gameServer = new GameServer();
        } catch (Exception e) {
            //TODO: Something!
        }
    }

    public void connectToServer() {

        this.NetworkPlayers = new ArrayList<Player>();
        try {
            this.gameClient = new GameClient(this);
        } catch (Exception e) {
            //TODO: Something!
        }

        setScreen(networkedSoccerScreen);
    }

    private void initilizeControls() {
        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log("Main", controller.getName());
            //if(Ouya.ID.equals(controller.getName()))
            if(this.isOuya)
            {
            	Gdx.app.log("Main", "Added Listener for Ouya Controller");
            	
            	OuyaListener listener = new OuyaListener();
                controller.addListener(listener);
                
                controls.add(listener.getControls());
            }
            else
            {
            	XboxListener listener = new XboxListener();
                controller.addListener(listener);
                //listener.getControls();
                controls.add(listener.getControls());
            }
            

        }

        controls.add(new KeyboardControls(Input.Keys.DPAD_UP, Input.Keys.DPAD_DOWN, Input.Keys.DPAD_LEFT, Input.Keys.DPAD_RIGHT, Input.Keys.ENTER, Input.Keys.ESCAPE));
        controls.add(new KeyboardControls(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.ENTER, Input.Keys.ESCAPE));
    }

    public ArrayList<IControls> getControllers()
    {
    	return this.controls;
    }
    
    private void createPauseMenu()
    {
    	ArrayList<PauseMenuItem> items = new ArrayList<PauseMenuItem>();
    	

    	items.add(new PauseMenuItem(MenuOptionConstants.RESUME, pauseMenuOption.resume));
    	items.add(new PauseMenuItem(MenuOptionConstants.TEAMSELECT, pauseMenuOption.teamSelect));
    	items.add(new PauseMenuItem(MenuOptionConstants.CONTROLS, pauseMenuOption.controls));
    	items.add(new PauseMenuItem(MenuOptionConstants.SOUND, pauseMenuOption.sound));
    	items.add(new PauseMenuItem(MenuOptionConstants.QUIT, pauseMenuOption.quit));
    	
    	pauseOverlay = new MenuOverlay(this, items);
    }
    
    private void createMainMenu()
    {
    	ArrayList<PauseMenuItem> items = new ArrayList<PauseMenuItem>();
    	

    	items.add(new PauseMenuItem(MenuOptionConstants.START, pauseMenuOption.start));
    	items.add(new PauseMenuItem(MenuOptionConstants.CREDITS, pauseMenuOption.credits));
    	items.add(new PauseMenuItem(MenuOptionConstants.SOUND, pauseMenuOption.sound));
    	items.add(new PauseMenuItem(MenuOptionConstants.EXIT, pauseMenuOption.exit));
    	
    	mainMenuOverlay = new MenuOverlay(this, items);
    }
}