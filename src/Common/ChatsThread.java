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
    private boolean local;


    /**
     * Create threads for the client and the server
     *
     * @param socket server socket
     * @param listenSocket takes server messages
     * @param serverClient server
     * @param user user, client
     * @param chat the chat we are using
     * @param local indicates if we are client or server
     * @return Error or messages from server
     */
    public ChatsThread(DatagramSocket socket, DatagramSocket listenSocket, InetAddress serverClient, Client user, Chat chat, boolean local){
        this.user = user;
        this.chat = chat;
        this.send = socket;
        this.listen = listenSocket;
        this.server = serverClient;
        this.local = local;
    }

    public void run(){
        String msg = "";
        if(local){
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
        }else{
            try {
                Message mesg = null;
                mesg = MessageManager.RECEIVE.receiveMessage(listen);
                System.out.println(mesg.getData());
            } catch (IOException e) {
            }
        }

    }
}
