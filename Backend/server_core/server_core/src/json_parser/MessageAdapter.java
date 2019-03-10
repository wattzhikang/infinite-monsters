package json_parser;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
	public GsonBuilder build = new GsonBuilder();

	SocketMessage message;
//	private ArrayList<JsonElement> credentials = new ArrayList<JsonElement>();

	public MessageAdapter(SocketMessage message) {

		this.message = message;
		parser = new JsonParser();
		String json = message.getMessage();

		/*
		 * Its breaking right here at line 33 presumably because of incorrect format.
		 * Still trying to figure this out
		 */
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

	public String run() {
		String json = message.getMessage();
		JsonElement jsonTree = parser.parse(json);
		gson = new Gson();
		
		switch (message.getOrigin()) {
		case CLIENT:
			if (jsonTree.isJsonObject()) {

				// JsonObject request = jsonTree.getAsJsonObject();

				/*
				 * Subscription : Authentication : Registration
				 */
				if (json.length() == 4) {
					Subscription subscription = new Subscription();
					subscription.request = this.request;
					String response = gson.toJson(subscription);
					return respond(response);

				} else if (json.length() == 13) {

					Registration login = new Registration();
					login.pass = this.pass;
					login.username = this.user;
					login.request = this.request;
					String response = gson.toJson(login);
					return respond(response);

				} else if (json.length() == 93) {

					Registration register = new Registration();
					register.pass = this.pass;
					register.username = this.user;
					register.request = this.request;
					register.privileges = this.privileges;
					String response = gson.toJson(register);
					return respond(response);
				}

				/*
				 * the request isn't valid
				 */
				else {
					String s = "Improper request or json string format";
					return respond(s);
				}

			} else {
				return respond("null");
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
		return json;
	}

	public String respond(String response) {
		System.out.println(response);
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

//	@Override
//	public JsonElement addCredentials(int num) {
//		if (num == 1) {
//			Subscription sub = new Subscription();
//			sub.request = this.request;
//			credentials.add(this.request);
//			return credentials;
//		} else if (num == 3) {
//			
//			credentials.add(this.request);
//			credentials.add(this.user);
//			credentials.add(this.pass);
//			return credentials;
//		} else if (num == 4) {
//			Registration login = new Registration();
//			login.pass = this.pass;
//			login.username = this.user;
//			login.request = this.request;
//			gson = new Gson();
//			
//			System.out.println(this.request.toString());
//			System.out.println(this.pass.toString());
//			System.out.println(this.user.toString());
//			System.out.println(this.privileges.toString());
//			System.out.println(gson.toJson(login));
//			
//			credentials.add(this.request);
//			credentials.add(this.user);
//			credentials.add(this.pass);
//			credentials.add(this.privileges);
//			return credentials;
//		} else
//			return null;
//	}

}
