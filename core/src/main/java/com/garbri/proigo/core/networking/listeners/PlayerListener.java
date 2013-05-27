package com.garbri.proigo.core.networking.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.garbri.proigo.core.networking.ClientPlayer;
import com.garbri.proigo.core.networking.Network;
import com.garbri.proigo.core.networking.server.GameServer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: conor
 * Date: 5/27/13
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerListener extends Listener {
    private Boolean DEBUG_LOGGING = true;
    private List<ClientPlayer> players;
    private Server server;
    private GameServer gameServer;

    public PlayerListener(Server server, List<ClientPlayer> players, GameServer gameServer) {

        this.players = players;
        this.server = server;
        this.gameServer = gameServer;

    }

    @Override
    public void received(Connection c, Object object) {
        // We know all connections for this server are actually CharacterConnections.
    	GameServer.PlayerConnection connection = (GameServer.PlayerConnection) c;
        ClientPlayer player = connection.player;

        if (object instanceof Network.Login) {
            // Ignore if already logged in.
            if (player != null) {
                Gdx.app.log("Netowrking-Server", "This connection is already associated with a player, Sending info in case you forgot");

                Network.PlayerInfo playerInfo = new Network.PlayerInfo();
                playerInfo.player = player;
                //Send a client his own information
                c.sendTCP(playerInfo);
                return;
            }

            String name = ((Network.Login) object).name;
            Gdx.app.log("Netowrking-Server", "Login attempt from: " + name);

            player = new ClientPlayer();

            player.name = name;
            //player.playerId = getNewPlayerID();

            if (player.playerId == -1) {
                c.sendTCP(new Network.GameFull());
            }

            Network.PlayerInfo playerInfo = new Network.PlayerInfo();
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

    private boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        value = value.trim();

        if (value.length() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void disconnected(Connection c) {
    	GameServer.PlayerConnection connection = (GameServer.PlayerConnection) c;
        if (connection.player != null) {

            Gdx.app.log("Netowrking-Server", "Got a Disconnect from: (name:" + connection.player.name + ", id:" + connection.player.playerId + ")");
            removePlayerFromList(connection.player);
        }
    }


    private int getNewPlayerID() {
        for (int i = 0; i < gameServer.idPool.length ; i++) {
            //IF false, it is a free id
            if (!gameServer.idPool[i]) {
            	gameServer.idPool[i] = true;
                return i;
            }
        }

        //If we are here there are no Ids left.
        return -1;
    }

    private void addPlayerToList(ClientPlayer p) {
        Gdx.app.log("Netowrking-Server", "Adding to the player list(name:" + p.name + ", id:" + p.playerId + ")");
        this.players.add(p);

        sendPlayerListToAll();

        if (DEBUG_LOGGING) {
            logListOfPlayers("add");
        }
    }

    private void sendPlayerListToAll() {
        Network.ListOfPlayers list = new Network.ListOfPlayers();
        list.players = this.players;
        server.sendToAllTCP(list);
    }

    private void removePlayerFromList(ClientPlayer p) {

        Gdx.app.log("Netowrking-Server", "Removing player from list(name:" + p.name + ", id:" + p.playerId + ")");
        this.players.remove(p);

        sendPlayerListToAll();

        if (DEBUG_LOGGING) {
            logListOfPlayers("remove");
         }
    }

    private void logListOfPlayers(String action)
    {
        Gdx.app.log("Netowrking-Server-DEBUG", "After " + action + ", list contains:");
        for(ClientPlayer pl: players)
        {
            Gdx.app.log("Netowrking-Server-DEBUG", "(name:"+ pl.name + ", id:" + pl.playerId + ")");
        }
    }
}
