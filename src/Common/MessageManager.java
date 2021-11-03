package Common;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MessageManager {

    static enum MSG_TYPE {
        LOGIN,
        SEND_PRIVATE,
        SEND_GROUP,
        LIST_GROUP,
        CREATE_GROUP,
        DELETE_GROUP,
        CREATE_USER,

    }

    public static boolean sendMessage(String ipFrom, String ipTo, int port, String message, int type, DatagramSocket socket) {
        DatagramPacket packet;
        boolean error = false;
        byte[] data;

        try {
            // Pad with 1 zero (for now)
            data = String.format("%02d%s", type, message).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            error = true;
        }

        //todo
        return error;
    }
}
