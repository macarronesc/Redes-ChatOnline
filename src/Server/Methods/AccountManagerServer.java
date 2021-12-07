package Server.Methods;

import java.util.HashMap;

public class AccountManagerServer {
    // CHECK IF THE USER EXIST IN THE DATABASE
    public boolean checkUser(HashMap<String, String> users, String user) {
        return users.containsKey(user);
    }

    // CHECK IF THE PASSWORD IS THE SAME
    public boolean checkPassword(HashMap<String, String> users, String user, String password) {
        return (checkUser(users, user)) && (users.get(user).equals(password));
    }

    // CHECK IF THE LOGIN IS OK
    public boolean checkLogIn (HashMap<String, String> users, String user, String password){
        return (checkUser(users, user) && checkPassword(users, user, password));
    }

    // ADD NEW USER TO THE LIST OF USERS
    public HashMap<String, String> newUser(String user, String password, HashMap<String, String> users) {
        users.put(user,password);
        return users;
    }
}
