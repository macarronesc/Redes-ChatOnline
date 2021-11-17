package Customer.Methods;

import Common.MessageManager;
import Common.Parameters;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

public class SignMethods {
    static Scanner scan = new Scanner(System.in);
    static String errorMessage = "99";

    /* USER REGISTRED */
    /*
        Si las credenciales son correctas el server enviará los datos
        Si son incorrectas enviará en la cabecera 99 (mensaje de error)
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
            else {
                String data = new String(answer.getData());
                if (data.substring(0, 2).equals(errorMessage))
                    System.out.println("Authentication failed");
                else
                    return user;
            }
        }
        return null;
    }

    /* USER NOT REGISTRED */
    /*
        Si el usuario ya existe el server enviará los datos
        Sino enviará en la cabecera 99 (mensaje de error)
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
            else {
                String data = new String(answer.getData());
                if (data.substring(0, 2).equals(errorMessage))
                    System.out.println("User already exists");
                else
                    return user;
            }
        }
        return null;
    }
}
