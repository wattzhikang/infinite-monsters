package server_core;

import game.Game;
import server_core.Client;
import server_core.SocketAdapter;
import server_core.Strategy;

public class StrategyUnsubscription implements Strategy {
	StrategyUnsubscriptionInfo info = null;
	
	public StrategyUnsubscription(StrategyUnsubscriptionInfo info) {
		this.info = info;
	}

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		client.getKey().removeSubscriber(info.subscriptionID);
	}

}
