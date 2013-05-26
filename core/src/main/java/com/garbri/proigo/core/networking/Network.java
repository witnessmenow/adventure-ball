package com.garbri.proigo.core.networking;

import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.garbri.proigo.core.objects.Player;

public class Network {
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(Login.class);
		kryo.register(PlayerInfo.class);
		kryo.register(ListOfPlayers.class);
		kryo.register(GameFull.class);
		
		kryo.register(ClientPlayer.class);
		kryo.register(java.util.ArrayList.class);


	}

	static public class Login {
		public String name;
	}
	
	static public class GameFull {
	}
	
	//This should get returned after a client logs in
	static public class PlayerInfo {
		ClientPlayer player;
	}
	
	static public class ListOfPlayers {
		public List<ClientPlayer> players;
	}


}
