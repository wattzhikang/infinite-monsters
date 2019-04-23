package server_core;

import com.google.gson.Gson;

import game.ClientKey;
import game.Game;

public class StrategyAuthentication implements Strategy {
	
	StrategyAuthenticationInfo info;
	
	
	
	public StrategyAuthentication(StrategyAuthenticationInfo info) {
		super();
		this.info = info;
	}



	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		ClientKey key;
		StrategyAuthenticationSuccess success = null;
		if ((key = game.authenticate(info.getUsername(), info.getPassword(), client)) != null) {
			client.setKey(key);
			success = new StrategyAuthenticationSuccess(true);
		} else {
			success = new StrategyAuthenticationSuccess(false);
		}
		
		socket.writeString(
				(new Gson())
				.toJson(success)
		);
	}

}
