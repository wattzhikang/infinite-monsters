package server_core;

public class ClientKey {
	public enum Privileges {SPECTATOR, PLAYER, DESIGNER};
	
	private String user;
	private Privileges priveleges;
	
	public ClientKey(String username, Privileges privileges) {
		this.user = user;
		this.priveleges = privileges;
	}

	public Privileges getPriveleges() {
		return priveleges;
	}
	
	public String getUser() {
		return user;
	}
}
