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

public class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private MessagesStorage messages;
 
    public ClientHandler(Socket socket, Server server, MessagesStorage messages) {
        this.socket = socket;
        this.server = server;
        this.messages = messages;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            printUsers();
            printMessages();
 
            String userName = reader.readLine();
            server.addUserName(userName);
 
            String serverMessage = "[MENSJAE DEL SISTEMA]: nuevo usuario conectado: " + userName;
            server.broadcast(serverMessage, this);
 
            String clientMsg;
            String prevMessage = "";

 
            do{
                clientMsg = reader.readLine();

                if(clientMsg.contains("message:")){
                    int  userMessageIndex = clientMsg.indexOf(":") + 1;
                    prevMessage =  clientMsg.substring(0, clientMsg.indexOf(":"));
                    clientMsg = clientMsg.substring( userMessageIndex, clientMsg.length());
                }
                
                Date currentDate = new Date();
                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
                String messageTime = formatTime.format(currentDate);

                if(prevMessage.equals("message")){
                    serverMessage = "["+messageTime+"]<"+ userName + ">: " + clientMsg;
                    System.out.println("[LOG DEL SISTEMA]"+serverMessage);
                    messages.addMessage(serverMessage);
                    server.broadcast(serverMessage, this);
                }

                prevMessage = "";

            }while(!clientMsg.equals("bye"));
            
            sendMessage("goodbye");
 
            server.removeUser(userName, this);
            
 
            serverMessage = "[MENSAJE DEL SERVIDOR]: el usuario: " + userName + " se ha desconectado.";

            server.broadcast(serverMessage, this);

            socket.close();
 
        } catch (IOException e) {
            System.err.println(e);
            
        }
    }
 
    protected void printUsers() {
        if (server.hasUsers()) {
            writer.println("Usuario Conectados: " + server.getUserNames());
        } else {
            writer.println("No hay otros usuarios conectados");
        }
    }

    protected void printMessages(){
        if(messages.length.get() != 0){
            String message = messages.getAllMessages();
            sendMessage(message);
        }
    }
 

   protected void sendMessage(String message) {
        writer.println(message);
    }
    
}
