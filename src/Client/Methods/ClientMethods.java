package Client.Methods;

import Common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ClientMethods {
    public static String newChat(Client user, String guest, DatagramSocket socket,  DatagramSocket listenSocket, InetAddress server) throws IOException {
        if (user.getActiveChats().containsKey(guest))
            return "You already have a chat with: " + guest;
        else {
            MessageManager.EXIST_USER.sendMessage(server, guest, socket);
            Message msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
            if (msg.getId() == MessageManager.ERROR.val()) {
                return "Error, this user("+guest+") does not exist\n";
            } else {
                user.getActiveChats().put(guest, new Chat(false));
                MessageManager.CREATE_PRIVATE.sendMessage(server, user + Parameters.SEPARATOR + guest, socket);
                // El servidor le tendrá que enviar un mensaje al guests para que este se guarde que tiene ahora un nuevo chat con user
                return "Success";
            }
        }
    }

    public static String newGroup(Client user, String guests, String name, DatagramSocket socket,  DatagramSocket listenSocket, InetAddress server) throws IOException {
        StringBuilder answer = new StringBuilder();
        String[] guestsList = guests.split(",");
        for (String guest : guestsList) {
            MessageManager.EXIST_USER.sendMessage(server, guest, socket);
            Message msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
            if (msg.getId() == MessageManager.ERROR.val()) {
                answer.append("Error, this user (").append(guest).append(") does not exist\n");
                guests = guests.replace("," + guest, "");
            }
        }
        if (answer.toString().equals("")) {
            MessageManager.CREATE_GROUP.sendMessage(server, name + "/" + guests, socket);
            // El servidor le tendrá que enviar un mensaje al guests para que este se guarde que tiene ahora un nuevo chat con user
            user.getActiveChats().put(name, new Chat(true));
            return "Success";
        }
        return answer.toString();
    }
}
