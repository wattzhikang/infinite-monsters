package server_core;

public class Message {
		private String message;
		private SocketListener client;
		
		public Message(String message, SocketListener client) {
			this.message = message;
			this.client = client;
		}
		
		public SocketListener getClient() {
			return client;
		}
		
		@Override
		public String toString() {
			return message;
		}
}
