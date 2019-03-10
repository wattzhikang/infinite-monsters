package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server_core.Client;

class ClientKeyTest {
	
	ClientKey key;
	
	private static final String USERNAME = "zjwatt";

	@BeforeEach
	void setUp() throws Exception {
		key = new ClientKey(USERNAME, null, ClientKey.Privileges.PLAYER);
	}

	@Test
	void privilegesWork() {
		assert key.getPriveleges() == ClientKey.Privileges.PLAYER;
	}
	
	@Test
	void usernameWorks() {
		assert key.getUser().equals(USERNAME);
	}
	
	@Test
	void managesSubscriptions() {
		Watcher subscription = new Watcher(null);
		
		assert subscription != null;
		
		key.addSubscription(subscription);
		
		System.out.println(key.getSubscriptions().size());
		
		assert key.getSubscriptions().size() == 1;
	}

}
