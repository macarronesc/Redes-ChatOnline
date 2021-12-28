package Client.Methods;

import Common.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class AccountManager {
	/* USER REGISTRED */

	/**
	 * If the credentials are correct, a Client will be returned
	 *
	 * @param socket Socket to send data from
	 * @param server Server address
	 * @param name   Username
	 * @param pass   Password
	 * @return A client logged in if OK, null otherwise
	 * @throws Exception - IOException if there's a communication error, general exceptiion otherwise
	 */
	public static Client sign_in(DatagramSocket socket, DatagramSocket listenSocket, InetAddress server, String name, String pass) throws Exception {
		Client user;
		Message msg;

		MessageManager.LOGIN.sendMessage(server, name + Parameters.SEPARATOR + pass, socket);
		msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
		if (msg.getId() == MessageManager.ERROR.val()) {
			throw new Exception();
		} else {
			user = new Client(Integer.parseInt(msg.getData().split(Parameters.SEPARATOR)[0]), name);
			user.setActiveChats(putChats(msg.getData().split(Parameters.SEPARATOR)[1]));
		}

		return user;
	}

	public static HashMap<String, Chat> putChats(String message){
		HashMap<String, Chat> map = new HashMap<>();
		if (!message.equals("null")) {
			String[] chats = message.split(";");
			for (String chat : chats) {
				String[] chatAux = chat.split(",");
				map.put(chatAux[1], new Chat(Boolean.parseBoolean(chatAux[0])));
			}
		}
		return map;
	}

	/* USER NOT REGISTRED */

	/**
	 * Sends data to the server so an user will be created
	 *
	 * @param socket Socket to send data from
	 * @param server Server address
	 * @param name   Username
	 * @param pass   Password
	 * @return true if succes | false if not
	 * @throws Exception - IOException if there's a communication error, general exceptiion otherwise
	 */
	public static Boolean sign_up(DatagramSocket socket,  DatagramSocket listenSocket,InetAddress server, String name, String pass) throws Exception {
		Message msg;
		MessageManager.CREATE_USER.sendMessage(server, name + Parameters.SEPARATOR + pass, socket);
		msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
		return msg.getId() != MessageManager.ERROR.val();
	}
}
