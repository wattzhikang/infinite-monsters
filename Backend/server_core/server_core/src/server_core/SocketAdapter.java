package server_core;

import java.io.EOFException;
import java.io.IOException;

public interface SocketAdapter {

	public String readString() throws IOException, EOFException;
	
	public void writeString(String string);
	
	public boolean isClosed();
	
	public void close();
	
}