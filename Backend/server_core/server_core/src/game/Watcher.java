package game;

import java.util.Collection;

import server_core.*;

public class Watcher {
	private Client client;
	private RectangleBoundary bounds;
	
	public Watcher(Client client) {
		this.client = client;
	}
	
	void sendDelta(Collection<Tile> tiles) {
		client.enqueueDeltaFrame(new DeltaFrame(bounds, tiles, false));
	}
	
	void unsubscribe() {
		
	}
	
	public void setBounds(RectangleBoundary bounds) {this.bounds = bounds;}
	public RectangleBoundary getBounds() {return bounds;}

}
