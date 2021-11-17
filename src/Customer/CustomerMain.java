package Customer;

import Common.MessageManager;
import Common.Parameters;

import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class CustomerMain {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        DatagramSocket socket = null;
        InetAddress server = null;

        String[] user_info;

        /* TO TEST */
        String msg;
        boolean error;


        /* TEST METHODS */
        try {
            server = InetAddress.getByName(Parameters.SERVER_IP);
            socket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }


        while(true) {
            user_info = getLogin(socket, server);

            if (user_info == null)
                System.out.println("ERROR");
            else
                System.out.println("Correct authentication");
            break;
        }

        /*while (!(msg = scan.nextLine()).equals("EOL")){
            error = MessageManager.SEND_PRIVATE.sendMessage(server, Parameters.LISTEN_PORT, msg, socket);
            System.out.println("Response error? " + error);
        }*/


        //MOSTRAR MENU
        mainMenu(0, scan);
        System.out.println("Goodbye");
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

    /* USER REGISTRED */
    /*
        Si las credenciales son correctas el server enviara 1
        Si son incorrectas enviará 0
     */
    public static String[] sign_in(DatagramSocket socket, InetAddress server){
        String[] user = new String[2];
        System.out.println("Please, log in: ");
        System.out.println("Username: ");
        user[0] = scan.nextLine().trim();
        System.out.println("Password: ");
        user[1] = scan.nextLine().trim();

        if(MessageManager.LOGIN.sendMessage(server, Parameters.LISTEN_PORT, Arrays.toString(user), socket))
            System.out.println("Error in the auth");
        else {
            DatagramPacket answer = MessageManager.LOGIN.receiveMessage(socket);
            if (answer == null)
                System.out.println("Error in the answer of the server");
            else if (answer.getData()[0] ==  (byte) 0)
                System.out.println("Authentication failed");
            else
                return user;
        }
        return null;
    }

    /* USER NOT REGISTRED */
    /*
        Si el usuario ya existe el server enviara 1
        Sino enviará 0
     */
    public static String[] sign_out(DatagramSocket socket, InetAddress server){
        String[] user = new String[2];
        System.out.println("New username: ");
        user[0] = scan.nextLine().trim();
        System.out.println("New password: ");
        user[1] = scan.nextLine().trim();

        if(MessageManager.CREATE_USER.sendMessage(server, Parameters.LISTEN_PORT, Arrays.toString(user), socket))
            System.out.println("Error in the auth");
        else {
            DatagramPacket answer = MessageManager.LOGIN.receiveMessage(socket);
            if (answer == null)
                System.out.println("Error in the answer of the server");
            else if (answer.getData()[0] == (byte) 0)
                System.out.println("User already exists");
            else
                return user;
        }
        return null;
    }

    public static void mainMenu(int unread, Scanner scan){
        int option;
        System.out.printf("You have %i unread messages\n", unread);
        System.out.println("(1) List my private chats");
        System.out.println("(2) List my group chats");
        System.out.println("(r) Refresh");
    }
}
