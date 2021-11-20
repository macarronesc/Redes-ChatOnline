package Common;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Message {
	private int id;
	private DatagramPacket packet;
	private String data;

	public Message(int id){
		this.id = id;
	}

	public Message(DatagramPacket packet, String data) {
		this.packet = packet;

		this.data = data.length() > 2 ? this.data = data.substring(2) : "";
		id = Integer.parseInt(data.substring(0,2));
	}

	public int getId() {
		return id;
	}

	public DatagramPacket getPacket() {
		return packet;
	}

	public String getData() {
		return data;
	}

	public InetAddress getAddress(){
		return packet.getAddress();
	}

	public int getPort(){
		return packet.getPort();
	}

	public int getLength(){
		return packet.getLength();
	}
}
