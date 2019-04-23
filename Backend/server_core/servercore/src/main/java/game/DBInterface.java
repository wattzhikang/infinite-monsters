package game;

import java.util.Collection;

public interface DBInterface {
	public boolean login(String username, String password);
	public boolean register(String username, String password);
	
	public RectangleBoundary lastSubscriptionBounds(ClientKey key);
	public void updateSubscriptionBounds(ClientKey key, RectangleBoundary bounds);
	
	public Collection<Tile> getTiles(Collection<Position> locations);
	public void updateTiles(Collection<Tile> tiles);
	
	public void close();
}
