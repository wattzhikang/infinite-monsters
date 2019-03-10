package game;

import java.util.Collection;

import server_core.*;

public class Watcher {
	private Client client;
	private int id;
	private RectangleBoundary bounds;
	
	public Watcher(Client client, int id) {
		this.client = client;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	void sendDelta(Collection<Tile> tiles) {
		client.enqueueDeltaFrame(new DeltaFrame(bounds, tiles, false));
	}
	
	void unsubscribe() {
		
	}
	
	public void setBounds(RectangleBoundary bounds) {this.bounds = bounds;}
	public RectangleBoundary getBounds() {return bounds;}

}
