package server_core;

/**
 * Dummy class encapsulating the information in
 * the client's JSON message
 * @author zjwatt
 *
 */
public class StrategyAuthenticationInfo {
	private String requestType;
	private String username;
	private String password;
	
	public StrategyAuthenticationInfo(String requestType, String username, String password) {
		super();
		this.requestType = requestType;
		this.username = username;
		this.password = password;
	}

	public String getRequestType() {
		return requestType;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	
}
