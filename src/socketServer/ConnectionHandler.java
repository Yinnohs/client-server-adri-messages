package socketServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionHandler extends Thread {
    private Socket clientSocket;
    private SocketServer server;
    public DataOutputStream upStream;
    public DataInputStream downStream;

    public ConnectionHandler(Socket socket, SocketServer server ) {
        this.clientSocket = socket;
        this.server = server;
    }

    public void run() {
        try {
            String msg;
            String toClientMsg;
            String clientMsg;
            Date curDate;
            SimpleDateFormat formatTime;
            String messageTime;
            String prevMessage = "";

            upStream = new DataOutputStream(this.clientSocket.getOutputStream());
            downStream = new DataInputStream(this.clientSocket.getInputStream());

            msg = "Conexión Realizada en la dirección: " + this.clientSocket.getInetAddress().toString();
            System.out.println("---------------------------");
            System.out.println(msg);
            System.out.println("---------------------------");
            String clientName = downStream.readUTF();

            if(this.server.getStorageLegth() != 0){
                String messages = this.server.getAllMessages();
                upStream.writeUTF(messages);
            }else{
                upStream.writeUTF("");
            }

            while (true) {
                clientMsg = downStream.readUTF();

                if(clientMsg.contains("message")){
                    int  userMessageIndex = clientMsg.indexOf(":") + 1;
                    prevMessage =  clientMsg.substring(0, clientMsg.indexOf(":"));
                    clientMsg = clientMsg.substring( userMessageIndex, clientMsg.length());
                }
                
                curDate = new Date();
                formatTime = new SimpleDateFormat("HH:mm:ss");
                messageTime = formatTime.format(curDate);

                if(prevMessage.equals("message")){
                    toClientMsg = "["+messageTime+"]<"+ clientName + ">:" + clientMsg;
                    System.out.println("[MENSAJE DEL SISTEMA]"+toClientMsg);
                    server.storeMessage(toClientMsg);
                    server.broadcast(toClientMsg);
                }else{
                    upStream.writeUTF("");
                }
                prevMessage = "";

                if (clientMsg.equals("bye")) {
                    upStream.writeUTF("goodbye");
                    break;
                }
            }
            downStream.close();
            upStream.close();
            
            System.out.println("[MENSAJE DEL SERVIDOR]: " + clientName + " ha cerrado la conexión.");
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Se ha cerrado la conexión de un cliente.");
        }
    }

    public void sendMessage (String message){
        try {
            this.upStream.writeUTF(message);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}