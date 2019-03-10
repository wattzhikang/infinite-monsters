package server_core;

import game.Game;

public class StrategyPoison implements Strategy {

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		try {
			client.shutDown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
