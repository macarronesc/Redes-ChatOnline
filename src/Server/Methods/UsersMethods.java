package Server.Methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class UsersMethods {

    // CHECK IF THE USER EXIST IN THE DATABASE
    public boolean checkUser(HashMap<String, String> data, String user) {
        return data.containsKey(user);
    }

    // CHECK IF THE PASSWORD IS THE SAME
    public boolean checkPassword(HashMap<String, String> data, String user, String password) {
        return (checkUser(data, user)) && (data.get(user).equals(password));
    }


    public boolean newUser(String user, String password, String fileName) {
        try {
            FileWriter write = new FileWriter(new File("fileName"));
            String frame = user + "," + password;
            write.append(frame);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("ERROR. File not found!");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

