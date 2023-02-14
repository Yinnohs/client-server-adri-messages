package socketClient.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private String hostname;
    private int port;
    private String userName;
 
    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }
 
    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("---------------------------------------------------------------");
            System.out.println("[MENSAJE]: Connectado al servidor en: " + hostname + ":" + port);
            System.out.println("---------------------------------------------------------------");
            new MessageReader(socket, this).start();
            new MessageSender(socket, this).start();
 
        } catch (UnknownHostException e) {
            System.out.println("[ERROR]: Servidor no encontrado -> " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[ERROR DE I/O]: " + e.getMessage());
        }
 
    }
 
    void setUserName(String userName) {
        this.userName = userName;
    }
 
    String getUserName() {
        return this.userName;
    }
}
