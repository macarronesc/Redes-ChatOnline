package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class OpenServer {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
            while (true) {
                Socket sc = ss.accept();
                new ThreadCliente(sc).start();
            }

        } catch (IOException e) {
            System.out.println("No puede escuchar en el puerto: " + Integer.parseInt(args[0]));
            System.exit(-1);
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
