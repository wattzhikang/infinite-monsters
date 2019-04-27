package game;

import java.util.Collection;

/**
 * Implemented by any class that is able to provide game data
 * @author zjwatt
 *
 */
public interface DBInterface {
	/**
	 * Returns true if a user matching matching this description is in the database
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean login(String username, String password);
	
	/**
	 * Returns true if the class was able to register this user in the databsse
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean register(String username, String password);
	
	/**
	 * Returns a RectangleBoundary describing where this player was last time the database was saved
	 * @param key
	 * @return
	 */
	public RectangleBoundary lastSubscriptionBounds(ClientKey key);
	
	/**
	 * Updates the where the player should be next time lastSubscriptionBounds() is called
	 * @param key
	 * @param bounds
	 */
	public void updateSubscriptionBounds(ClientKey key, RectangleBoundary bounds);
	
	/**
	 * Returns a collection of tiles for the given locations
	 * @param locations
	 * @return
	 */
	public Collection<Tile> getTiles(Collection<Position> locations);
	
	/**
	 * Updates tiles in the database
	 * @param tiles
	 */
	public void updateTiles(Collection<Tile> tiles);
	
	/**
	 * Closes the database properly
	 */
	public void close();
}
