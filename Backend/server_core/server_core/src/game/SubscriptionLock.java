package game;

import java.util.Collection;
import java.util.LinkedList;

class SubscriptionLock {
	private Collection<Subscription> subscribers;
	
	SubscriptionLock() {
		subscribers = new LinkedList<Subscription>();
	}
	
	Collection<Subscription> getSubscribers() {
		return subscribers;
	}
	
	void addSubscriber(Subscription sub) {
		subscribers.add(sub);
	}
	
	void addSubscribers(Collection<Subscription> subs) {
		subscribers.addAll(subs);
	}
}
