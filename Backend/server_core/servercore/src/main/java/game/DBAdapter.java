package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Connects to a MySQL or MariaDB database
 * @author Zachariah Watt
 *
 */
public class DBAdapter implements DBInterface {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/intermondb" +
		"?useLegacyDatetimeCode=false&serverTimezone=America/Chicago" +
		"&autoReconnect=true"
	;
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
			
			
			String sql = "SELECT " + USER_USERNAME + " FROM " + USER_TABLE;
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
				" WHERE " + USER_USERNAME + "='" + username +
				"' AND " + USER_PASSWORD + "='" + password + "';"
			;

			System.out.println("Executing SQL statement: " + sql);

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
			// TODO this can be done with SQL
			statement = connection.createStatement();
			
			String sql = 
				"SELECT " + USER_USERNAME +
				" FROM " + USER_TABLE +
				" WHERE " + USER_USERNAME + "='" + username + "';"
			;
			
			System.out.println("Executing SQL statement: " + sql);
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
		Collection<Tile> tiles = new LinkedList<Tile>();

		if (locations == null || locations.size() < 1) {
			return tiles;
		}
		
		Statement statement = null;
		ResultSet results = null;
		try {
			statement = connection.createStatement();

			String sql =
				"SELECT * FROM " +
				UNIVERSE_TABLE +
				" WHERE "
			;

			boolean firstClause = true;
			for (Position location : locations) {
				if (!firstClause) {
					sql += " OR ";
				} else {
					firstClause = false;
				}
				sql += " ( ";
				sql += UNIVERSE_DUNGEON + "=";
				sql += location.getDungeon();
				sql += " AND " + UNIVERSE_X + "=";
				sql += location.getX();
				sql += " AND " + UNIVERSE_Y + "=";
				sql += location.getY();
				sql += " ) ";
			}

			sql += ";";

			System.out.println("Executing SQL statement: " + sql);
			results = statement.executeQuery(sql);

			while (results.next()) {
				int dungeon = results.getInt(UNIVERSE_DUNGEON);
				int x = results.getInt(UNIVERSE_X);
				int y = results.getInt(UNIVERSE_Y);
				
				Position position = new Position(
					x,
					y,
					dungeon
				);

				boolean walkable =
					(results.getInt(UNIVERSE_WALKABLE) == 0) ?
					false : true
				;
				String terrain = results.getString(UNIVERSE_TERRAIN);
				String object = results.getString(UNIVERSE_OBJECT);
				String character = results.getString(UNIVERSE_PLAYER);
				if (character.length() < 1) {
					character = null;
				}

				Tile tile = new Tile(
					position,
					walkable,
					terrain,
					object,
					character
				);

				tiles.add(tile);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			tiles = null;
		} catch (Exception e) {

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

		return tiles;
	}

	@Override
	public void updateTiles(Collection<Tile> tiles) {
		Statement statement = null;

		try {
			for (Tile tile : tiles) {
				statement = connection.createStatement();

				int tileIsWalkable = ((tile.isWalkable()) ? 0 : 1);
				String tileTerrain = (tile.getTerrain() == null) ?
					"" : tile.getTerrain()
				;
				String tileObject = (tile.getObject() == null) ?
					"" : tile.getObject()
				;
				String tileCharacter = (tile.getCharacter() == null) ?
					"" : tile.getCharacter()
				;

				String sql = 
					"INSERT INTO " + UNIVERSE_TABLE +
					" ( " +
					UNIVERSE_DUNGEON + ", " +
					UNIVERSE_X + ", " +
					UNIVERSE_Y + ", " +
					UNIVERSE_WALKABLE + ", " +
					UNIVERSE_TERRAIN + ", " +
					UNIVERSE_OBJECT + ", " +
					UNIVERSE_PLAYER +
					" ) VALUES ( " +
					tile.getLocation().getDungeon() + ", " +
					tile.getLocation().getX() + ", " +
					tile.getLocation().getY() + ", " +
					tileIsWalkable + ", " +
					"'" + tileTerrain + "', " +
					"'" + tileObject + "', " +
					"'" + tileCharacter + "'" +
					")" +

					" ON DUPLICATE KEY " +

					"UPDATE " +
					UNIVERSE_WALKABLE + " = " + tileIsWalkable + ", " +
					UNIVERSE_TERRAIN + " = '" + tileTerrain + "', " +
					UNIVERSE_OBJECT + " = '" + tileObject + "', " + 
					UNIVERSE_PLAYER + " = '" + tileCharacter + "' " +

					";"
				;

				System.out.println("Executing SQL statement: " + sql);
				statement.executeUpdate(sql);

				statement.close();
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public RectangleBoundary lastSubscriptionBounds(ClientKey key) {
		Statement statement = null;
		ResultSet results = null;

		RectangleBoundary boundary = null;

		try {
			statement = connection.createStatement();

			String sql = 
				"SELECT * FROM " + USER_TABLE + " WHERE " +
				USER_USERNAME + " = '" + key.getUser() + "';"
			;

			System.out.println("Executing SQL statement: " + sql);
			results = statement.executeQuery(sql);

			results.next();

			int xLowerLeft = results.getInt(USER_XLOWERLEFT);
			int yLowerLeft = results.getInt(USER_YLOWERLEFT);
			int xUpperRight = results.getInt(USER_XUPPERRIGHT);
			int yUpperRight = results.getInt(USER_YUPPERRIGHT);
			long dungeon = results.getLong(USER_DUNGEON);

			Position lowerLeft = new Position(xLowerLeft, yLowerLeft, dungeon);
			Position upperRight = new Position(xUpperRight, yUpperRight, dungeon);

			boundary = new RectangleBoundary(upperRight, lowerLeft);
		} catch (SQLException se) {
			se.printStackTrace();
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
		return boundary;
	}

	@Override
	public void updateSubscriptionBounds(ClientKey key, RectangleBoundary bounds) {
		Statement statement = null;
		ResultSet results = null;

		try{
			statement = connection.createStatement();

			String sql =
				"UPDATE SET " +
				USER_XLOWERLEFT + " = " + bounds.getLowerLeft().getX() + ", " +
				USER_YLOWERLEFT + " = " + bounds.getLowerLeft().getY() + ", " +
				USER_XUPPERRIGHT + " = " + bounds.getUpperRight().getX() + ", " +
				USER_YUPPERRIGHT + " = " + bounds.getUpperRight().getY() + " " +
				" WHERE " +
				USER_USERNAME + " = '" + key.getUser() + "'" +
				";"
			;

			System.out.println("Executing SQL statement: " + sql);
			statement.executeUpdate(sql);
		} catch (SQLException se) {
			se.printStackTrace();
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
	}
}
