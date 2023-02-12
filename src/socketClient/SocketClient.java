package socketClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class SocketClient {

    public static void runClient(){
        {
            try {

                InetAddress myIp = InetAddress.getLocalHost();
                Socket socket = new Socket(myIp, 5005);
    
                Scanner scanner = new Scanner(System.in);
                DataOutputStream upStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream downStream = new DataInputStream(socket.getInputStream());
    
                System.out.println("¿Cómo te llamas?");
                String name = scanner.nextLine();
                upStream.writeUTF(name);
    
                while (true) {
                    System.out.println("Introduce el mensaje pal servidor:");
                    String message = scanner.nextLine();
                    upStream.writeUTF(message);
                    String serverMessage = downStream.readUTF();
                    if(serverMessage.length() != 0){
                        System.out.println();
                    }
    
                    if (message.equals("bye")) {
                        break;
                    }
                }
    
                upStream.close();
                scanner.close();
                socket.close();
    
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
