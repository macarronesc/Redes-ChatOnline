package Common;
import java.io.IOException;
import java.lang.Thread;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ChatsThread extends Thread{
    private Client user;
    private String guest;
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
     * @param guest indicates the user to send the message
     * @return Error or messages from server
     */
    public ChatsThread(DatagramSocket socket, DatagramSocket listenSocket, InetAddress serverClient, Client user, Chat chat, boolean local, String guest){
        this.user = user;
        this.chat = chat;
        this.send = socket;
        this.listen = listenSocket;
        this.server = serverClient;
        this.local = local;
        this.guest = guest;
    }

    public void run(){
        String message = "";
        if(local){
            while(!message.equalsIgnoreCase("exit")){
                System.out.print(user.getUsername()+": ");
                message = scan.nextLine();
                if (!message.equalsIgnoreCase("exit")){
                    chat.addMessage(user.getUsername(),message);
                    chat.markRead();
                    try {
                        // THE MESSAGE FORMAT IS: MESSAGE / GUEST (NAME OF GROUP OR PERSON) / IS GROUP
                        MessageManager.SEND_PRIVATE.sendMessage(server, message + Parameters.SEPARATOR + guest + Parameters.SEPARATOR + chat.isGroup(), send);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                this.interrupt();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }else{
            try {
                Message messageReceive = MessageManager.RECEIVE.receiveMessage(listen);
                // THE MESSAGE FORMAT IS: MESSAGE / NAME OF THE GROUP OR PERSON TO SEND / IS GROUP / NAME OF THE SENDER
                String[] data = messageReceive.getData().split(Parameters.SEPARATOR);
                if ((data[1].equals(guest)) && (data[2].equals(String.valueOf(chat.isGroup())))){
                    System.out.println(data[3] + ": " + data[0]);
                    chat.addMessage(data[3], data[0]);
                }
            } catch (IOException ignored) {
            }
        }
    }
}
