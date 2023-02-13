package socketServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionHandler extends Thread {
    private Socket clientSocket;
    private MessagesStorage messageStorage; 


    public ConnectionHandler(Socket socket, MessagesStorage messageStorage ) {
        this.clientSocket = socket;
        this.messageStorage = messageStorage;
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

            DataOutputStream upStream = new DataOutputStream(this.clientSocket.getOutputStream());
            DataInputStream downStream = new DataInputStream(this.clientSocket.getInputStream());

            msg = "Conexión Realizada en la dirección: " + this.clientSocket.getInetAddress().toString();
            System.out.println("---------------------------");
            System.out.println(msg);
            System.out.println("---------------------------");
            String clientName = downStream.readUTF();

            if(this.messageStorage.length.get() != 0){
                String messages = this.messageStorage.getAllMessages();
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
                    messageStorage.addMessage(toClientMsg);
                    upStream.writeUTF(toClientMsg);
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
}