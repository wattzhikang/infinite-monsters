package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import server_core.ServerCore;

class FullyLocalTest {

	@Test
	void test() {
		ServerThread server = new ServerThread();
		server.start();
		
		Socket socket = null;
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
				socket = new Socket("localhost", ServerCore.PORT_TCP);
				break;
			} catch (UnknownHostException e) {
				//e.printStackTrace();
				fail("Unknown host");
			} catch (IOException e) {
				e.printStackTrace();
				fail("IOException");
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail("Interrupted");
			}
		}
		if (socket == null) {
			fail("Failed to initiate connection");
		}
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			
			out.writeObject("{\"requestType\":\"registration\",\"username\":\"user1\",\"password\":\"sunshine\",\"privileges\":\"player\"}");
			
			System.out.println("Testing Thread: Received " + in.readObject().toString());
			
			out.writeObject("{\"requestType\":\"authentication\",\"username\":\"user1\",\"password\":\"sunshine\"}");
			
			System.out.println("Testing Thread: Received " + in.readObject().toString());
			
			out.writeObject("{\"requestType\":\"subscription\"}");
			
			System.out.println("Testing Thread: Received " + in.readObject().toString());
			
			out.writeObject("{"
					+ "\"requestType\":\"mod_move_subscription\","
					+ "\"subscriptionID\":0,"
					+ "\"xL\":1,"
					+ "\"xR\":4,"
					+ "\"yU\":2,"
					+ "\"yL\":0,"
					+ "\"oldPlayerX\":1,"
					+ "\"oldPlayerY\":1,"
					+ "\"newPlayerX\":2,"
					+ "\"newPlayerY\":1"
					+ "}"
			);
			
			
			System.out.println("Testing Thread: Received " + in.readObject().toString());
			System.out.println("Testing Thread: Received " + in.readObject().toString());
			
			//Thread.sleep(100);
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Got a totally different object");
		} //catch (InterruptedException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}

private class ServerThread extends Thread {
	@Override
	public void run() {
		String[] args = {ServerCore.DEBUG};
		ServerCore.main(args);
	}
}
}
