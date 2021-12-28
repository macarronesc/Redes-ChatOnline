package Client;

import Client.Methods.AccountManager;
import Client.Methods.ClientMethods;
import Common.*;

import java.io.IOException;
import java.net.*;
import java.util.Objects;
import java.util.Scanner;

public class ClientMain {
	private static final Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws IOException {

		DatagramSocket sendSocket, listenSocket;
		InetAddress server;
		Client user = null;
		boolean error;
		int option;

		try {
			server = InetAddress.getByName(Parameters.SERVER_IP);
			sendSocket = new DatagramSocket();
			listenSocket = new DatagramSocket(Parameters.CLIENT_LISTEN_PORT);
			listenSocket.setSoTimeout(Parameters.TIMEOUT_DEFAULT);
			MessageManager.TEST.sendMessage(server, "", sendSocket);
			MessageManager.TEST.receiveMessage(listenSocket);

		} catch (SocketTimeoutException | UnknownHostException | SocketException e) {
			System.out.print("Server not found. Bye :)");
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


		/* MAIN MENU */
		do {
			mainMenu(user.getUnread());
			option = scanInt();
			switch (option) {
				case 1 -> {
					clear();
					System.out.println("You currently have chats with: \n");
					String aux = user.getActiveChatsString();
					//show active chats and ask if you want to chat
					if (!Objects.equals(aux, "")) {
						System.out.println(aux + "\nDo you want to chat? [y/n]");
						String stringScan = scan.nextLine();
						while ((!stringScan.equalsIgnoreCase("y")) && (!stringScan.equalsIgnoreCase("n"))) {
							System.out.println("\nPress [y/n] please.\nDo you want to chat? [y/n]");
							stringScan = scan.nextLine();
						}
						if (stringScan.equals("y"))
							chatView(sendSocket, listenSocket, server,user, selectChat(user));
					}
				}
				case 2 -> {
					//Create a new private chat
					clear();
					System.out.println("With whom do you want to start a chat?\n");
					String guest = scan.nextLine();
					System.out.println(ClientMethods.newChat(user, guest, sendSocket, listenSocket, server));
				}
				case 3 -> {
					//Show acive groups and ask to chat
					clear();
					System.out.println("You currently have groups with: \n");
					String aux = user.getActiveGroups();
					if (!Objects.equals(aux, "")) {
						System.out.println(aux + "\nDo you want to chat? [y/n]");
						String stringScan = scan.nextLine();
						while ((!stringScan.equalsIgnoreCase("y")) && (!stringScan.equalsIgnoreCase("n"))) {
							if (stringScan.equals("y")) {
								chatView(sendSocket, listenSocket, server, user, selectChat(user));
							}
							System.out.println("\nPress [y/n] please.\nDo you want to chat? [y/n]");
							stringScan = scan.nextLine();
						}
					}
				}
				case 4 -> {
					//Create a new group
					clear();
					System.out.println("With whom do you want to start a group? \nMax 10 users \nPress 0 to finish\n");
					String guests = "";
					String line = scan.nextLine();
					for (int i = 0; !line.equals("0") && i != 9; i++) {
						guests = guests.concat(line).concat(",");
						line = scan.nextLine();
					}
					System.out.println("What's the name of the group?");
					String name = scan.nextLine();
					System.out.println(ClientMethods.newGroup(user, guests, name, sendSocket, listenSocket, server));
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

	public static void refreshChats(Client user, DatagramSocket socket, DatagramSocket listenSocket, InetAddress server) throws IOException {
		MessageManager.REQUEST_CHATS.sendMessage(server, user.getUsername(), socket);
		Message msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
		user.setActiveChats(AccountManager.putChats(msg.getData()));
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
		boolean succesSignUp = false;
		try {
			if (register)
				succesSignUp = AccountManager.sign_up(socket, listenSocket, server, name, pass);
			// Even if an user  is creating an account, make sure the server registered it by performing a login
			// That way we can also get a valid connection "cookie"

			user = AccountManager.sign_in(socket, listenSocket, server, name, pass);

		} catch (IOException e) {
			System.out.println("Error connecting to the server");
		} catch (Exception e) {
			if (!succesSignUp && register)
				System.out.println("Error creating account, username already exists");
			else
				System.out.println("Error logging in, unknown username or wrong password");
		}
		return user;
	}

	private static Chat selectChat(Client user){
		clear();
		System.out.println("Who do you want to chat with?\n");
		String userToChat = scan.nextLine();
		return user.getActiveChats().get(userToChat);
	}

	public static void chatView(DatagramSocket socket, DatagramSocket listenSocket, InetAddress server, Client user,Chat chat) {
		clear();
		System.out.println("Type 'exit' to close the chat");

		//Shows the chat and create diferents threads, one for the local chat and other for the server messages
		for (ChatMessage a: chat.getMessages()) {
			System.out.println(a.getUser()+": "+a.getMessage());
		}
		chat.markRead();
		ChatsThread local = new ChatsThread(socket, listenSocket, server, user,chat, true);
		local.start();

		if(!chat.isGroup()){
			ChatsThread threadserver = new ChatsThread(socket, listenSocket, server, null, chat, false);
			threadserver.start();
			//Lock the chat until typeing exit
			while(local.isAlive()){
			}
			threadserver.interrupt();
			local.interrupt();
		}else{
			ChatsThread[] threadserver = new ChatsThread[10];
			for (ChatsThread a: threadserver) {
				a = new ChatsThread(socket, listenSocket,server,null,chat,false);
			}

			for(ChatsThread a: threadserver){
				a.start();
			}
			//Lock the chat until typeing exit
			while(local.isAlive()){
			}

			for (ChatsThread a: threadserver){
				a.interrupt();
			}
			local.interrupt();
		}


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
