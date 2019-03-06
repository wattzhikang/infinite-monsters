package json_parser;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import server_core.SocketMessage;

public class MessageAdapter implements ParserInterface {

	public JsonElement user = null;
	public JsonElement pass = null;
	public JsonElement request = null;
	public JsonElement privileges = null;
	public JsonParser parser;
	public Gson gson;
	SocketMessage message;
	private ArrayList<JsonElement> credentials;

	public MessageAdapter(SocketMessage message) {
		
		this.message=message;
		parser = new JsonParser();
		String json = message.getMessage();
		JsonElement jsonTree = parser.parse(json);
		
		
		if (jsonTree.isJsonObject()) {
			JsonObject request = jsonTree.getAsJsonObject();

			this.user = request.get("username");
			this.pass = request.get("password");
			this.request = request.get("requestType");
			this.privileges = request.get("privileges");
		// Registration reg = gson.fromJson(jsonTree, Registration.class);

		}
	
	}
	
	public void run() {
		String json = message.getMessage();
		JsonElement jsonTree = parser.parse(json);
		gson = new Gson();
		switch (message.getOrigin()) {
		case CLIENT:
			if (jsonTree.isJsonObject()) {

//				JsonObject request = jsonTree.getAsJsonObject();

				/*
				 * Subscription : Authentication : Registration
				 */
				if (json.length() == 4) {
					credentials = addCredentials(1);
					String response = gson.toJson(credentials, Authentication.class);
					respond(response);

				} else if (json.length() == 13) {

					credentials = addCredentials(3);
					String response = gson.toJson(credentials, Authentication.class);
					respond(response);

				} else if (json.length() == 17) {

					credentials = addCredentials(4);
					String response = gson.toJson(credentials, Registration.class);
					respond(response);
				}

				/*
				 * the request isn't valid
				 */
				else {
					String s = "Improper request or json string format";
					respond(s);
				}

			} else {
				respond("null");
			}
		case SERVER:
			if (jsonTree.isJsonObject()) {
				JsonObject response = jsonTree.getAsJsonObject();

			}
		case POISON:
			break;
		default:
			break;

		}
	}

	public String respond(String response) {
		return response;

	}

	@Override
	public JsonElement getRequestType(String JSON) {
		// TODO Auto-generated method stub
		// JsonParser parser = new JsonParser();

		return this.request;
	}

	@Override
	public JsonElement getUsername(String JSON) {
		// TODO Auto-generated method stub
		return this.user;
	}

	@Override
	public JsonElement getPassword(String JSON) {
		// TODO Auto-generated method stub
		return this.pass;
	}

	@Override
	public ArrayList<JsonElement> addCredentials(int num) {
		if (num == 1) {
			credentials.add(this.request);
			return credentials;
		} else if (num == 3) {
			credentials.add(this.request);
			credentials.add(this.user);
			credentials.add(this.pass);
			return credentials;
		} else if (num == 4) {
			credentials.add(this.request);
			credentials.add(this.user);
			credentials.add(this.pass);
			credentials.add(this.privileges);
			return credentials;
		} else
			return null;
	}

}
