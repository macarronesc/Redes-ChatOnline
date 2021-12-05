package Client.Methods;

import Common.Client;
import Common.Message;
import Common.MessageManager;
import Common.Parameters;

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
}
