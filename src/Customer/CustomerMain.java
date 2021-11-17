package Customer;

import Common.Parameters;
import java.net.*;
import java.util.Scanner;
import static Customer.CustomerMethods.*;

public class CustomerMain {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        DatagramSocket socket;
        InetAddress server;
        String[] userInfo;


        /* TEST METHODS */
        try {
            server = InetAddress.getByName(Parameters.SERVER_IP);
            socket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return;
        }

        /* AUTH OF THE USER */
        userInfo = auth(socket, server);

        /* MAIN MENU */
        int option = scan.nextInt();
        while (option == 0) {
            mainMenu(request_unread_messages(socket, server));
             switch (option){
                 case 1:
                     list_private_chats(socket,server, userInfo);
                     break;
                 case 2:
                     //list_private_chats(socket,server);
                     break;
                 case 3:
                     //list_private_chats(socket,server);
                     break;
                 case 4:
                     //list_private_chats(socket,server);
                     break;
             }
             option = scan.nextInt();
        }
        System.out.println("Goodbye");
    }

    public static void mainMenu(int unread){
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("You have " + unread+ " unread messages\n");
        System.out.println("(1) List my private chats");
        System.out.println("(2) Create a new private chat");
        System.out.println("(3) List my group chats");
        System.out.println("(4) Create a new group chat");
        // YO PONDRIA ESTA OPCION DENTRO DE 1 Y 2
        //System.out.println("(r) Refresh");
        System.out.println("(0) Exit");
        System.out.println("-----------------------------------------------------------------------");
    }
}
