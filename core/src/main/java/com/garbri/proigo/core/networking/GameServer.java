package com.garbri.proigo.core.networking;

import java.io.IOException;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.garbri.proigo.core.networking.Network.AddPlayer;
import com.garbri.proigo.core.networking.Network.Login;


public class GameServer {
	
	Server server;
	HashSet<ClientPlayer> loggedIn = new HashSet();
	
	public GameServer () throws IOException {
		server = new Server() {
			protected PlayerConnection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new PlayerConnection();
			}
		};
		
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);

		server.addListener(new Listener() {
				public void received (Connection c, Object object) {
					// We know all connections for this server are actually CharacterConnections.
					PlayerConnection connection = (PlayerConnection)c;
					ClientPlayer player = connection.player;

					if (object instanceof Login) 
					{
						// Ignore if already logged in.
						if (player != null) return;
						
						String name = ((Login)object).name;
						Gdx.app.log("Netowrking", "Login attempt from: " + name);
						//player = loadCharacter();

//						// Reject if couldn't load character.
//						if (player == null) {
//							c.sendTCP(new RegistrationRequired());
//							return;
//						}
//
//						loggedIn(connection, character);
//						return;
		
					}
				}
				
				private boolean isValid (String value) {
					if (value == null) return false;
					value = value.trim();
					if (value.length() == 0) return false;
					return true;
				}

//				public void disconnected (Connection c) {
//					PlayerConnection connection = (PlayerConnection)c;
//					if (connection.character != null) {
//						loggedIn.remove(connection.character);
//
//						RemoveCharacter removeCharacter = new RemoveCharacter();
//						removeCharacter.id = connection.character.id;
//						server.sendToAllTCP(removeCharacter);
//					}
//				}
			});
		
		server.bind(Network.port);
		server.start();
	}
	
//	void loggedIn (PlayerConnection c, ClientPlayer player) {
//		c.player = player;
//
//		// Add existing characters to new logged in connection.
//		for (ClientPlayer other : loggedIn) {
//			AddPlayer addCharacter = new AddPlayer();
//			addCharacter.player = other;
//			c.sendTCP(addCharacter);
//		}
//
//		loggedIn.add(character);
//
//		// Add logged in character to all connections.
//		AddPlayer addPlayer = new AddPlayer();
//		addPlayer.player = player;
//		server.sendToAllTCP(addPlayer);
//	}
		
	
	
	// This holds per connection state.
	static class PlayerConnection extends Connection {
		public ClientPlayer player;
	}
	

}
	

