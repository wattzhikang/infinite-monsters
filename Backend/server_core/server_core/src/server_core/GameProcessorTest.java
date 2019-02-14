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
			Thread.sleep(5000);
			Socket sendSocket = new Socket("localhost", ServerCore.PORT);
			out = new ObjectOutputStream(sendSocket.getOutputStream());
			out.writeObject("Message Test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("failure");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class ServerTester extends Thread {
		public void run() {
			ServerCore.main(null);
		}
	}

}
