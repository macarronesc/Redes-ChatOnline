package TestServer;

import Common.Message;
import Common.MessageManager;
import Common.Parameters;

import java.io.IOException;
import java.net.*;

public class TestServer {
	public static void main(String[] args) {
		try {
			DatagramSocket socket = new DatagramSocket(Parameters.LISTEN_PORT);
			Message msg;

			while (true) {
				msg = MessageManager.RECEIVE.receiveMessage(socket);
				System.out.println("[SERVER] IP\t" + msg.getAddress());
				System.out.println("[SERVER] PORT\t" + msg.getPort());
				System.out.println("[SERVER] LENGTH\t" + msg.getLength());
				System.out.println("[SERVER] DATA\t" + msg.getData().substring(2, msg.getLength()));
				System.out.println("[SERVER] MSG TYPE\t" + msg.getData().substring(0,2));
				// send a ping to the client
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
