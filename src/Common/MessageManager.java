package Common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public enum MessageManager {

	//Signal to send and recieve signals from connection between server and client

	// 0x users
	LOGIN(00),
	CREATE_USER(01),
	// 1x groups
	LIST_GROUP(10),
	CREATE_GROUP(11),
	DELETE_GROUP(12),
	// 2x private
	LIST_PRIVATE(20),
	CREATE_PRIVATE(21),
	DELETE_PRIVATE(22),
	EXIST_USER(23),
	// 3x send
	SEND_PRIVATE(30),
	SEND_GROUP(31),
	// 4x request
	REQUEST_MESSAGE(40),
	REQUEST_PRIVATE(41),
	REQUEST_CHATS(42),
	//9x others
	TEST(97),
	RECEIVE(98),
	ERROR(99);

	private int num;

	public int val() {
		return num;
	}

	MessageManager(int num) {
		this.num = num;
	}

	public void sendMessage(InetAddress receiver, String message, DatagramSocket socket) throws IOException {
		InetAddress sender = null;
		DatagramPacket packet;
		boolean error = false;
		byte[] data;

		// Add the padded code with 1 zero before the message
		data = String.format("%02d%s", num, message).getBytes(StandardCharsets.UTF_8);


        if (receiver.toString().contains("localhost"))
		    packet = new DatagramPacket(data, data.length, receiver, Parameters.LISTEN_PORT);
        else
            packet = new DatagramPacket(data, data.length, receiver, Parameters.CLIENT_LISTEN_PORT);
		socket.send(packet);
	}

	/**
	 * Receive a newtwork message using a specific socket
	 * @param socket The listen socket that will receive the message
	 * @return A Message object containing the message
	 * @throws IOException If there's a problem with the message
	 */
	public Message receiveMessage(DatagramSocket socket) throws IOException {
		byte[] buffer;
		String data;
		Message msg;

		buffer = new byte[1000];
		DatagramPacket answer = new DatagramPacket(buffer, buffer.length);
		socket.receive(answer);
		data = new String(answer.getData());
		msg = new Message(answer, data);

		return msg;
	}

	public String[] processMessage(Message msg){

		return null;
	}
}
