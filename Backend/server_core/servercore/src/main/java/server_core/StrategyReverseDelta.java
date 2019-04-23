package server_core;

import game.Game;

public class StrategyReverseDelta implements Strategy {
	private StrategyReverseDeltaInfo info;
	
	public StrategyReverseDelta(StrategyReverseDeltaInfo info) {
		this.info = info;
	}

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		// TODO Auto-generated method stub
	}

}
