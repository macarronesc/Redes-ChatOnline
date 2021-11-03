package Cliente;

import Common.MessageManager;
import Common.Parameters;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DatagramSocket socket = null;
        String msg;
        Client client;
        InetAddress server = null;
        boolean error;


        /* TEST METHODS */
        try {
            server = InetAddress.getByName(Parameters.SERVER_IP);
            socket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        while (!(msg = scan.nextLine()).equals("EOL")){
            error = MessageManager.SEND_PRIVATE.sendMessage(server, Parameters.LISTEN_PORT, msg, socket);
            System.out.println("Response error? " + error);
        }

        System.out.println("Goodbye");
        /* TEST METHODS */

    }

    public static String[] getLogin() {
        String[] user = new String[2];
        Scanner scan = new Scanner((System.in));
        System.out.println("Please, log in: ");
        System.out.println("Username: ");
        user[0] = scan.nextLine().trim();
        System.out.println("Password: ");
        user[1] = scan.nextLine().trim();
        //todo validate user
        return user;
    }

    public static void mainMenu(int unread, Scanner scan){
        int option;
        System.out.printf("You have %i unread messages\n", unread);
        System.out.println("(1) List my private chats");
        System.out.println("(2) List my group chats");
        System.out.println("(r) Refresh");


    }
}
