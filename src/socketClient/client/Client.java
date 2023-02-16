package socketClient.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

// Creación de cada cliente
public class Client {
    // Atributos del cliente.
    private String hostname; // Dirección IP o dirección DNS del servidor.
    private int port; // Puerto externo del servidor.
    private String userName; // Nombre del usuario.
 
    // Constructor del cliente.
    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }
 
    // Función que ejecuta el funcionamiento principal del cliente.
    // Cliente es un hilo padre que genera dos hilos hijos, uno que escucha y otro que envia mensaje.
    public void execute() {
        try {
            // Instanciación del socket.
            Socket socket = new Socket(hostname, port);

            System.out.println("---------------------------------------------------------------");
            System.out.println("[MENSAJE]: Connectado al servidor en: " + hostname + ":" + port);
            System.out.println("---------------------------------------------------------------");

            // instanciación y ejecución del hilo hijo de lectura.
            new MessageReader(socket, this).start();
            // instanciación y ejecución del hilo hijo de escritura.
            new MessageSender(socket, this).start();
 
        } catch (UnknownHostException e) {
            System.out.println("[ERROR]: Servidor no encontrado -> " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[ERROR DE I/O]: " + e.getMessage());
        }
 
    }

    // Setter del usuario.
    void setUserName(String userName) {
        this.userName = userName;
    }
    // Getter del usuario.
    String getUserName() {
        return this.userName;
    }

    // Función de ejecución.
    public static void main(String[] args) {
        Client client = new Client("localhost", 5005);
        client.execute(); 
    }
}
