package server_core;

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
