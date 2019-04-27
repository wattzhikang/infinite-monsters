package server_core;

import game.Game;

/**
 * Class that represents what actions should be taken based on what message is in the Client's
 * message queue
 * @author zjwatt
 *
 */
public interface Strategy {
	
	/**
	 * Takes appropriate action. See implementing classes for examples
	 * @param game
	 * @param socket
	 * @param client
	 */
	public void takeAction(Game game, SocketAdapter socket, Client client);
	
}
