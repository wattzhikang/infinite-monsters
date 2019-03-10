package server_core;

import com.google.gson.Gson;

import game.Game;

public class StrategyRegistration implements Strategy {
	
	private StrategyRegistrationInfo info;

	public StrategyRegistration(StrategyRegistrationInfo info) {
		this.info = info;
	}

	@Override
	public void takeAction(Game game, SocketAdapter adapter, Client client) {
		StrategyRegistrationSuccess success = null;
		if (game.register(info.getUsername(), info.getPassword()) == true) {
			success = new StrategyRegistrationSuccess(true);
		} else {
			success = new StrategyRegistrationSuccess(false);
		}
		
		adapter.writeString(
				(new Gson())
				.toJson(success)
		);
	}

}
