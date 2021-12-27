package Server;

import Common.Message;
import Common.MessageManager;
import Common.Parameters;
import Server.Methods.ReadFiles;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class OpenServer {
    public static void main(String[] args) {
        try {
            byte[] buffer;
            DatagramPacket packet;
            String data;
            Random rand = new Random();
            DatagramSocket socket = new DatagramSocket();
            DatagramSocket listenSocket = new DatagramSocket(Parameters.LISTEN_PORT);
            Message msg;
            InetAddress client;
            int msgNum = 0;

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
            HashMap<String, InetAddress> userToIp = new HashMap<String, InetAddress>();

            //CUANDO UN USUARIO PIDA CREAR UN CHAT PRUIVADO EL SERVIDOR PRIMERO TENDRA QUE COMPROBAR SI ESE USER EXISTE

            while (true) {
                msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
                System.out.println("[SERVER] IP\t" + msg.getAddress());
                System.out.println("[SERVER] PORT\t" + msg.getPort());
                System.out.println("[SERVER] LENGTH\t" + msg.getLength());
                System.out.println("[SERVER] DATA\t" + msg.getData());
                System.out.println("[SERVER] MSG TYPE\t" + msg.getId());

                // send an OK response to the server (temp)
                if (msg.getId() == MessageManager.LOGIN.val()) {
                    String[] dataMessage = msg.getData().split(Parameters.SEPARATOR);

                    if ((users != null) && (users.containsKey(dataMessage[0])) && (users.get(dataMessage[0]).equals(dataMessage[1]))){
                        userToIp.put(dataMessage[0], msg.getAddress());
                        String chats = "";
                        if (usersChats.get(dataMessage[0]) != null) {
                            for (String chat : usersChats.get(dataMessage[0])) {
                                chats = chats + chat;
                            }
                        } else
                            chats = "null";

                        MessageManager.LOGIN.sendMessage(msg.getAddress(), rand.nextInt() + Parameters.SEPARATOR + chats, socket);
                    } else {
                        MessageManager.ERROR.sendMessage(msg.getAddress(), "", socket);
                    }
                }

                if (msg.getId() == MessageManager.CREATE_USER.val()) {
                    String[] dataMessage = msg.getData().split(Parameters.SEPARATOR);

                    if ((users != null) && (!users.containsKey(dataMessage[0]))) {
                        userToIp.put(dataMessage[0], msg.getAddress());
                        users.put(dataMessage[0], dataMessage[1]);
                        MessageManager.CREATE_USER.sendMessage(msg.getAddress(), String.valueOf(rand.nextInt()), socket);
                    } else {
                        MessageManager.ERROR.sendMessage(msg.getAddress(), "", socket);
                    }
                }
                //if (msg.getId() == MessageManager.LIST_GROUP.val() )
                //if (msg.getId() == MessageManager.CREATE_GROUP.val() )
                //if (msg.getId() == MessageManager.DELETE_GROUP.val() )
                //if (msg.getId() == MessageManager.LIST_PRIVATE.val() )
                //if (msg.getId() == MessageManager.CREATE_PRIVATE.val() )
                //if (msg.getId() == MessageManager.DELETE_PRIVATE.val() )
                //if (msg.getId() == MessageManager.SEND_PRIVATE.val() )
                //if (msg.getId() == MessageManager.SEND_GROUP.val() )
                if (msg.getId() == MessageManager.REQUEST_MESSAGE.val())
                    MessageManager.REQUEST_MESSAGE.sendMessage(msg.getAddress(), "group" + Parameters.SEPARATOR + "ricardo milos" + Parameters.SEPARATOR + "super message num: " + String.valueOf(msgNum), socket);
                //if (msg.getId() == MessageManager.REQUEST_PRIVATE.val()) MessageManager.REQUEST_PRIVATE.sendMessage(msg.getAddress(), "NotMeNotYou\0ur mom\0ricardo milos\0sadpeepo", socket);
                //if (msg.getId() == MessageManager.RECEIVE.val() )
                //if (msg.getId() == MessageManager.ERROR.val() )

                msgNum++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
