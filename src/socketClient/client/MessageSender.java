package socketClient.client;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

// Hilo encargado de escribir los mensajes al servidor.
public class MessageSender extends Thread {
    // Instanciación de atributos.
    private PrintWriter writer; // Clase que se encarga de enviar mensajes al servidor.
 
    // Constructor.
    public MessageSender(Socket socket) {
        try {
            // Instanciación del stream de datos de salida al servidor.
            OutputStream output = socket.getOutputStream();
            // Instanciación de escritor del stream.
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            System.err.println("[ERROR AL CONSEGUIR EL OUTPUT]: " + e.getMessage());
        }
    }
    // Funcón de ejecución.
    public void run() {
        try {
            // Instanciación de una consola aparte para datos de entrada.
            Console console = System.console();
    
            // Petición de nombre de usuario.
            String userName = console.readLine("\nColoca tu nombre: ");
            // Seteo del nombre del usuario.
            // client.setUserName(userName);
            // Nombre del usuario enviado al servidor.
            writer.println(userName);
    
            // Variable que se encarga de enviar el mensaje al servidor.
            String text;
    
            // Envia al servidor el mensaje escrito por la consola del hilo.
            // Bucle do-while, revisa que la variable text no sea = "bye".
            do {
                text = console.readLine("#: ");
                writer.println(text);
            }while(!text.equals("bye"));

        } catch (Exception e) {
            System.out.println("[Error del Servidor]: " + e.getMessage());
        }
    }
}
