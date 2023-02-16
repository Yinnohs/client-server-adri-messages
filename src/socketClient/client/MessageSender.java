package socketClient.client;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private Client client;
 
    public MessageSender(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
 
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            System.err.println("[ERROR AL CONSEGUIR EL OUTPUT]: " + e.getMessage());
        }
    }
 
    public void run() {
        try {
            Console console = System.console();
    
            String userName = console.readLine("\nColoca tu nombre: ");
            client.setUserName(userName);
            writer.println(userName);
    
            String text;
    
            do {
                text = console.readLine("#: ");
                writer.println(text);
            }while(!text.equals("bye"));

        } catch (Exception e) {
 
            System.out.println("[Error del Servidor]: " + e.getMessage());
        }
    }
    
}
