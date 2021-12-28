package Common;
import java.io.IOException;
import java.lang.Thread;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ChatsThread extends Thread{
    private Client user;
    private Chat chat;
    private DatagramSocket send,listen;
    private InetAddress server;
    private static final Scanner scan = new Scanner(System.in);
    public ChatsThread(DatagramSocket socket, DatagramSocket listenSocket, InetAddress serverClient, Client user, Chat chat){
        this.user = user;
        this.chat = chat;
        this.send = socket;
        this.listen = listenSocket;
        this.server = serverClient;
    }

    public void run(){
        String msg = "";
        while(!msg.toLowerCase().equals("exit")){
            System.out.print(user.getUsername()+": ");
            msg = scan.nextLine();
            chat.addMessage(user.getUsername(),msg);
            chat.markRead();
            try {
                MessageManager.SEND_PRIVATE.sendMessage(server, msg, send);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.interrupt();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
