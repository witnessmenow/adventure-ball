package com.garbri.proigo.core.networking;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.garbri.proigo.core.proigo;
import com.garbri.proigo.core.networking.Network.ListOfPlayers;
import com.garbri.proigo.core.networking.Network.Login;
import com.garbri.proigo.core.networking.Network.PlayerInfo;
import com.garbri.proigo.core.networking.Network.UpdateBall;
import com.garbri.proigo.core.objects.Player;

public class GameClient {

	UI ui;
	Client client;
	String name;
	private proigo gameObj;
	
	public GameClient (proigo game) {
		client = new Client();
		client.start();
		
		this.gameObj = game;

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		// ThreadedListener runs the listener methods on a different thread.
		client.addListener(new ThreadedListener(new Listener() {
			public void connected (Connection connection) 
			{

			}

			public void received (Connection connection, Object object) {
				if (object instanceof PlayerInfo) {
					PlayerInfo playerInfo = (PlayerInfo)object;
					
					Player player = gameObj.players.get(0);
					player.playerName = playerInfo.player.name;
					player.playerId = playerInfo.player.playerId;
					player.setTeamBasedOnId();
					return;
				}
				
				if (object instanceof UpdateBall) {
					
					UpdateBall ball = (UpdateBall)object;
					gameObj.networkSoccer.networkBallPosition = ball.position;
					return;
				}

				if (object instanceof ListOfPlayers) {

					return;
				}

			}
			
			public void disconnected (Connection connection) {
				System.exit(0);
			}
		}));


		ui = new UI();

		String host = ui.inputHost();
		try {
			client.connect(5000, host, Network.port);
			// Server communication after connection can go here, or in Listener#connected().
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		name = ui.inputName();
		Login login = new Login();
		login.name = name;
		client.sendTCP(login);

	}

	
	
	static class UI {
		HashMap<Integer, Character> characters = new HashMap();

		public String inputHost () {
			String input = (String)JOptionPane.showInputDialog(null, "Host:", "Connect to server", JOptionPane.QUESTION_MESSAGE,
				null, null, "localhost");
			if (input == null || input.trim().length() == 0) System.exit(1);
			return input.trim();
		}

		public String inputName () {
			String input = (String)JOptionPane.showInputDialog(null, "Name:", "Connect to server", JOptionPane.QUESTION_MESSAGE,
				null, null, "Test");
			if (input == null || input.trim().length() == 0) System.exit(1);
			return input.trim();
		}

	}
	
	
	
	
}
