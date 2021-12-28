package Server;

import Common.Message;
import Common.MessageManager;
import Common.Parameters;
import Server.Methods.ReadFiles;

import javax.sql.rowset.spi.SyncFactory;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class OpenServer {
    public static void main(String[] args) {
        try {
            Random rand = new Random();
            DatagramSocket socket = new DatagramSocket();
            DatagramSocket listenSocket = new DatagramSocket(Parameters.LISTEN_PORT);
            Message msg;
            int msgNum = 0;

            HashMap<String, String> users = ReadFiles.getDataUsers("users.txt");
            HashMap<String, String[]> usersChats = ReadFiles.getDataChats("chats.txt");
            HashMap<String, InetAddress> userToIp = new HashMap<String, InetAddress>();

            while (true) {
                msg = MessageManager.RECEIVE.receiveMessage(listenSocket);
                System.out.println("\n[SERVER] IP\t" + msg.getAddress());
                System.out.println("[SERVER] PORT\t" + msg.getPort());
                System.out.println("[SERVER] LENGTH\t" + msg.getLength());
                System.out.println("[SERVER] DATA\t" + msg.getData());
                System.out.println("[SERVER] MSG TYPE\t" + msg.getId());
                System.out.println("MESSAGE NUMBER: " +msgNum);

                // send an OK response to the server (temp
                if (msg.getId() == MessageManager.LOGIN.val()) {
                    String[] dataMessage = msg.getData().split(Parameters.SEPARATOR);
                    if ((users != null) && (users.containsKey(dataMessage[0])) && (users.get(dataMessage[0]).equals(dataMessage[1]))){
                        userToIp.put(dataMessage[0], msg.getAddress());
                        String chats = "";
                        if (usersChats.get(dataMessage[0]) != null) {
                            for (String chat : usersChats.get(dataMessage[0])) {
                                chats = chats + chat + ";";
                            }
                            chats = chats.substring(0, chats.length() - 1);
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
                if (msg.getId() == MessageManager.TEST.val()) {
                    MessageManager.TEST.sendMessage(msg.getAddress(), "", socket);
                }

                if (msg.getId() == MessageManager.EXIST_USER.val()) {
                    if ((users != null) && (users.containsKey(msg.getData()))){
                        MessageManager.EXIST_USER.sendMessage(msg.getAddress(), "true", socket);
                    } else {
                        MessageManager.ERROR.sendMessage(msg.getAddress(), "", socket);
                    }
                }

                if (msg.getId() == MessageManager.REQUEST_CHATS.val()) {
                    StringBuilder chats = new StringBuilder();
                    assert usersChats != null;
                    if (usersChats.get(msg.getData()) != null) {
                        for (String chat : usersChats.get(msg.getData())) {
                            chats.append(chat).append(";");
                        }
                        chats = new StringBuilder(chats.substring(0, chats.length() - 1));
                    } else
                        chats = new StringBuilder("null");
                    MessageManager.REQUEST_CHATS.sendMessage(msg.getAddress(), chats.toString(), socket);
                }

                if (msg.getId() == MessageManager.CREATE_PRIVATE.val()) {
                    String[] dataMessage = msg.getData().split(Parameters.SEPARATOR);
                    if ((users != null) && (users.containsKey(dataMessage[1]))) {
                        //USER
                        int lengtUser;
                        //Si el usuario todavia no tiene ningun chat se hace esto
                        if (usersChats.get(dataMessage[0]) == null)
                            lengtUser = 1;
                        else
                            lengtUser = usersChats.get(dataMessage[0]).length + 1;


                        String [] chatsUser = new String[lengtUser];
                        if (lengtUser != 1) {
                            for (int i = 0; i < lengtUser - 1; i++) {
                                chatsUser[i] = usersChats.get(dataMessage[0])[i];
                            }
                            chatsUser[chatsUser.length - 1] = "false," + dataMessage[1];
                        } else
                            chatsUser[0] = "false," + dataMessage[1];

                        usersChats.put(dataMessage[0], chatsUser);


                        //GUEST
                        int lenghGuest;
                        if (usersChats.get(dataMessage[1]) == null)
                            lenghGuest = 1;
                        else
                            lenghGuest = usersChats.get(dataMessage[1]).length + 1;

                        String [] chatsGuest = new String[lenghGuest];
                        if (lenghGuest != 1) {
                            for (int i = 0; i < lenghGuest - 1; i++) {
                                chatsGuest[i] = usersChats.get(dataMessage[1])[i];
                            }
                            chatsGuest[chatsGuest.length - 1] = "false," + dataMessage[0];
                        } else
                            chatsGuest[0] = "false," + dataMessage[0];

                        usersChats.put(dataMessage[1], chatsGuest);

                    } else {
                        MessageManager.ERROR.sendMessage(msg.getAddress(), "", socket);
                    }
                }

                if (msg.getId() == MessageManager.CREATE_GROUP.val()) {
                    //dataMessage = user // nameOfGroup // guests (splitet by ",")
                    String[] dataMessage = msg.getData().split(Parameters.SEPARATOR);
                    if ((users != null) && (users.containsKey(dataMessage[0]))) {
                        //USER
                        //Si el usuario todavia no tiene ningun chat se hace esto
                        int lengtUser;
                        if (usersChats.get(dataMessage[0]) == null)
                            lengtUser = 1;
                        else
                            lengtUser = usersChats.get(dataMessage[0]).length + 1;


                        String [] chatsUser = new String[lengtUser];
                        if (lengtUser != 1) {
                            for (int i = 0; i < lengtUser - 1; i++) {
                                chatsUser[i] = usersChats.get(dataMessage[0])[i];
                            }
                            chatsUser[chatsUser.length - 1] = "true," + dataMessage[1];
                        } else
                            chatsUser[0] = "true," + dataMessage[1];

                        usersChats.put(dataMessage[0], chatsUser);


                        //GUEST
                        for (String guest: dataMessage[2].split(",")) {
                            int lenghGuest;
                            if (usersChats.get(guest) == null)
                                lenghGuest = 1;
                            else
                                lenghGuest = usersChats.get(guest).length + 1;

                            String[] chatsGuest = new String[lenghGuest];
                            if (lenghGuest != 1) {
                                for (int i = 0; i < lenghGuest - 1; i++) {
                                    chatsGuest[i] = usersChats.get(guest)[i];
                                }
                                chatsGuest[chatsGuest.length - 1] = "true," + dataMessage[1];
                            } else
                                chatsGuest[0] = "true," + dataMessage[1];

                            usersChats.put(guest, chatsGuest);
                        }
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
                if (msg.getId() == MessageManager.SEND_PRIVATE.val() ){
                    msg.getData();
                    System.out.println(msg.getData());
                    MessageManager.SEND_PRIVATE.sendMessage(msg.getAddress(), msg.getData(), socket);
                }
                //if (msg.getId() == MessageManager.SEND_GROUP.val() )
                //if (msg.getId() == MessageManager.REQUEST_MESSAGE.val())
                    //MessageManager.REQUEST_MESSAGE.sendMessage(msg.getAddress(), "group" + Parameters.SEPARATOR + "ricardo milos" + Parameters.SEPARATOR + "super message num: " + String.valueOf(msgNum), socket);
                //if (msg.getId() == MessageManager.REQUEST_PRIVATE.val()) MessageManager.REQUEST_PRIVATE.sendMessage(msg.getAddress(), "NotMeNotYou\0ur mom\0ricardo milos\0sadpeepo", socket);
                //if (msg.getId() == MessageManager.RECEIVE.val() )
                //if (msg.getId() == MessageManager.ERROR.val() )

                msgNum++;
                if ((msgNum % 10) == 0){
                    assert usersChats != null;
                    System.out.println(ReadFiles.saveDataChats(usersChats, "chats.txt"));
                    assert users != null;
                    System.out.println(ReadFiles.saveDataUsers(users, "users.txt"));
                    System.out.println("Data saved!");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
