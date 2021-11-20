package Client.Methods;

import Common.Message;
import Common.MessageManager;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

import static Client.Methods.SignMethods.*;


public class ClientMethods {
    static Scanner scan = new Scanner(System.in);

    /* AUTH METHOD */
    public static String[] auth(DatagramSocket socket, InetAddress server){
        String[] userInfo = null;
        while(userInfo == null) {
            userInfo = getLogin(socket, server);

            if (userInfo == null)
                System.out.println("ERROR. Try again");
            else
                System.out.println("Correct authentication");
        }
        return userInfo;
    }

    /* PPAL METHOD */
    public static String[] getLogin(DatagramSocket socket, InetAddress server){
        System.out.println("Are you registred?");
        System.out.println("Press ([y]/n)");
        String answer = scan.nextLine();
        if ((answer.equalsIgnoreCase("y")) || (answer.equalsIgnoreCase("yes")))
            return sign_in(socket, server);
        else
            return sign_up(socket, server);
    }

    /* REQUEST UNREAD MESSAGES */
    public static int request_unread_messages(DatagramSocket socket, DatagramSocket listen, InetAddress server){
        int unreadMessages = -1;
        Message msg;

        if (!MessageManager.REQUEST_UNREAD.sendMessage(server, "", listen)){
            msg = MessageManager.REQUEST_UNREAD.receiveMessage(listen);
            if (msg.getId() != MessageManager.ERROR.val())
                unreadMessages = Integer.parseInt(msg.getData());
        }
        return unreadMessages;
    }

    // LOS CHATS IRAN SEPARADOS POR "|"
    public static void list_private_chats(DatagramSocket socket, DatagramSocket listen,
                                          InetAddress server, String[] userInfo){
        String allprivateChats = null;
        Message msg;

        if (!MessageManager.REQUEST_PRIVATE.sendMessage(server, Arrays.toString(userInfo), socket))
        {
            // Wait for server response
            while (allprivateChats == null) {
                allprivateChats = MessageManager.RECEIVE.receiveMessage(listen).getData();
            }

            String [] privateChats = allprivateChats.split("\\|");
            //TODO por favo retornalo y tratalo en el main (o otra clase como ClienteConsola)
            System.out.println("\n\n\n\n\n-----------------------------------------------------------------------");
            System.out.println("You have a private chat with:\n");
            for (String privateChat: privateChats) {
                System.out.println(privateChat);
            }
            System.out.println("-----------------------------------------------------------------------");
        } else {
            //error
        }
    }
}
