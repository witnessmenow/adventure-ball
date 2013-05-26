package com.garbri.proigo.core.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.garbri.proigo.core.networking.Network.ListOfPlayers;
import com.garbri.proigo.core.networking.Network.Login;
import com.garbri.proigo.core.networking.Network.PlayerInfo;
import com.garbri.proigo.core.networking.Network.GameFull;
import com.garbri.proigo.core.objects.Player;


public class GameServer {
	
	private static boolean DEBUG_LOGGING = true;
	
	private static int MAX_PLAYERS=2;
	private boolean[] idPool;
	
	private List<ClientPlayer> players;
	
	Server server;
	//HashSet<ClientPlayer> loggedIn = new HashSet();
	
	public GameServer () throws IOException {
		server = new Server() {
			protected PlayerConnection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new PlayerConnection();
			}
		};
		
		this.idPool = new boolean[MAX_PLAYERS];
		this.players = new ArrayList<ClientPlayer>();
		
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
						if (player != null)
						{
							Gdx.app.log("Netowrking-Server", "This connection is already associated with a player, Sending info in case you forgot");
							
							PlayerInfo playerInfo = new PlayerInfo();
							playerInfo.player = player;
							//Send a client his own information
							c.sendTCP(playerInfo);
							return;
						}
						
						String name = ((Login)object).name;
						Gdx.app.log("Netowrking-Server", "Login attempt from: " + name);
						
						player = new ClientPlayer();
						
						player.name = name;
						player.playerId = getNewPlayerID();
						
						if(player.playerId == -1)
						{
							c.sendTCP(new GameFull());
						}
						
						PlayerInfo playerInfo = new PlayerInfo();
						playerInfo.player = player;
						
						//Send a client his own information
						c.sendTCP(playerInfo);
						
						//NOTE:
						//We may need to introduce sleeps here or something.
						//Do we need to guarantee the above gets received before the below?
						
						
						//Add player to list and send out updated list to all clients
						addPlayerToList(player);
						
						//Everything went ok - keeping this player associated with this connection
						connection.player = player;
						
						return;
		
					}
				}
				
				private boolean isValid (String value) {
					if (value == null) return false;
					value = value.trim();
					if (value.length() == 0) return false;
					return true;
				}

				public void disconnected (Connection c) {
					PlayerConnection connection = (PlayerConnection)c;
					if (connection.player != null) {
						
						Gdx.app.log("Netowrking-Server", "Got a Disconnect from: (name:"+ connection.player.name + ", id:" + connection.player.playerId + ")");
						removePlayerFromList(connection.player);
					}
				}
			});
		
		server.bind(Network.port);
		server.start();
	}
	
	private int getNewPlayerID()
	{
		for(int i = 0; i<MAX_PLAYERS; i++ )
		{
			//IF false, it is a free id
			if(!idPool[i])
			{
				idPool[i] = true;
				return i;
			}
		}
		
		//If we are here there are no Ids left.
		return -1;
	}
	
	private void addPlayerToList(ClientPlayer p)
	{
		Gdx.app.log("Netowrking-Server", "Adding to the player list(name:"+ p.name + ", id:" + p.playerId + ")");
		this.players.add(p);
		
		sendPlayerListToAll();
		
		if(DEBUG_LOGGING)
		{
			logListOfPlayers("add");
		}
	}
	
	private void sendPlayerListToAll()
	{
		ListOfPlayers list = new ListOfPlayers();
		list.players = this.players;
		server.sendToAllTCP(list);
	}
	
	private void logListOfPlayers(String action)
	{
		Gdx.app.log("Netowrking-Server-DEBUG", "After " + action + ", list contains:");
		for(ClientPlayer pl: players)
		{
			Gdx.app.log("Netowrking-Server-DEBUG", "(name:"+ pl.name + ", id:" + pl.playerId + ")");
		}
	}
	
	private void removePlayerFromList(ClientPlayer p)
	{
		
		Gdx.app.log("Netowrking-Server", "Removing player from list(name:"+ p.name + ", id:" + p.playerId + ")");
		this.players.remove(p);
		
		sendPlayerListToAll();
		
		if(DEBUG_LOGGING)
		{
			logListOfPlayers("remove");
		}
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
	

