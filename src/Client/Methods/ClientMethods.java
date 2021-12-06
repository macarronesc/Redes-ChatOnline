package Client.Methods;

import Common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ClientMethods {

    /* REQUEST UNREAD MESSAGES
    public static int requestUnreadMessages(DatagramSocket socket, DatagramSocket listen,
                                            InetAddress server, Client user) throws IOException {
        int unreadMessages = 0;
        String data = user.getUsername() + Parameters.SEPARATOR + user.getCookie();
        Message msg;

        MessageManager.REQUEST_UNREAD.sendMessage(server, data, socket);
        msg = MessageManager.RECEIVE.receiveMessage(listen);
        if (msg.getId() != MessageManager.ERROR.val())
            unreadMessages = Integer.parseInt(msg.getData());

        return unreadMessages;
    }

    public static String[] listPrivateChats(DatagramSocket socket, DatagramSocket listen,
                                            InetAddress server, Client user) throws IOException {
        String[] usersConnected = null;
        String data = user.getUsername() + Parameters.SEPARATOR + user.getCookie();
        Message msg;

        MessageManager.REQUEST_PRIVATE.sendMessage(server, data, socket);
        msg = MessageManager.RECEIVE.receiveMessage(listen);
        if (msg.getId() != MessageManager.ERROR.val()) {
            usersConnected = msg.getData().split(Parameters.SEPARATOR);
        }

        return usersConnected;
    }

     */


    public static String newChat(Client user, String guests, DatagramSocket socket,  DatagramSocket listenSocket, InetAddress server) throws IOException {
        if (user.getActiveChats().containsKey(guests))
            return "You already have a chat with: " + guests;
        else {
            MessageManager.EXIST_USER.sendMessage(server, guests, socket);
            Message msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
            if (msg.getId() == MessageManager.ERROR.val()) {
                return "Error, this user does not exist\n";
            } else {
                user.getActiveChats().put(guests, new Chat(false));
                MessageManager.CREATE_PRIVATE.sendMessage(server, user + Parameters.SEPARATOR + guests, socket);
                // El servidor le tendrá que enviar un mensaje al guests para que este se guarde que tiene ahora un nuevo chat con user
                return "Success";
            }
        }
    }

    /*public static String newGroup(Client user, String[] guests, DatagramSocket socket,  DatagramSocket listenSocket, InetAddress server) throws IOException {
        if (user.getActiveChats().containsKey(guests))
            return "You already have a chat with: " + guests;
        else {
            MessageManager.EXIST_USER.sendMessage(server, guests, socket);
            Message msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
            if (msg.getId() == MessageManager.ERROR.val()) {
                return "Error, this user does not exist\n";
            }
            return "Success";
        }
    }*/
}
