package json_parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import server_core.SocketMessage;

public class MessageAdapterTest {
	public enum MessageOrigin {
		CLIENT, SERVER, POISON
	}

	private MessageAdapter adapt;

//	private static final server_core.SocketMessage.MessageOrigin CLIENT = null;;

	/*
	 * Build Test Class before making json work. Understand how the Client will ask
	 * for something and how it will output the resultant message
	 */

	// {"requestType":"registration","username":"user1","password":"sunshine","privileges":"player"}
	@Test
	public void test() {
//		server_core.SocketMessage.MessageOrigin origin = CLIENT;
		
		String JSON = "{\"requestType\":\"registration\",\"username\":\"user1\",\"password\":\"sunshine\",\"privileges\":\"player\"}";
		SocketMessage message = new SocketMessage(server_core.SocketMessage.MessageOrigin.CLIENT, JSON);
		JsonParser parser = new JsonParser();
		System.out.println(parser.parse(JSON).toString());
//		message.JSONMessage=JSON;
		
		System.out.println("1");
//		JSON = JSON.trim();
//		Gson gson = new Gson();
		System.out.println("2");

		adapt = new MessageAdapter(message);
		adapt.run();
		System.out.println(adapt);
		
		System.out.println("3");

	}
}
