package server_core;

public class SocketMessage {
	
	public enum MessageOrigin { CLIENT, SERVER, POISON };
	
	private SocketMessage.MessageOrigin origin;
	private String JSONMessage;
	
	public SocketMessage(SocketMessage.MessageOrigin origin, String JSONMessage) {
		this.origin = origin;
		this.JSONMessage = JSONMessage;
	}
	
	public SocketMessage.MessageOrigin getOrigin() {
		return origin;
	}
	
	public String getMessage() {
		return JSONMessage;
	}

}
