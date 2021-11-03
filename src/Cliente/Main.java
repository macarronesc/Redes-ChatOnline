package Cliente;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Client client;
        String[] user = getLogin();

        //client = new Client();

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
