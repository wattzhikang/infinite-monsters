package server_core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAdapter implements DBInterface {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/intermondb";
	private static final String USER = "root";
	private static final String PASSWORD = "Abundant space 421 points!";
	
	private static final String USER_TABLE = "intermondb.user";

	Connection connection = null;
	
	public DBAdapter() {
		Statement statement = null;
		ResultSet results = null;
		try {
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			System.out.println("Connection established");
			
			System.out.println("Creating statement");
			statement = connection.createStatement();
			System.out.println("Statement Created");
			
			
			String sql = "SELECT UserName FROM intermondb.user";
			System.out.println("Executing Query...");
			results = statement.executeQuery(sql);
			System.out.println("Query Successfully Executed");
			
			System.out.println("Printing Usernames");
			while (results.next()) {
				System.out.println(results.getString("UserName"));
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
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
			String sql = "SELECT UserName, Password FROM " + USER_TABLE + " WHERE UserName='"
					+ username + "' AND Password='" + password + "';";
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
			String sql = "SELECT UserName FROM " + USER_TABLE + " WHERE UserName='" + username + "';";
			results = statement.executeQuery(sql);
			
			if (results.next()) {
				return false;
			}
			
			//if user is not in database, create an entry
			statement.close();
			
			statement = connection.createStatement();
			sql = "INSERT INTO " + USER_TABLE + " VALUES ('" + username + "', '" + password + "');";
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
}
