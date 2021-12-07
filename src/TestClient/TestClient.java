package TestClient;

import Common.Message;
import Common.MessageManager;
import Common.Parameters;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DatagramSocket socket, listenSocket;
        InetAddress server = null;
        Message msg = null;
        String data;


        /* TEST METHODS */
        try {
            server = InetAddress.getByName(Parameters.SERVER_IP);
            socket = new DatagramSocket();
            listenSocket = new DatagramSocket(Parameters.CLIENT_LISTEN_PORT);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return;
        }

        data = scan.nextLine();
        while (!data.equals("EOL")){
            System.out.println("a");
            try {
                MessageManager.SEND_PRIVATE.sendMessage(server, data, socket);
                msg = MessageManager.RECEIVE.receiveMessage(listenSocket);

                System.out.println("Received ping from " + msg.getAddress() + ":" + msg.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Goodbye");
        /* TEST METHODS */

    }
}
