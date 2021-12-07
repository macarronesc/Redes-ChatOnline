package Server.Methods;

import Common.MessageManager;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

    public static void createNewChat (String sender, String receiver, HashMap<String, InetAddress> userToIp, HashMap<String, String> usersChats, DatagramSocket socket) throws IOException {
        usersChats.replace(sender, usersChats.get(sender), usersChats.get(sender).concat(";false,").concat(receiver));
        usersChats.replace(receiver, usersChats.get(receiver), usersChats.get(receiver).concat(";false,").concat(sender));
        MessageManager.CREATE_PRIVATE.sendMessage(userToIp.get(receiver), sender, socket);
    }

    // THE MESSAGE HAS THE FORMAT: nameOfGroup/user1,user2,user3,...
    public static void createNewGroup (String sender, String message, HashMap<String, InetAddress> userToIp, HashMap<String, String> usersChats, DatagramSocket socket) throws IOException {
        String name = message.split("/")[0];
        String[] guests = message.split("/")[1].split(",");
        usersChats.replace(sender, usersChats.get(sender), usersChats.get(sender).concat(";true,").concat(name));

        for (String guest : guests){
            usersChats.replace(guest, usersChats.get(guest), usersChats.get(guest).concat(";true,").concat(name));
            MessageManager.CREATE_GROUP.sendMessage(userToIp.get(guest), name, socket);
        }
    }

    /* TO-DO */
    public static void communicationPrivateChat(){

    }

    /* TO-DO */
    public static void communicationGroups(){

    }
}

