package socketServer.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

// Creación del hilo encargado de gestionar la conexión con el cliente.
public class ClientHandler extends Thread {
    // Atributos.
    private Socket socket; // Conexión con el servidor. 
    private Server server; // hilo padre.
    private PrintWriter writer; // Clase que se encarga de enviar mensajes a los clientes.
    private MessagesStorage messages; // Almacenamiento de mensajes.
 
    // Constructor.
    public ClientHandler(Socket socket, Server server, MessagesStorage messages) {
        this.socket = socket;
        this.server = server;
        this.messages = messages;
    }
 
    // Función de ejecución.
    public void run() {
        try {
            // Instanciación del stream de datos de entrada.
            InputStream input = socket.getInputStream();
            // Instanciación del buffer que se encarga de leer los mensajes del cliente asociado.
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            // Instanciación del stream de datos de salida.
            OutputStream output = socket.getOutputStream();
            // Instanciación de escritor del stream hacia los clientes.
            writer = new PrintWriter(output, true);
 
            // Función printUsers.
            printUsers();
            // Función printMessages.
            printMessages();
 
            // Lector del nombre del usuario.
            String userName = reader.readLine();
            // Usuario añadido al servidor.
            server.addUserName(userName);
 
            // Mensaje al servidor de que un nuevo usuario se ha conectado.
            String serverMessage = "[MENSJAE DEL SISTEMA]: nuevo usuario conectado: " + userName;
            // Broadcast del nuevo usuario.
            server.broadcast(serverMessage, this);
 
            // Mensaje del cliente.
            String clientMsg;
            
            // Bucle do-while.
            do{
                // Variable que sirve para revisar si el mensaje empieza por "message:".
                String prevMessage = "";
                // Lee el mensaje del cliente y lo guarda en la variable.
                clientMsg = reader.readLine();

                // Condición en la que si el mensaje empieza por "message:" se guarda el resto de la cadena.
                if(clientMsg.startsWith("message:")){
                    int userMessageIndex = clientMsg.indexOf(":") + 1;
                    prevMessage =  clientMsg.substring(0, clientMsg.indexOf(":"));
                    clientMsg = clientMsg.substring(userMessageIndex, clientMsg.length());
                } else {
                    System.out.println(userName + " ha escrito un mensaje ERRONEO");
                    server.senderErrorMessage(this);
                }
                
                // Condición en la que si "prevMessage es = message" genera el mensaje a enviar.
                if(prevMessage.equals("message")){
                    Date currentDate = new Date();
                    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
                    String messageTime = formatTime.format(currentDate);
                    serverMessage = "["+messageTime+"]<"+ userName + ">: " + clientMsg;
                    System.out.println("[LOG DEL SISTEMA]"+serverMessage);
                    messages.addMessage(serverMessage);
                    server.broadcast(serverMessage, this);
                }

            }while(!clientMsg.equals("bye"));
            
            // Si el mensaje es bye, el servidor enviará a dicho cliente "goodbye".
            sendMessage("goodbye");
 
            // Borra al usuario de la lista de usuarios.
            server.removeUser(userName, this);
            // Genera un mensaje del servidor.
            serverMessage = "[MENSAJE DEL SERVIDOR]: el usuario: " + userName + " se ha desconectado.";
            // Se lo muestra a los demás usuarios que no se han desconectados.
            server.broadcast(serverMessage, this);
            // Cierra la conexión.
            socket.close();
 
        } catch (IOException e) {
            System.err.println(e);
            
        }
    }
    // Muestra los usuarios conectados.
    protected void printUsers() {
        if (server.hasUsers()) {
            writer.println("Usuario Conectados: " + server.getUserNames());
        } else {
            writer.println("No hay otros usuarios conectados");
        }
    }
    // Muestra los mensajes almacenados.
    protected void printMessages(){
        if(messages.length.get() != 0){
            String message = messages.getAllMessages();
            sendMessage(message);
        }
    }
 
    // Envia los mensajes al cliente conectado.
   protected void sendMessage(String message) {
        writer.println(message);
    }
    
}
