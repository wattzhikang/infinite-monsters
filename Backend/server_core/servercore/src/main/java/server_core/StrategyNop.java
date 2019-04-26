package server_core;
import game.Game;

public class StrategyNop implements Strategy {

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		return; //This is a Nop
	}

}
