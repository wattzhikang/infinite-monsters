package server_core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import game.Tile;

public class SocketMessage {
	
	public static final String REQUEST_REGISTRATION = "registration";
	public static final String REQUEST_AUTHENTICATION = "authentication";
	public static final String REQUEST_SUBSCRIPTION = "subscription";
	public static final String REQUEST_MOD_MOVE_SUBSCRIPTION = "mod_move_subscription";
	
	private Strategy strategy;
	
	public SocketMessage(String JSONMessage) throws Exception {
		String requestType = null;
		try {
			requestType =
					(new JsonParser())
					.parse(JSONMessage)
					.getAsJsonObject()
					.get("requestType")
					.getAsString()
			;
		} catch (Exception e) {
			throw e;
		}
		
		switch (requestType) {
		case REQUEST_REGISTRATION:
			strategy = new StrategyRegistration(
					(new Gson())
					.fromJson(JSONMessage, StrategyRegistrationInfo.class)
			);
			break;
		case REQUEST_AUTHENTICATION:
			strategy = new StrategyAuthentication(
					(new Gson())
					.fromJson(JSONMessage, StrategyAuthenticationInfo.class)
			);
			break;
		case REQUEST_SUBSCRIPTION:
			strategy = new StrategySubscription();
			break;
		case REQUEST_MOD_MOVE_SUBSCRIPTION:
			strategy = new StrategyModificationMS(
					(new Gson())
					.fromJson(JSONMessage, StrategyModificationMSInfo.class)
			);
			break;
		}
	}
	
	public SocketMessage(DeltaFrame message) {
		Collection<Tile> tiles = message.getTiles();
		List<TileInfo> tileInfos = new ArrayList<TileInfo>();
		for (Tile tile : tiles) {
			tileInfos.add(
					new TileInfo(
							tile.getLocation().getX(),
							tile.getLocation().getY(),
							tile.isWalkable(),
							tile.getTerrain(),
							tile.getObject(),
							tile.getCharacter()
					)
			);
		}
		
		strategy = new StrategyDeltaFrame(
				new DeltaFrameInfo(
					message.getSubscriptionID(),
					message.isDungeonChange(),
					message.getBounds().getLowerLeft().getX(),
					message.getBounds().getUpperRight().getX(),
					message.getBounds().getUpperRight().getY(),
					message.getBounds().getLowerLeft().getY(),
					(Collection<TileInfo>)tileInfos
				)
		);
	}
	
	public SocketMessage() {
		strategy = new StrategySeppuku();
	}

	public Strategy getStrategy() {
		return strategy;
	}
}
