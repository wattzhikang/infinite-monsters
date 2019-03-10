package server_core;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class SocketMessage {
	
	public static final String REQUEST_REGISTRATION = "registration";
	public static final String REQUEST_AUTHENTICATION = "authentication";
	public static final String REQUEST_SUBSCRIPTION = "subscription";
	public static final String REQUEST_MOD_MOVE_SUBSCRIPTION = "mod_move_subscription";
	
	private Strategy strategy;
	
	public SocketMessage(String JSONMessage) {
		String requestType =
				(new JsonParser())
				.parse(JSONMessage)
				.getAsJsonObject()
				.get("requestType")
				.getAsString()
		;
		
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
		
	}
	
	public SocketMessage() {
		strategy = new StrategyPoison();
	}

	public Strategy getStrategy() {
		return strategy;
	}
}
