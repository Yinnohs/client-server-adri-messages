package socketServer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<ClientHandler> clients = new HashSet<>();
    private MessagesStorage messages = new MessagesStorage();

 
    public Server(int port) {
        this.port = port;
    }
 
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Servidor iniciado en localhost:"+ port);
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[LOG DEL SERVIDOR]: Nueva conexión en desde - " + socket.getInetAddress().toString());
 
                ClientHandler connection = new ClientHandler(socket, this, messages);
                clients.add(connection);
                connection.start();
 
            }
 
        } catch (IOException e) {
            System.err.println("[ERROR DEL SERVIDOR]: " + e.getMessage());
        }
    }
 

    protected void broadcast(String message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }
 

    protected void addUserName(String userName) {
        userNames.add(userName);
    }
 

    protected void removeUser(String userName, ClientHandler aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            clients.remove(aUser);
            System.out.println("[USUARIO DESCONECTADO]: " + userName + " se ha desconectado");
        }
    }
 
    protected Set<String> getUserNames() {
        return this.userNames;
    }
 
    protected boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public static void main(String[] args) {
        Server server = new Server(5005);
        server.execute();
    }
    
}
