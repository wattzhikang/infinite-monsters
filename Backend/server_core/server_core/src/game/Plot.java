package game;

public class Plot {
	SubscriptionLock stake;
	Tile plot;
	
	public Plot(SubscriptionLock stake, Tile plot) {
		this.stake = stake;
		this.plot = plot;
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
}
