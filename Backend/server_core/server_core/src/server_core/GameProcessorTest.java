package server_core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket;

import org.junit.jupiter.api.Test;

class GameProcessorTest {

	@Test
	void test() throws BindException, InterruptedException {
		ServerTester server = new ServerTester();
		server.start();
		Socket sendSocket = null;
		int retryCounter = 0;
		int threshold = 12;

		ObjectOutputStream out = null;
		boolean success = false;
		while (!success) {
			while (retryCounter < threshold) {
				try {
					sendSocket = new Socket("localhost", ServerCore.PORT);
					out = new ObjectOutputStream(sendSocket.getOutputStream());
					out.writeObject("Message Test");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fail("failure");
				}
			}
			// socket failed to connect
			if (sendSocket == null) {
				retryCounter = 0;
				Thread.sleep(5000);
				continue;
			}
			// succeeded in connection
			else {
				success = true;
			}
		}
	}

	private class ServerTester extends Thread {
		public void run() {
			ServerCore.main(null);
		}
	}

}
