package server_core;

import game.Game;

public class StrategySubscription implements Strategy {

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		game.getSubscription(client.getKey());
	}

}
