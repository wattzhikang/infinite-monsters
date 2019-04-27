package game;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Holds information about a tile and all subscriptions
 * with an active interest in it
 * @author zjwatt
 *
 */
public class Plot {
	private SubscriptionLock stake;
	private Tile plot;
	private Collection<Subscription> stakeholders;
	
	/**
	 * Returns a Plot with a given tile and a blank list of
	 * subscribers
	 * @param plot
	 */
	public Plot(Tile plot) {
		this.stake = new SubscriptionLock();
		this.plot = plot;
		this.stakeholders = new LinkedList<Subscription>();
	}
	
	/**
	 * Gets the SubscriptionLock at the tile
	 * @return
	 * @deprecated
	 */
	public SubscriptionLock getStake() {
		return stake;
	}
	
	/**
	 * Sets the SubscriptionLock at this plot
	 * @param stake
	 * @deprecated
	 */
	public void setStake(SubscriptionLock stake) {
		this.stake = stake;
	}
	
	/**
	 * Gets the tile at this plot
	 * @return
	 */
	public Tile getPlot() {
		return plot;
	}
	
	/**
	 * Updates the tile at this plot
	 * @param plot
	 */
	public void setPlot(Tile plot) {
		this.plot = plot;
	}
	
	/**
	 * Adds a Subscription with an active interest in this plot
	 * @param subscription
	 */
	public void addSubscriber(Subscription subscription) {
		stakeholders.add(subscription);
	}
	
	/**
	 * Removes a Subscription that no longer has an interest
	 * in this plot
	 * @param subscription
	 */
	public void removeSubscriber(Subscription subscription) {
		stakeholders.remove(subscription);
	}
	
	/**
	 * Returns a collection with all Subscriptions with an
	 * active interest in this plot
	 * @return
	 */
	public Collection<Subscription> getSubscribers() {
		return stakeholders;
	}
}
