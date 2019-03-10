package server_core;

public class StrategyAuthenticationSuccess {
	private boolean loginSuccess;

	public StrategyAuthenticationSuccess(boolean loginSuccess) {
		super();
		this.loginSuccess = loginSuccess;
	}

	public boolean getLoginSuccess() {
		return loginSuccess;
	}
}
