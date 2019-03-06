package json_parser;

import java.util.ArrayList;

import com.google.gson.JsonElement;

import server_core.SocketMessage;

//import server_core.Client.RequestType;

public interface ParserInterface {
	
	
//	enum RequestType { REGISTRATION, AUTHENTICATION, SUBSCRIPTION, MODIFICATION, LOGOUT, MALFORMED };
	
	//{"requestType":"registration","username":"user1","password":"sunshine","privileges":"player"}
		//{ requestType : registration , username : user1 , password : sunshine , privileges : player }
		//0 1           2 3            4 5        6 7     8 9       10 11      12 13        14 15     16
		
		//{"requestType":"authentication","username":"user1","password":"sunshine"}
		//{ requestType : authentication , username : user1 , password : sunshine }
		//0 1           2 3              4 5        6 7     8 9       10 11       12
		
		/*
		 * The following methods are a hacked-together means of reading the client's
		 * JSON strings until such time as Gson is integrated into the project
		 */
		public JsonElement getRequestType(String JSON);
		public JsonElement getUsername(String JSON);
		public JsonElement getPassword(String JSON);
		public ArrayList<JsonElement> addCredentials(int num);
//		public Registration register(SocketMessage message);
//		public Authentication login(SocketMessage message);
		//public String request(SocketMessage message);
	
}
