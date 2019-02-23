package server_core;

public class DBDummy implements DBInterface {

	@Override
	public boolean login(String username, String password) {
		return true;
	}

	@Override
	public void close() {
		return;
	}

}
