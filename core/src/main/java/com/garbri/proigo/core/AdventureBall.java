package com.garbri.proigo.core;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.garbri.proigo.core.controls.IControls;
import com.garbri.proigo.core.controls.KeyboardControls;
import com.garbri.proigo.core.controls.XboxListener;
import com.garbri.proigo.core.networking.GameClient;
import com.garbri.proigo.core.networking.GameServer;
import com.garbri.proigo.core.objects.Player;

public class AdventureBall extends Game {

    public RaceScreen raceScreen;
    public SoccerScreen soccerScreen;
    public NetworkedSoccerScreen networkedSoccerScreen;

    //Number of players;
    public ArrayList<Player> players;
    public ArrayList<Player> NetworkPlayers;

    ArrayList<IControls> controls = new ArrayList<IControls>();

    public GameServer gameServer;
    public GameClient gameClient;

    @Override
    public void create() {

        this.initilizeControls();
        int numPlayers = 4;
        createPlayers(numPlayers);
        raceScreen = new RaceScreen(this);
        soccerScreen = new SoccerScreen(this);
        networkedSoccerScreen = new NetworkedSoccerScreen(this);
        this.soccerScreen.ballOffsetX = 0f;

        setScreen(raceScreen);


    }

    private void createPlayers(int numberOfPlayers) {
        this.players = new ArrayList<Player>();
        Player tempPlayer;


        for (int i = 0; i < numberOfPlayers; i++) {
            tempPlayer = new Player("Player " + String.valueOf(i + 1), this.controls.get(i), i);

            tempPlayer.active = true;

            if (i % 2 == 0) {
                tempPlayer.playerTeam = Player.team.blue;
            } else {
                tempPlayer.playerTeam = Player.team.red;
            }

            this.players.add(tempPlayer);
        }
    }

    public void changeNumberPlayers(int numberOfPlayers, Screen screen) {
        createPlayers(numberOfPlayers);
        setScreen(screen);
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
            XboxListener listener = new XboxListener();
            controller.addListener(listener);
            listener.getControls();
            controls.add(listener.getControls());

        }

        controls.add(new KeyboardControls(Input.Keys.DPAD_UP, Input.Keys.DPAD_DOWN, Input.Keys.DPAD_LEFT, Input.Keys.DPAD_RIGHT));
        controls.add(new KeyboardControls(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D));
        controls.add(new KeyboardControls(Input.Keys.DPAD_UP, Input.Keys.DPAD_DOWN, Input.Keys.DPAD_LEFT, Input.Keys.DPAD_RIGHT));
        controls.add(new KeyboardControls(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D));
    }

}