package TestServer;

import Common.Message;
import Common.MessageManager;
import Common.Parameters;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class TestServer {
	public static void main(String[] args) {
		try {
			Random rand = new Random();
			DatagramSocket socket = new DatagramSocket();
			DatagramSocket listenSocket = new DatagramSocket(Parameters.LISTEN_PORT);
			Message msg;
			InetAddress client;

			while (true) {
				msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
				System.out.println("[SERVER] IP\t" + msg.getAddress());
				System.out.println("[SERVER] PORT\t" + msg.getPort());
				System.out.println("[SERVER] LENGTH\t" + msg.getLength());
				System.out.println("[SERVER] DATA\t" + msg.getData());
				System.out.println("[SERVER] MSG TYPE\t" + msg.getId());

				// send an OK response to the server (temp)
				if (msg.getId() == MessageManager.LOGIN.val())
					MessageManager.LOGIN.sendMessage(msg.getAddress(), String.valueOf(rand.nextInt()), socket);
				if (msg.getId() == MessageManager.CREATE_USER.val())
					MessageManager.CREATE_USER.sendMessage(msg.getAddress(), String.valueOf(rand.nextInt()), socket);
				//if (msg.getId() == MessageManager.LIST_GROUP.val() )
				//if (msg.getId() == MessageManager.CREATE_GROUP.val() )
				//if (msg.getId() == MessageManager.DELETE_GROUP.val() )
				//if (msg.getId() == MessageManager.LIST_PRIVATE.val() )
				//if (msg.getId() == MessageManager.CREATE_PRIVATE.val() )
				//if (msg.getId() == MessageManager.DELETE_PRIVATE.val() )
				//if (msg.getId() == MessageManager.SEND_PRIVATE.val() )
				//if (msg.getId() == MessageManager.SEND_GROUP.val() )
				if (msg.getId() == MessageManager.REQUEST_UNREAD.val())
					MessageManager.REQUEST_UNREAD.sendMessage(msg.getAddress(), String.valueOf(69), socket);
				if (msg.getId() == MessageManager.REQUEST_PRIVATE.val())
					MessageManager.REQUEST_PRIVATE.sendMessage(msg.getAddress(), "NotMeNotYou\0ur mom\0ricardo milos\0sadpeepo", socket);
				//if (msg.getId() == MessageManager.RECEIVE.val() )
				//if (msg.getId() == MessageManager.ERROR.val() )


			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
