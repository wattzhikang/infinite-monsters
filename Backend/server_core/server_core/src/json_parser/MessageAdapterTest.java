package json_parser;

import static org.junit.Assert.assertEquals;
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

		
		adapt = new MessageAdapter(message);
//		String response = adapt.run();
		
		assertEquals(
				"{\"request\":\"registration\",\"username\":\"user1\",\"pass\":\"sunshine\",\"privileges\":\"player\"}",
				adapt.run());

	}
}
