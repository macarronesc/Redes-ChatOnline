package Servidor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileCSV {
    public HashMap<String, String> getData(String fileName) {
        HashMap<String, String> data = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            String[] users = scanner.nextLine().split(",");
            while(scanner.hasNextLine()){
                data.put(users[0], users[1]);
                users = scanner.nextLine().split(",");
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR. File not found!");
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public void saveData(HashMap<String, String> data, String fileName) {
        try (Writer writer = new FileWriter("fileName")) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.append(entry.getKey())
                        .append(',')
                        .append(entry.getValue())
                        .append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            System.out.println("ERROR. Could not save data!");
            e.printStackTrace();
        }
    }
}
