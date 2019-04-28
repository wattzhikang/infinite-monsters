package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import com.mysql.jdbc.Driver;

/**
 * Connects to a MySQL or MariaDB database
 * @author Zachariah Watt
 *
 */
public class DBAdapter implements DBInterface {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/intermondb?useLegacyDatetimeCode=false&serverTimezone=America/Chicago";
	private static final String USER = "intermon";
	private static final String PASSWORD = "6$r6F~q9gWj$_pj";
	
	private static final String USER_TABLE = "intermondb.users";
	private static final String USER_USERNAME = "username";
	private static final String USER_PASSWORD = "password";
	private static final String USER_PLAYERX = "playerX";
	private static final String USER_PLAYERY = "playerY";
	private static final String USER_XLOWERLEFT = "xLowerLeft";
	private static final String USER_YLOWERLEFT = "yLowerLeft";
	private static final String USER_XUPPERRIGHT = "xUpperRight";
	private static final String USER_YUPPERRIGHT = "yUpperRight";
	private static final String USER_DUNGEON = "dungeon";

	private static final String UNIVERSE_TABLE = "intermondb.universe";
	private static final String UNIVERSE_DUNGEON = "dungeon";
	private static final String UNIVERSE_X = "x";
	private static final String UNIVERSE_Y = "y";
	private static final String UNIVERSE_TERRAIN = "backgroundTerrain";
	private static final String UNIVERSE_OBJECT = "foregroundObject";
	private static final String UNIVERSE_PLAYER = "player";
	private static final String UNIVERSE_WALKABLE = "walkable";

	Connection connection = null;
	
	public DBAdapter() {
		Statement statement = null;
		ResultSet results = null;
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("Driver loaded");

			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			System.out.println("Connection established");
			
			System.out.println("Creating statement");
			statement = connection.createStatement();
			System.out.println("Statement Created");
			
			
			String sql = "SELECT " + USER_USERNAME + " FROM intermondb.user";
			System.out.println("Executing Query...");
			results = statement.executeQuery(sql);
			System.out.println("Query Successfully Executed");
			
			System.out.println("Printing Usernames");
			while (results.next()) {
				System.out.println(results.getString("UserName"));
			}
			
		} catch (SQLException se) {
			// TODO this needs to stop the server from starting
			se.printStackTrace();
		} catch (Exception e) {
			// TODO this needs to stop the server from starting
			e.printStackTrace();
			System.exit(1);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (results != null) {
				try {
					results.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public synchronized boolean login(String username, String password) {
		Statement statement = null;
		ResultSet results = null;
		try {
			statement = connection.createStatement();

			String sql = 
				"SELECT " +
				USER_USERNAME + ", " + USER_PASSWORD +
				" FROM " +
				USER_TABLE + 
				" WHERE UserName='" + username +
				"' AND Password='" + password + "';"
			;

			results = statement.executeQuery(sql);
			
			if (results.next()) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (results != null) {
				try {
					results.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	public synchronized boolean register(String username, String password) {
		Statement statement = null;
		ResultSet results = null;
		try {
			//first check that user is not already in database
			statement = connection.createStatement();
			
			String sql = 
				"SELECT " + USER_USERNAME +
				" FROM " + USER_TABLE +
				" WHERE " + USER_USERNAME + "='" + username + "';"
			;
			
			results = statement.executeQuery(sql);
			
			if (results.next()) {
				return false;
			}
			
			//if user is not in database, create an entry
			statement.close();
			
			statement = connection.createStatement();

			sql =
				"INSERT INTO " + USER_TABLE +
				"( " + USER_USERNAME + ", " + USER_PASSWORD + ") " +
				" VALUES ('" + username + "', '" + password + "');"
			;
			
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (results != null) {
				try {
					results.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Collection<Tile> getTiles(Collection<Position> locations) {
		// TODO Auto-generated method stub
		Statement statement = null;
		ResultSet results = null;
		return null;
	}

	@Override
	public void updateTiles(Collection<Tile> tiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RectangleBoundary lastSubscriptionBounds(ClientKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSubscriptionBounds(ClientKey key, RectangleBoundary bounds) {
		// TODO Auto-generated method stub
		
	}
}
