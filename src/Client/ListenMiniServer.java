package Client;

import Common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ListenMiniServer implements Runnable {
	private static int NUM_THREADS = 20;
	private final DatagramSocket listenSocket;
	private DatagramSocket selfSocket;
	private Thread[] msgProcess;
	private int threadInd;
	private Client user;
	private boolean running;

	public ListenMiniServer(DatagramSocket listenSocket, Client user) throws Exception {
		selfSocket = new DatagramSocket(); //TODO no necesario, pruebas, quitar cuando server tenga thread
		this.listenSocket = listenSocket;
		this.user = user;
		msgProcess = new Thread[NUM_THREADS];
		threadInd = 0;
	}

	@Override
	public void run() {
		MessageToChat convert;
		Message msg;
		running = true;
		System.out.println("Listen Mini Server running");
		while (running) {
			try {
				MessageManager.REQUEST_MESSAGE.sendMessage(InetAddress.getByName(Parameters.SERVER_IP), "", selfSocket);
				// No timeout is set, port is open forever for an incoming message
				msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
				//TODO thread para procesar msg? Por si viene troceado o algo, ya se vera

				System.out.println("Received message! " + msg);
				convert = new MessageToChat(msg, user);
				convert.run();
				this.stop(); // quitar
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		running = false;
	}
}
