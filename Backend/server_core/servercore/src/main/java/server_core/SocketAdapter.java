package server_core;

import java.io.EOFException;
import java.io.IOException;

/**
 * Implemented by anything that can represent a connection to the server.
 * @author zjwatt
 *
 */
public interface SocketAdapter {

	public String readString() throws IOException, EOFException;
	
	public void writeString(String string);
	
	public boolean isClosed();
	
	public void close();

	@Override
	public String toString();
	
}