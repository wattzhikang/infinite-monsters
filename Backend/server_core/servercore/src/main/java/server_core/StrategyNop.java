package server_core;
import game.Game;

/**
 * Takes no action
 * @author Zachariah Watt
 *
 */
public class StrategyNop implements Strategy {

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		return; //This is a Nop
	}

}
