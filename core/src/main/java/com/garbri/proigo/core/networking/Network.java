package com.garbri.proigo.core.networking;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(Login.class);
		kryo.register(PlayerInfo.class);
		kryo.register(ListOfPlayers.class);
		kryo.register(GameFull.class);
		kryo.register(UpdateBall.class);
		
		kryo.register(ClientPlayer.class);
		kryo.register(java.util.ArrayList.class);
		kryo.register(com.badlogic.gdx.math.Vector2.class);


	}

	static public class Login {
		public String name;
	}
	
	static public class UpdateBall {
		public Vector2 position;
	}
	
	static public class GameFull {
	}
	
	//This should get returned after a client logs in
	static public class PlayerInfo {
		public ClientPlayer player;
	}
	
	static public class ListOfPlayers {
		public List<ClientPlayer> players;
	}


}
