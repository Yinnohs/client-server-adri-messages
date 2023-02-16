package socketClient.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

// Hilo encargado de escuchar los mensajes del servidor.
public class MessageReader extends Thread {
    // Instanciación de atributos.
    private BufferedReader reader; // Buffer de lectura.
    private Socket socket; // Conexión actual con el servidor.
    private Client client; // Cliente (Hilo padre).
 
    // Constructor.
    public MessageReader(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
 
        try {
            // Instanciación del stream de datos de entrada.
            InputStream input = socket.getInputStream();
            // Instanciación del buffer de lectura, pasando como variable el stream.
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            System.err.println("[ERROR AL CONSEGUIR EL INPUT]:" + e.getMessage());
        }
    }
 
    // Función de ejecución.
    public void run() {
        try {
                // Bucle de ejecución infinita.
                while (true) {
                    // Lectura de mensajes que llegan del servidor.
                    String response = reader.readLine();

                    // Muestra el mensaje.
                    System.out.println("\n" + response);
                    
                    // Condición de cerrado de bucle.
                    if(response.equals("goodbye")){
                        break;
                    }
                    
                    // Señalizador de escritura.
                    System.out.print("#:");
                    
                }
                // Cerrado de la conexión.
                socket.close();
            } catch (IOException e) {
                System.err.println("[ERROR AL LEER DEL SERVIDOR]:" + e.getMessage());
            }
        
    }
    
}
