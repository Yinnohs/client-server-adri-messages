package socketClient.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReader extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private Client client;
 
    public MessageReader(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
 
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            System.err.println("[ERROR AL CONSEGUIR EL INPUT]:" + e.getMessage());
        }
    }
 
    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println("\n" + response);
                
                if (client.getUserName() != null) {
                    System.out.print("#:");
                }

                if(response == null){
                    System.exit(0);
                }

            } catch (IOException e) {
                System.err.println("[ERROR AL LEER DEL SERVIDOR]:" + e.getMessage());
                break;
            }
        }
    }
    
}
