package socketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    private ServerSocket serverSocket;
    private List<String> messageStorage;
    private List<ConnectionHandler> clientList;

    public void start(int port) {
        try {
            this.clientList = new ArrayList<>();
            this.messageStorage = new ArrayList<>();
            this.serverSocket = new ServerSocket(port);
            while (true){
                ConnectionHandler client =  new ConnectionHandler(serverSocket.accept(), this);
                clientList.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  synchronized  void storeMessage(String message){
        this.messageStorage.add(message);
    }

    public synchronized int getStorageLegth(){
        return this.messageStorage.size();
    }

    public synchronized String  getAllMessages(){
        String result = "";
        if(this.messageStorage.size() != 0){
            for(String element : messageStorage){
                result += element + "\n";
            }
        }
        return result;
    }

    public  synchronized void broadcast (String message){
        for (ConnectionHandler client : clientList){
            client.sendMessage(message);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
