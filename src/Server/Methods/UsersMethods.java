package Server.Methods;

import Common.MessageManager;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class UsersMethods {

    // SEND USER CHATS
    // THE SERVER SEND: "cookies/true,chat1;false,chat2;true,1chat3;..."
    public void sendUserChats (InetAddress receiver, String user, HashMap<String, String[]> usersChats, DatagramSocket socket) throws IOException {
        String chats = "";
        for (String chat : usersChats.get(user))
            chats = chats.concat(chat).concat(";");
        String message = String.valueOf(new Random().nextInt()) + "/" + chats;
        MessageManager.LOGIN.sendMessage(receiver, message, socket);
    }



}

