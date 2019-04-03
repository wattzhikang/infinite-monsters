package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import server_core.Client;

public class ClientKey {
	public enum Privileges {SPECTATOR, PLAYER, DESIGNER};
	
	private String user;
	private Client userLink;
	private Privileges priveleges;
	private Collection<Watcher> subscriptions;
	
	private Map<Integer, Subscription> subscribers;
	
	private int IDs = 0;
	
	public ClientKey(String username, Client client, Privileges privileges) {
		this.user = username;
		userLink = client;
		this.priveleges = privileges;
		
		subscriptions = new LinkedList<Watcher>();
		
		subscribers = new HashMap<Integer, Subscription>();
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
	
	public int getNextID() {
		return IDs;
	}
	
	public void addSubscriber(Subscription subscription, int ID) {
		subscribers.put(new Integer(ID), subscription);
		IDs++;
	}
	public Subscription getSubscriber(int ID) {
		return subscribers.get(ID);
	}
	public void unsubscribeAll() {
		for (Entry<Integer, Subscription> subscriber : subscribers.entrySet()) {
			subscriber.getValue().unsubscribe();
		}
	}
}
