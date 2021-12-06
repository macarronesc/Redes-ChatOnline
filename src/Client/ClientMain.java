package Client;

import Client.Methods.AccountManager;
import Client.Methods.ClientMethods;
import Common.Chat;
import Common.Client;
import Common.Parameters;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {
	private static final Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		ListenMiniServer miniServer;
		Thread miniServerThread;
		DatagramSocket sendSocket, listenSocket;
		InetAddress server;
		Client user = null;
		boolean error;
		int option;


		/* TEST METHODS */
		try {
			server = InetAddress.getByName(Parameters.SERVER_IP);
			sendSocket = new DatagramSocket();
			listenSocket = new DatagramSocket(Parameters.CLIENT_LISTEN_PORT); //TODO OJO PRUEBAS
			//listenSocket = new DatagramSocket(Parameters.LISTEN_PORT);
			//listenSocket.setSoTimeout(Parameters.TIMEOUT_DEFAULT);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
			return;
		}

		//Sign in or up
		do {
			error = false;
			option = welcomeMenu(); //1 = login, 2 = register
			switch (option) {
				case 0 -> {
					return;
				}
				case 1 -> user = loginMenu(sendSocket, listenSocket, server, false);
				case 2 -> user = loginMenu(sendSocket, listenSocket, server, true);
				default -> error = true;
			}
			if (user == null) error = true;
		} while (error);

		// Sockets created and user authenticated, create the listen thread
		// Thread runs constantly in background awaitng messages from the server
		try {
			miniServer = new ListenMiniServer(listenSocket, user);
			miniServerThread = new Thread(miniServer, "Client Mini Server");
			miniServerThread.start();
		} catch (Exception e) {
			System.out.println("Error initializing client, aborting...");
			e.printStackTrace();
			return;
		}

		/* MAIN MENU */
		do {
			mainMenu(user.getUnread());
			option = scanInt();
			switch (option) {
				case 1 -> {
					clear();
					System.out.println("You currently have chats with: \n");
					System.out.println(user.getActiveChatsString());
					System.out.println("Do you want to chat? [y/n]");
					if (scan.nextLine().equals("y"))
						chatView(selectChat(false, user));
				}
				case 2 -> {
					clear();
					System.out.println("With whom do you want to start a chat?\n");
					String guest = scan.nextLine();
					System.out.println(ClientMethods.newChat(user, guest, sendSocket, listenSocket, server));
				}
				case 3 -> {
					clear();
					System.out.println("You currently have groups with: \n");
					System.out.println(user.getActiveGroups());
					System.out.println("Do you want to chat? [y/n]");
					if (scan.nextLine().equals("y"))
						chatView(selectChat(true, user));
				}
				case 4 -> {
					clear();
					System.out.println("With whom do you want to start a group? \nMax 10 users \nPress 0 to finish\n");
					String[] guests = new String[9];
					String line = scan.nextLine();
					for (int i = 0; !line.equals("0") && i != 9; i++) {
						guests[i] = line;
						line = scan.nextLine();
					}
					System.out.println(ClientMethods.newGroup(user, guests, sendSocket, listenSocket, server));
				}
			}
		} while (option != 0);
		System.out.println("Goodbye");
	}

	public static void mainMenu(int unread) {
		clear();
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("You have " + unread + " unread messages\n");
		System.out.println("(1) List my private chats");
		System.out.println("(2) Create a new private chat");
		System.out.println("(3) List my group chats");
		System.out.println("(4) Create a new group chat");
		// YO PONDRIA ESTA OPCION DENTRO DE 1 Y 2
		//System.out.println("(r) Refresh");
		System.out.println("(0) Exit");
		System.out.println("-----------------------------------------------------------------------");
	}

	private static int welcomeMenu() {
		clear();
		System.out.println("Hello there");
		System.out.println("(1) Login");
		System.out.println("(2) Create account");
		System.out.println("(0) Cancel and exit");
		return scanInt();
	}

	private static Client loginMenu(DatagramSocket socket, DatagramSocket listenSocket, InetAddress server, boolean register) {
		Client user = null;
		String name, pass;
		clear();
		System.out.println("Please enter your account details");
		System.out.println("Username: ");
		name = scan.nextLine().replace("\0", "").trim();

		System.out.println("\nPassword: ");
		pass = scan.nextLine().replace("\0", "").trim();
		clear();

		try {
			if (register) AccountManager.sign_up(socket, listenSocket, server, name, pass);
			// Even if an user is creating an account, make sure the server registered it by performing a login
			// That way we can also get a valid connection "cookie"
			user = AccountManager.sign_in(socket, listenSocket, server, name, pass);
		} catch (IOException e) {
			System.out.println("Error connecting to the server");
		} catch (Exception e) {
			if (register)
				System.out.println("Error creating account, username already exists");
			else
				System.out.println("Error logging in, unknown username or wrong password");
		}
		return user;
	}

	private static Chat selectChat(boolean groups, Client user){
		clear();
		System.out.println("Who do you want to chat with?\n");

		if (groups)
			System.out.println("In the case of groups type all participant \nExample: participant1,participant2,participant3,... \n");
		String userToChat = scan.nextLine();
		return user.getActiveChats().get(userToChat);
	}

	/* TO-DO */
	public static void chatView(Chat chat){
		clear();

	}

	private static int scanInt() {
		int op = 0;
		try {
			op = scan.nextInt();
			scan.nextLine(); //clears buffer
		} catch (Exception ignored) {
		}
		return op;
	}

	private static void clear() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
}
