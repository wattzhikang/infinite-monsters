package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import server_core.Client;

/**
 * This serves as the wallet for Clients. It is the means by which a Client
 * verifies its privilege level, and it also manages its subscriptions.
 * @author Zachariah Watt
 *
 */
public class ClientKey {
	/**
	 * The possible privilege levels for the program
	 */
	public enum Privileges {SPECTATOR, PLAYER, DESIGNER};
	
	/**
	 * The client's username
	 */
	private String user;
	
	/**
	 * A reference to this Key's client
	 */
	private Client userLink;
	
	/**
	 * The privilege level of this specific client
	 */
	private Privileges priveleges;
	
	/**
	 * The subscriptions held by the Client
	 */
	private Map<Integer, Subscription> subscribers;
	
	/**
	 * This is the next ID that will be assigned to the next
	 */
	private int IDs = -1;
	
	public ClientKey(String username, Client client, Privileges privileges) {
		this.user = username;
		userLink = client;
		this.priveleges = privileges;
		
		subscribers = new HashMap<Integer, Subscription>();
	}

	public Privileges getPriveleges() {
		return priveleges;
	}
	
	public String getUser() {
		return user;
	}
	
	public Client getUserLink() {
		return userLink;
	}
	
	public int getNextID() {
		return IDs;
	}
	
	public int addSubscriber(Subscription subscription) {
		subscribers.put(new Integer(IDs), subscription);
		subscription.setID(IDs);
		return IDs++;
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
