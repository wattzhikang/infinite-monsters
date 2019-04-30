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
		if (client.getKey() == null) {
			System.out.println("This client does not have an identity");
			return;
		}
		game.getSubscription(client.getKey());
	}

}
