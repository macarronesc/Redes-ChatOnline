package Server;

import Common.Chat;
import Common.Client;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileCSV {
    public static HashMap<String, String[]> getDataChats(String fileName) {
        HashMap<String, String[]> data = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] lineAux = line.split("\\(");
                String[] chats = lineAux[1].substring(0,lineAux[1].length() - 2).split(";");
                data.put(lineAux[0], chats);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR. File not found!");
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public static void saveDataChats(HashMap<String, String> data, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            for (String user : data.keySet()){
                writer.append(user).append('(').append(data.get(user));
                writer.append(')').append(System.getProperty("line.separator"));
            }
            writer.close();
            System.out.println("DONE");
        } catch (IOException e) {
            System.out.println("ERROR. Could not save data!");
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> clientsToMap(Client[] users){
        HashMap<String, String> map = new HashMap<>();
        for (Client user : users){
            Map<String, Chat> chats = user.getActiveChats();
            String aux = "";
            for (String chat : chats.keySet()){
                aux = aux.concat(String.valueOf(chats.get(chat).isGroup())).concat(",");
                aux = aux.concat(chat).concat(";");
            }
            map.put(user.getUsername(),aux);
        }
        return map;
    }

    public static HashMap<String, String> getDataUsers(String fileName) {
        HashMap<String, String> data = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while(scanner.hasNextLine()){
                String[] users = scanner.nextLine().split(",");
                data.put(users[0], users[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR. File not found!");
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public static void saveDataUsers(HashMap<String, String> data, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.append(entry.getKey())
                        .append(',')
                        .append(entry.getValue())
                        .append(System.getProperty("line.separator"));
            }
            writer.close();
            System.out.println("DONE");
        } catch (IOException e) {
            System.out.println("ERROR. Could not save data!");
            e.printStackTrace();
        }
    }
}
