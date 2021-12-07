package Server;

import Common.Chat;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileCSV {
    public static HashMap<String, String[]> getData(String fileName) {
        HashMap<String, String[]> data = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] lineAux = line.split("\\(");
                String[] chats = lineAux[1].substring(0,lineAux[1].length() - 2).split(",");
                data.put(lineAux[0], chats);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR. File not found!");
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public static void saveData(HashMap<String, String[]> data, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            for (Map.Entry<String, String[]> client : data.entrySet()) {
                writer.append(client.getKey()).append('(');
                for (String chat: client.getValue()) {
                    writer.append(chat).append(',');
                }
                writer.append(')').append(System.getProperty("line.separator"));
            }
            writer.close();
            System.out.println("DONE");
        } catch (IOException e) {
            System.out.println("ERROR. Could not save data!");
            e.printStackTrace();
        }
    }
}
