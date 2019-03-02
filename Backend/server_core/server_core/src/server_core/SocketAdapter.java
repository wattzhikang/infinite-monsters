package server_core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketAdapter {
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public SocketAdapter(Socket socket) {
		this.socket = socket;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			System.out.println("output flushed for " + socket.getInetAddress().toString());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}
	
	public String getHost() {
		return (socket.getInetAddress() != null) ? socket.getInetAddress().toString() : null;
	}
	
	public String readString() throws IOException {
		String read = null;
		try {
			read = (String) in.readObject();
		} catch (ClassNotFoundException e) {
			//malformed input
			return null;
		} catch (IOException e) {
			//TODO check to see if it's because the connection was dropped
			throw e;
		}
		return read;
	}
	
	public void writeString(String toWrite) {
		try {
			out.writeObject((Object)toWrite);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isClosed() {
		return socket.isClosed();
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}