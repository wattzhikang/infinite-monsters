package server_core;

import game.Game;
import game.Watcher;

public class StrategySubscription implements Strategy {

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		Watcher subscription;
		game.getSubscription(client.getKey());
	}

}
