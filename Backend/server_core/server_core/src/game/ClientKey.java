package game;

import java.util.Collection;
import java.util.LinkedList;

import server_core.Client;

public class ClientKey {
	public enum Privileges {SPECTATOR, PLAYER, DESIGNER};
	
	private String user;
	private Client userLink;
	private Privileges priveleges;
	private Collection<Watcher> subscriptions;
	
	public ClientKey(String username, Client client, Privileges privileges) {
		this.user = username;
		userLink = client;
		this.priveleges = privileges;
		
		subscriptions = new LinkedList<Watcher>();
	}

	public Privileges getPriveleges() {
		return priveleges;
	}
	
	public String getUser() {
		return user;
	}
	
	public Client getUserLink() {return userLink;}
	
	void addSubscription(Watcher subscription) {
		subscriptions.add(subscription);
	}
	
	Collection<Watcher> getSubscriptions() {
		return subscriptions;
	}
	
	public int getNumSubscriptions() {
		return subscriptions.size();
	}
}
