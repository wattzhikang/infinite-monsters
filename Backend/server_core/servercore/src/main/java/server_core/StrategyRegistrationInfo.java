package server_core;

/**
 * Dummy class encapsulating the information in
 * the client's JSON message
 * @author Zachariah Watt
 *
 */
public class StrategyRegistrationInfo {
	private String requestType;
	private String username;
	private String password;
	private String privileges;
	
	public StrategyRegistrationInfo(String requestType, String username, String password, String privileges) {
		this.requestType = requestType;
		this.username = username;
		this.password = password;
		this.privileges = privileges;
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

	public String getPrivileges() {
		return privileges;
	}
}
