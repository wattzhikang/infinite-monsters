package server_core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.junit.jupiter.api.Test;

class FullyRemoteTest {

	@Test
	void test() {
		int retryCounter = 0;
		final int retryLimit = 10;
		
		Socket sendSocket = null;
		
		final String SENDMESSAGE = "Message Test";
		
		for (; retryCounter < retryLimit; retryCounter++) {
			try {
				sendSocket = new Socket("cs309-yt-1.misc.iastate.edu", ServerCore.PORT);
				ObjectOutputStream out = new ObjectOutputStream(sendSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(sendSocket.getInputStream());
				
				out.writeObject("Message Test");
				
				String message = in.readObject().toString();
				
				if (message.equals(SENDMESSAGE)) {
					System.out.println(message);
					sendSocket.close();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Interrupted for some reason");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				fail("Sent a totally different object than expected");
			}
		}

		
		
		fail("Not yet implemented");
	}

}
