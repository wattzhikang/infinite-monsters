package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
		//TODO reimplement
//		Watcher subscription = new Watcher(null, 0);
//		
//		assert subscription != null;
//		
//		key.addSubscription(subscription);
//		
//		System.out.println(key.getSubscriptions().size());
//		
//		assert key.getSubscriptions().size() == 1;
	}

}
