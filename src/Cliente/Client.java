package Cliente;

import Servidor.ThreadCliente;

import java.util.List;

public class Client {
    private String userName;
    private ThreadCliente thread;
    private List<String> userServers;

    public Client(String userName, ThreadCliente thread, List<String> userServers) {
        this.userName = userName;
        this.thread = thread;
        this.userServers = userServers;
    }

    public String getUserName() {
        return userName;
    }

    public ThreadCliente getThread() {
        return thread;
    }

    public List<String> getUserServers() {
        return userServers;
    }

    public boolean sendMessage(String message){
        boolean error = false;
        //todo
        return error;
    }




}
