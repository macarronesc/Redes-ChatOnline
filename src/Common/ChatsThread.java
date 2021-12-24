package Common;
import java.lang.Thread;
import java.util.Scanner;

public class ChatsThread extends Thread{
    private Client user;
    private Chat chat;
    private static final Scanner scan = new Scanner(System.in);
    public ChatsThread(Client user, Chat chat){
        this.user = user;
        this.chat = chat;
    }

    public void run(){
        String msg = "";
        while(!msg.toLowerCase().equals("exit")){
            /*"Alex" habria que cambiarlo por user.getUsername*/
            System.out.print("Alex"+": ");
            msg = scan.nextLine();
            chat.addMessage("Alex",msg);
        }

        try {
            this.interrupt();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
