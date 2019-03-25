package game;

public class Subscription {
	SubscriptionLock lock;
	
	SubscriptionLock getLock() {
		return lock;
	}
	
	void updateLock(SubscriptionLock lock) {
		this.lock = lock;
	}
}
