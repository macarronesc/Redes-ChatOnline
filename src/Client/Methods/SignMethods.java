package Client.Methods;

import Common.Message;
import Common.MessageManager;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

public class SignMethods {
	static Scanner scan = new Scanner(System.in);
	//TODO pasar datos por parametro desde ClientMain

	/* USER REGISTRED */
    /*
        Si las credenciales son correctas el server enviará los datos
        Si son incorrectas enviará en la cabecera 99 (mensaje de error)
     */
	public static String[] sign_in(DatagramSocket socket, InetAddress server) {
		String[] user = new String[2];
		Message msg;
		System.out.println("Please, log in: ");
		System.out.println("Username: ");
		user[0] = scan.nextLine().trim();
		System.out.println("Password: ");
		user[1] = scan.nextLine().trim(); //a lo mejor no guardar el password xd

		if (MessageManager.LOGIN.sendMessage(server, Arrays.toString(user), socket))
			System.out.println("Error in the auth");
		else {
			msg = MessageManager.LOGIN.receiveMessage(socket);
			if (msg.getId() == MessageManager.ERROR.val()) {
				System.out.println("Error in the answer of the server");
				System.out.println("Authentication failed");
				user = null; //TODO un poco guarro
			}
		}
		return user;
	}

	/* USER NOT REGISTRED */
    /*
        Si el usuario ya existe el server enviará los datos
        Sino enviará en la cabecera 99 (mensaje de error)
     */
	public static String[] sign_up(DatagramSocket socket, InetAddress server) {
		String[] user = new String[2];
		Message msg;
		System.out.println("New username: ");
		user[0] = scan.nextLine().trim();
		System.out.println("New password: ");
		user[1] = scan.nextLine().trim(); // no retornar passwd

		if (MessageManager.CREATE_USER.sendMessage(server, Arrays.toString(user), socket))
			System.out.println("Error in the auth");
		else {
			msg = MessageManager.LOGIN.receiveMessage(socket);
			if (msg.getId() == MessageManager.ERROR.val()) {
				System.out.println("Error in the answer of the server");
                System.out.println(msg.getData()); // server response
                user = null; //TODO un poco guarro
			}
		}
		return user;
	}
}
