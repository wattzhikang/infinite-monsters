package server_core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.junit.jupiter.api.Test;

class GameProcessorTest {

	@Test
	void test() {
		ServerTester server = new ServerTester();
		server.start();
		
		ObjectOutputStream out = null;
		try {
			Socket sendSocket = new Socket("127.0.0.1", ServerCore.PORT);
			out = new ObjectOutputStream(sendSocket.getOutputStream());
			out.writeObject("Message Test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("failure");
		}
	}
	
	private class ServerTester extends Thread {
		public void run() {
			ServerCore.main(null);
		}
	}

}
