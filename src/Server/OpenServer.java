package Server;

import Common.Parameters;
import Server.FileCSV;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.HashMap;

public class OpenServer {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(Parameters.LISTEN_PORT);
            byte[] buffer;
            DatagramPacket packet;
            String data;

            String[] aaa = new String[3];
            aaa[0] = "culo";
            aaa[1] = "AAA";
            aaa[2] = "AAA";
            HashMap<String, String[]> map = new HashMap<>();
            map.put("dani",aaa);
            map.put("alfredo",aaa);
            map.put("nico",aaa);
            FileCSV.saveData(map,"users.txt");
            System.out.println( Arrays.toString(map.get("dani")));

            HashMap<String, String[]> map2 = new HashMap<>();
            map2 = FileCSV.getData("users.txt");


            // CUANDO EL SERVIDOR ENVIE LOS DATOS DEL LOGIN TENDRA QUE ENVIAR "cookies/true,chat1;false,chat2;true,1chat3;..."
            System.out.println( Arrays.toString(map2.get("nico")));



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
