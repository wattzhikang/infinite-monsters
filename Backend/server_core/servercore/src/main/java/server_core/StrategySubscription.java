package server_core;

import game.Game;

/**
 * Attempts to get a Subscripton for a client
 * @author Zachariah Watt
 *
 */
public class StrategySubscription implements Strategy {

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		game.getSubscription(client.getKey());
	}

}
