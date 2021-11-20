package Common;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public enum MessageManager {
    // 0x users
    LOGIN           (00),
    CREATE_USER     (01),
    // 1x groups
    LIST_GROUP      (10),
    CREATE_GROUP    (11),
    DELETE_GROUP    (12),
    // 2x private
    LIST_PRIVATE    (20),
    CREATE_PRIVATE  (21),
    DELETE_PRIVATE  (22),
    // 3x send
    SEND_PRIVATE    (30),
    SEND_GROUP      (31),
    // 4x request
    REQUEST_UNREAD  (40),
    REQUEST_PRIVATE (41),
    //9x others
    RECEIVE         (98),
    ERROR           (99);

    private int num;

    public int val(){return num;}

    MessageManager(int num) {
        this.num = num;
    }

    public boolean sendMessage(InetAddress receiver, String message,DatagramSocket socket) {
        InetAddress sender = null;
        DatagramPacket packet;
        boolean error = false;
        byte[] data;

        try {
            // Add the padded code with 1 zero before the message
            data = String.format("%02d%s", num, message).getBytes(StandardCharsets.UTF_8);

            //TODO partir mensaje si no cabe
            packet = new DatagramPacket(data, data.length, receiver, Parameters.LISTEN_PORT);
            socket.send(packet);
            sender = packet.getAddress();
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
        }

        return error;
    }

    public Message receiveMessage(DatagramSocket socket) {
        byte[] buffer;
        String data;
        Message msg;
        try {
            buffer = new byte[1000];
            DatagramPacket answer = new DatagramPacket(buffer, buffer.length);
            socket.receive(answer);
            data = new String(answer.getData());
            msg = new Message(answer, data);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
            msg = new Message(ERROR.val());
        }
        return msg;
    }
}
