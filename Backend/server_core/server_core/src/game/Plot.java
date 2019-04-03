package game;

import java.util.Collection;
import java.util.LinkedList;

public class Plot {
	private SubscriptionLock stake;
	private Tile plot;
	private Collection<Subscription> stakeholders;
	
	public Plot(Tile plot) {
		this.stake = new SubscriptionLock();
		this.plot = plot;
		this.stakeholders = new LinkedList<Subscription>();
	}
	public SubscriptionLock getStake() {
		return stake;
	}
	public void setStake(SubscriptionLock stake) {
		this.stake = stake;
	}
	public Tile getPlot() {
		return plot;
	}
	public void setPlot(Tile plot) {
		this.plot = plot;
	}
	
	public void addSubscriber(Subscription subscription) {
		stakeholders.add(subscription);
	}
	public void removeSubscriber(Subscription subscription) {
		stakeholders.remove(subscription);
	}
	public Collection<Subscription> getSubscribers() {
		return stakeholders;
	}
}
