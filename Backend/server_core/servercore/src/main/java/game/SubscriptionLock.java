package game;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides a mutex-like locking mechanism for Subscriptions
 * to ensure mutual exclusion
 * @author Zachariah Watt
 *
 */
class SubscriptionLock extends ReentrantLock {
	private Collection<Subscription> subscribers;
	
	SubscriptionLock() {
		subscribers = new LinkedList<Subscription>();
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	Collection<Subscription> getSubscribers() {
		return subscribers;
	}
	
	/**
	 * @deprecated
	 * @param sub
	 */
	void addSubscriber(Subscription sub) {
		subscribers.add(sub);
	}
	
	/**
	 * @deprecated
	 * @param subs
	 */
	void addSubscribers(Collection<Subscription> subs) {
		subscribers.addAll(subs);
	}
	
	/**
	 * @deprecated
	 * @param sub
	 * @return
	 */
	boolean removeSubscriber(Subscription sub) {
		return subscribers.remove(sub);
	}
	
	/**
	 * Checks to see if the given subscription overlaps with any other
	 * subscription at the given position
	 * @param subscription
	 * @param position
	 * @return
	 * @deprecated
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
	 * @deprecated
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
