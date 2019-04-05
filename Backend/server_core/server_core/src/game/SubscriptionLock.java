package game;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

class SubscriptionLock extends ReentrantLock {
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
	
	boolean removeSubscriber(Subscription sub) {
		return subscribers.remove(sub);
	}
	
	/**
	 * Checks to see if the given subscription overlaps with any other
	 * subscription at the given position
	 * @param subscription
	 * @param position
	 * @return
	 */
	boolean hasOverlap(Subscription subscription, Position position) {
		for (Subscription otherSub : subscribers) {
			if (otherSub != subscription && otherSub.getBounds().isAt(position)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the given subscription overlaps with *any* other subscription
	 * within the bounds of this lock
	 * @param subscription
	 * @return
	 */
	boolean hasOverlap(Subscription subscription) {
		Collection<Position> positions = subscription.getBounds().getBetween();
		for (Position position : positions) {
			if (hasOverlap(subscription, position)) {
				return true;
			}
		}
		return false;
	}
}
