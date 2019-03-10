package server_core;

import game.Game;
import game.Watcher;

public class StrategySubscription implements Strategy {

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		Watcher subscription;
		if ((subscription = game.getSubscription(client.getKey())) != null) {
			client.addSubscription(subscription);
		}
	}

}
