package server_core;

import com.google.gson.Gson;

import game.Coordinates;
import game.Game;
import game.RectangleBoundary;

public class StrategyModificationMS implements Strategy {
	private StrategyModificationMSInfo info;
	
	public StrategyModificationMS(StrategyModificationMSInfo info) {
		this.info = info;
	}
	
	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		RectangleBoundary bounds = new RectangleBoundary(
				new Coordinates(info.getxL(), info.getyL()),
				new Coordinates(info.getxR(), info.getyU())
		);
		Coordinates nPlayerLocation = new Coordinates(
				info.getNewPlayerX(),
				info.getNewPlayerY()
		);
		
		StrategyModificationMSSuccess success = null;
		
//		if (game.moveSubscription(client.getKey(), client.getSubscription(info.getSubscriptionID()), bounds, new Coordinates(info.getOldPlayerX(), info.getOldPlayerY()), new Coordinates(info.getNewPlayerX(), info.getNewPlayerY())) == true) {
//			success = new StrategyModificationMSSuccess(true);
//		} else {
//			success = new StrategyModificationMSSuccess(false);
//		}
		
		client.getKey().getSubscriber(info.getSubscriptionID()).move(bounds, nPlayerLocation);
		
		socket.writeString(
				(new Gson())
				.toJson(success)
		);
	}
}
