package server_core;

import game.Game;

public interface Strategy {
	
	public void takeAction(Game game, SocketAdapter socket, Client client);
	
}
