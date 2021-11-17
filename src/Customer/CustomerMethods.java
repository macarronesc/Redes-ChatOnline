package Customer;

import Common.MessageManager;
import Common.Parameters;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

import static Customer.Methods.SignMethods.*;


public class CustomerMethods {
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
            return sign_out(socket, server);
    }

    /* REQUEST UNREAD MESSAGES */
    public static int request_unread_messages(DatagramSocket socket, InetAddress server){
        int unreadMessages = -1;
        while(unreadMessages == -1)
            unreadMessages = get_unread_messages(socket, server);

        return unreadMessages;
    }

    /* ASK THE SERVER FOR THE UNREAD MESSAGES */
    public static int get_unread_messages(DatagramSocket socket, InetAddress server){
        MessageManager.REQUEST_UNREAD.sendMessage(server, Parameters.LISTEN_PORT, "", socket);
        DatagramPacket answer = MessageManager.REQUEST_UNREAD.receiveMessage(socket);
        if (answer != null) {
            String data = new String(answer.getData());
            data = data.substring(2, answer.getLength());

            return Integer.parseInt(data);
        }
        System.out.println("ERROR, in the request of the unread messages");
        return -1;
    }

    // LOS CHATS IRAN SEPARADOS POR "|"
    public static void list_private_chats(DatagramSocket socket, InetAddress server, String[] userInfo){
        String AllprivateChats = null;
        while (AllprivateChats == null)
            AllprivateChats = get_private_chats(socket, server, userInfo);

        String [] privateChats = AllprivateChats.split("\\|");
        System.out.println("\n\n\n\n\n-----------------------------------------------------------------------");
        System.out.println("You have a private chat with:\n");
        for (String privateChat: privateChats) {
            System.out.println(privateChat);
        }
        System.out.println("-----------------------------------------------------------------------");
    }

    public static String get_private_chats(DatagramSocket socket, InetAddress server, String[] userInfo){
        MessageManager.REQUEST_PRIVATE.sendMessage(server, Parameters.LISTEN_PORT, Arrays.toString(userInfo), socket);
        DatagramPacket answer = MessageManager.REQUEST_PRIVATE.receiveMessage(socket);
        if (answer != null)
            return new String(answer.getData()).substring(2, answer.getLength());
        return null;
    }
}
