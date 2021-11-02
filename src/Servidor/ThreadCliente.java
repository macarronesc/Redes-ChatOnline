package Servidor;

import java.io.*;
import java.net.Socket;

public class ThreadCliente extends Thread {
    Socket s;
    BufferedReader isS;
    PrintWriter osS;

    public ThreadCliente(Socket s) throws IOException {
        this.s = s;
        isS = new BufferedReader(new InputStreamReader(s.getInputStream()));
        osS = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
    }

    @Override
    public void run() {

        System.out.println("New peticion");
        String msg;
        try {

            msg = isS.readLine();
            System.out.println(msg);

            osS.println(msg.toUpperCase());

            osS.close();
            isS.close();
            s.close();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
