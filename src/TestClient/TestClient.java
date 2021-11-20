package TestClient;

import Common.Message;
import Common.MessageManager;
import Common.Parameters;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DatagramSocket socket = null;
        InetAddress server = null;
        boolean error;
        Message msg;
        String data;


        /* TEST METHODS */
        try {
            server = InetAddress.getByName(Parameters.SERVER_IP);
            socket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        while (!(data = scan.nextLine()).equals("EOL")){
            error = MessageManager.SEND_PRIVATE.sendMessage(server, data, socket);
            System.out.println("Response error? " + error);

            msg = MessageManager.RECEIVE.receiveMessage(socket);
            System.out.println("Received ping from " + msg.getAddress() + ":" + msg.getPort());
        }

        System.out.println("Goodbye");
        /* TEST METHODS */

    }
}
