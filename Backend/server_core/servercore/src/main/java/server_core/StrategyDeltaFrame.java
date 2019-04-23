package server_core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import game.Game;

public class StrategyDeltaFrame implements Strategy {
	private DeltaFrameInfo info;
	
	public StrategyDeltaFrame(DeltaFrameInfo info) {
		this.info = info;
	}
	
	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		socket.writeString(
				builder.create()
				.toJson(info)
		);
	}

}
