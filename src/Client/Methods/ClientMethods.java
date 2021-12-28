package Client.Methods;

import Common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;


public class ClientMethods {

    /**
     * Creates a chat with a specific user
     * @param user The client
     * @param guest Name of a possible user to chat with
     * @param socket Send socket
     * @param listenSocket Receive socket
     * @param server The server's address
     * @return An error or information message
     * @throws IOException If there's a problem with the socket
     */
    public static String newChat(Client user, String guest, DatagramSocket socket,  DatagramSocket listenSocket, InetAddress server) throws IOException {
        if (user.getActiveChats().containsKey(guest))
            return "You already have a chat with: " + guest;
        else {
            MessageManager.EXIST_USER.sendMessage(server, guest, socket);
            Message msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
            if (msg.getId() == MessageManager.ERROR.val()) {
                return "Error, this user("+guest+") does not exist\n";
            } else {
                if (!Objects.equals(guest, user.getUsername())) {
                    user.getActiveChats().put(guest, new Chat(false));
                    MessageManager.CREATE_PRIVATE.sendMessage(server, user.getUsername() + Parameters.SEPARATOR + guest, socket);
                    return "Success";
                } else
                    return "You can not create a new chat with yourself";
            }
        }
    }

    /**
     *
     * Creates a chat with a specific group
     * @param user The client
     * @param guests Name of the possible users to chat with, separated by commas
     * @param socket Send socket
     * @param listenSocket Receive socket
     * @param server The server's address
     * @return An error or information message
     * @throws IOException If there's a problem with the socket
     */
    public static String newGroup(Client user, String guests, String name, DatagramSocket socket,  DatagramSocket listenSocket, InetAddress server) throws IOException {
        StringBuilder answer = new StringBuilder();
        String[] guestsList = guests.split(",");
        if (user.getActiveChats().containsKey(name))
            return "You already have a group with name: " + name;
        else {
            for (String guest : guestsList) {
                MessageManager.EXIST_USER.sendMessage(server, guest, socket);
                Message msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
                if (msg.getId() == MessageManager.ERROR.val()) {
                    answer.append("Error, this user (").append(guest).append(") does not exist\n");
                    guests = guests.replace("," + guest, "");
                }
            }
            if (answer.toString().equals("")) {
                guests = guests.replace(user.getUsername() + ",", "");
                if (!guests.equals("")) {
                    MessageManager.CREATE_GROUP.sendMessage(server, user.getUsername() + Parameters.SEPARATOR + name + Parameters.SEPARATOR + guests, socket);
                    user.getActiveChats().put(name, new Chat(true));
                    return "Success";
                } else
                    return "You can not create a group with yourself only ";
            }
            return answer.toString();
        }
    }
}
