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
	
	/**
	 * Constructs a new ClientKey
	 * @param username The username of the Client
	 * @param client The Client object itself
	 * @param privileges The privilege level of the Client
	 */
	public ClientKey(String username, Client client, Privileges privileges) {
		this.user = username;
		userLink = client;
		this.priveleges = privileges;
		
		subscribers = new HashMap<Integer, Subscription>();
	}

	/**
	 * Returns the privilege level of the Client
	 * @return
	 */
	public Privileges getPriveleges() {
		return priveleges;
	}
	
	/**
	 * Returns the username of the Client
	 * @return
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Returns the Client object of the client
	 * @return
	 */
	public Client getUserLink() {
		return userLink;
	}
	
	/**
	 * Adds a subscription, automatically assigning it a subscription ID.
	 * @param subscription
	 * @return The ID assigned to the added subscription
	 */
	int addSubscriber(Subscription subscription) {
		IDs++;
		subscribers.put(new Integer(IDs), subscription);
		subscription.setID(IDs);
		return IDs;
	}
	
	/**
	 * Returns the subscription identified by the ID
	 * @param ID
	 * @return
	 */
	public Subscription getSubscriber(int ID) {
		return subscribers.get(ID);
	}
	
	/**
	 * Unsubscribes a specific subscription
	 * @param ID
	 */
	public void removeSubscriber(int ID) {
		subscribers.get(ID).unsubscribe();
		subscribers.remove(ID);
	}
	
	/**
	 * This unsubscribes every single subscription from the game
	 */
	public void unsubscribeAll() {
		for (Entry<Integer, Subscription> subscriber : subscribers.entrySet()) {
			subscriber.getValue().unsubscribe();
			subscribers.remove(subscriber.getKey());
		}
	}
}
