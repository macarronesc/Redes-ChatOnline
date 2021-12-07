package Client.Methods;

import Common.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

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
			user = new Client(Integer.parseInt(msg.getData().split("/")[0]), name);
			HashMap<String, Chat> map = new HashMap<>();
			String[] chats = msg.getData().split("/")[1].split(";");

			for(String chat : chats){
				String[] chatAux = chat.split(",");
				map.put(chatAux[1], new Chat(Boolean.parseBoolean(chatAux[0])));
			}
			user.setActiveChats(map);
		}

		return user;
	}

	/* USER NOT REGISTRED */

	/**
	 * Sends data to the server so an user will be created
	 *
	 * @param socket Socket to send data from
	 * @param server Server address
	 * @param name   Username
	 * @param pass   Password
	 * @throws Exception - IOException if there's a communication error, general exceptiion otherwise
	 */
	public static void sign_up(DatagramSocket socket,  DatagramSocket listenSocket,InetAddress server, String name, String pass) throws Exception {
		Message msg;
		MessageManager.CREATE_USER.sendMessage(server, name + Parameters.SEPARATOR + pass, socket);
		msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
		if (msg.getId() == MessageManager.ERROR.val()) {
			throw new Exception();
		}
	}
}
