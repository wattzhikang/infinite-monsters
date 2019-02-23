package server_core;

public interface DBInterface {
	public boolean login(String username, String password);
	public void close();
}
