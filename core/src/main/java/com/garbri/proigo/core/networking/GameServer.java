package com.garbri.proigo.core.networking;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.garbri.proigo.core.networking.Network.UpdateBall;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.garbri.proigo.core.networking.Listeners.PlayerListener;

public class GameServer {

	private static boolean DEBUG_LOGGING = true;

	private static int MAX_PLAYERS=2;
	private Boolean[] idPool;

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

		this.idPool = new Boolean[MAX_PLAYERS];
		this.players = new ArrayList<ClientPlayer>();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);

		server.addListener(new PlayerListener(server,this.idPool,MAX_PLAYERS,this.players));

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
    public void updateGame(Vector2 ballPosition)
    {
        UpdateBall ball = new UpdateBall();
        ball.position = ballPosition;
        server.sendToAllTCP(ball);
    }


}


