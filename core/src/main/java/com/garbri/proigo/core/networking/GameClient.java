package com.garbri.proigo.core.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.garbri.proigo.core.networking.Network.Login;

public class GameClient {

	Client client;
	String name;
	
	public GameClient () {
		client = new Client();
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		// ThreadedListener runs the listener methods on a different thread.
		client.addListener(new ThreadedListener(new Listener() {
			public void connected (Connection connection) 
			{

			}

			public void disconnected (Connection connection) {
				System.exit(0);
			}
		}));


		String host = "localhost";
		try {
			client.connect(5000, host, Network.port);
			// Server communication after connection can go here, or in Listener#connected().
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		name = "Client1";
		Login login = new Login();
		login.name = name;
		client.sendTCP(login);

	}
	
	
	
	
}
