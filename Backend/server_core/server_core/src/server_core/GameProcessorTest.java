package server_core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
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
		final int threshold = 12;
		
		final String SENDMESSAGE = "Message Test";
		
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		while (retryCounter < threshold) {
			try {
				sendSocket = new Socket("localhost", ServerCore.PORT);
				out = new ObjectOutputStream(sendSocket.getOutputStream());
				in = new ObjectInputStream(sendSocket.getInputStream());
				
				out.writeObject("Message Test");
				
				String message = in.readObject().toString();
				
				if (message.equals(SENDMESSAGE)) {
					System.out.println(message);
					/*
					 * If program fails to shut down gracefully, this
					 * will give the VM time to print its stack trace.
					 */
					Thread.sleep(1000);
					return;
				} else {
					fail("Message Received: " + message);
				}
			} catch (IOException e) {
				e.printStackTrace();
				if (retryCounter <= threshold) {
					retryCounter++;
					System.out.println("Retry " + retryCounter);
					Thread.sleep(1000);
				}
				continue;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Sent a totally different object than expected");
			}
		}
	}

	private class ServerTester extends Thread {
		public void run() {
			ServerCore.main(null);
		}
	}

}
