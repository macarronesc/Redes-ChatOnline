package TestCustomer;

import Common.MessageManager;
import Common.Parameters;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestCustomer {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DatagramSocket socket = null;
        String msg;
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
}
