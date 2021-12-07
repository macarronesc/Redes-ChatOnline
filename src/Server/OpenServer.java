package Server;

import Common.Parameters;
import Server.Methods.ReadFiles;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OpenServer {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(Parameters.LISTEN_PORT);
            byte[] buffer;
            DatagramPacket packet;
            String data;

            /*String aaa = "false,culo;true,aaaa;false,hola";
            HashMap<String, String> map = new HashMap<>();
            map.put("dani",aaa);
            map.put("alfredo",aaa);
            map.put("nico",aaa);
            ReadFiles.saveDataChats(map,"chats.txt");
            System.out.println(map.get("dani"));

            HashMap<String, String[]> map2 = new HashMap<>();
            map2 = ReadFiles.getDataChats("chats.txt");

            HashMap<String, String> map3 = new HashMap<>();
            map3.put("dani", "hola");
            map3.put("dani2", "hola");
            map3.put("axel", "hola");
            ReadFiles.saveDataUsers(map3, "users.txt");

            HashMap<String, String> map4 = new HashMap<>();
            map4 = ReadFiles.getDataUsers("users.txt");
            for (String key : map4.keySet())
                System.out.println(key + " " + map4.get(key));

            System.out.println( Arrays.toString(map2.get("nico")));*/

            HashMap<String, String> users = ReadFiles.getDataUsers("users.txt");
            HashMap<String, String[]> usersChats = ReadFiles.getDataChats("chats.txt");
            HashMap<String, InetAddress> userToIp;

            //CUANDO UN USUARIO PIDA CREAR UN CHAT PRUIVADO EL SERVIDOR PRIMERO TENDRA QUE COMPROBAR SI ESE USER EXISTE

            while (true) {
                buffer = new byte[Parameters.MAX_BUFFER_SIZE];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                data = new String(packet.getData()); //String from bytes
                System.out.println("[SERVER] IP\t" + packet.getAddress());
                System.out.println("[SERVER] PORT\t" + packet.getPort());
                System.out.println("[SERVER] LENGTH\t" + packet.getLength());
                System.out.println("[SERVER] DATA\t" + data);
                System.out.println("[SERVER] MSG TYPE\t" + data.substring(0, 2));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
