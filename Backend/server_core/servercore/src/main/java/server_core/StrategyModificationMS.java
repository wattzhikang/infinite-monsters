package server_core;

import com.google.gson.Gson;

import game.Position;
import game.Game;
import game.RectangleBoundary;

/**
 * Attempts to move a client's subscription
 * @author Zachariah Watt
 *
 */
public class StrategyModificationMS implements Strategy {
	private StrategyModificationMSInfo info;
	
	public StrategyModificationMS(StrategyModificationMSInfo info) {
		this.info = info;
	}
	
	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		if (client.getKey() == null) {
			System.out.println("This client does not have an identity");
			return;
		}

		long dungeon = client.getKey().getSubscriber(info.getSubscriptionID()).dungeon();
		
		RectangleBoundary bounds = new RectangleBoundary(
				new Position(info.getxL(), info.getyL(), dungeon),
				new Position(info.getxR(), info.getyU(), dungeon)
		);
		Position nPlayerLocation = new Position(
				info.getNewPlayerX(),
				info.getNewPlayerY(),
				dungeon
		);
		
		
		StrategyModificationMSSuccess success = new StrategyModificationMSSuccess(true);
		
		client.getKey().getSubscriber(info.getSubscriptionID()).move(bounds, nPlayerLocation);
		
		socket.writeString(
				(new Gson())
				.toJson(success)
		);
	}
}
