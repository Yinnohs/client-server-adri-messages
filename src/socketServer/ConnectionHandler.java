package socketServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionHandler extends Thread {
    private Socket clientSocket;


    public ConnectionHandler(Socket socket) {
        this.clientSocket = socket;
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

            while (true) {
                clientMsg = downStream.readUTF();

                if(clientMsg.contains("message")){
                    prevMessage =  clientMsg.substring(0, clientMsg.indexOf(":"));
                }
                
                curDate = new Date();
                formatTime = new SimpleDateFormat("HH:mm:ss");
                messageTime = formatTime.format(curDate);

                if(prevMessage.equals("message")){
                    toClientMsg = "[MENSAJE DEL SISTEMA]["+messageTime+"] "+ clientName + ": " + clientMsg;
                    System.out.println(toClientMsg);
                    upStream.writeUTF(toClientMsg);
                }else{
                    upStream.writeUTF("");
                }
                prevMessage = "";

                if (clientMsg.equals("bye")) {
                    break;
                }
            }
            downStream.close();
            System.out.println("[MENSAJE DEL SERVIDOR]: " + clientName + " ha cerrado la conexión.");
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Se ha cerrado la conexión de un cliente.");
        }
    }
}