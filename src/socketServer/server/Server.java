package socketServer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

// Creación del server.
public class Server {
    // Instanciación de atributos.
    private int port; // asignación del puerto a usar.
    private Set<String> userNames = new HashSet<>(); // Creación de una colección de objetos no duplicados para los usuarios.
    private Set<ClientHandler> clients = new HashSet<>(); // Creación de una colección de objetos no duplicados para los hilos hijos.
    private MessagesStorage messages = new MessagesStorage(); // Instanciación de almacenado global de mensajes.

    // Constructor.
    public Server(int port) {
        this.port = port;
    }
 
    // Función de ejecución.
    public void execute() {
        // Intenta instanciar el servidor por el puerto asignado y si no lo consigue tira un error.
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Servidor iniciado en localhost:"+ port);
 
            // Bucle de instanciación y conexión de clientes.
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
 
    // Esta función se encarga de enviar los mensajes a todos los demás clientes que no sea quien lo envió.
    protected void broadcast(String message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }
 
    // Añade nuevos usuarios al servidor.
    protected void addUserName(String userName) {
        userNames.add(userName);
    }
 
    // Quita un usuario del servidor.
    protected void removeUser(String userName, ClientHandler aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            clients.remove(aUser);
            System.out.println("[USUARIO DESCONECTADO]: " + userName + " se ha desconectado");
        }
    }
 
    // Getter de los usuarios.
    protected Set<String> getUserNames() {
        return this.userNames;
    }
 
    // Variable para revisar si hay usuarios conectados.
    protected boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    // Función de ejecución.
    public static void main(String[] args) {
        Server server = new Server(5005);
        server.execute();
    }
    
}
