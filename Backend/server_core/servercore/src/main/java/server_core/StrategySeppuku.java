package server_core;

import game.Game;

/**
 * Deauthenticates a client
 * @author Zachariah Watt
 *
 */
public class StrategySeppuku implements Strategy {

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		//this should not attempt to close the socket, simply deauthenticate
		client.getKey().unsubscribeAll();
		client.setKey(null);
	}

}
