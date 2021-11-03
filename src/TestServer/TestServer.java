package TestServer;

import Common.Parameters;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class TestServer {
	public static void main(String[] args) {
		try {
			DatagramSocket socket = new DatagramSocket(Parameters.LISTEN_PORT);
			byte[] buffer;
			DatagramPacket packet;
			String data;

			while (true) {
				buffer = new byte[Parameters.MAX_BUFFER_SIZE];
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				data = new String(packet.getData()); //String from bytes
				System.out.println("[SERVER] IP\t" + packet.getAddress());
				System.out.println("[SERVER] PORT\t" + packet.getPort());
				System.out.println("[SERVER] LENGTH\t" + packet.getLength());
				System.out.println("[SERVER] DATA\t" + data);
				System.out.println("[SERVER] MSG TYPE\t" + data.substring(0,2));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
