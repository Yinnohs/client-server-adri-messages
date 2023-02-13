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

                String chatMessages = downStream.readUTF();

                if(chatMessages.length() != 0){
                    System.out.println(chatMessages);
                }
                System.out.println("Bienvenido al chat de texto");
                while (true) {
                    String message = "";
                    String serverMessage ="";
                    System.out.print("#: ");

                    message = scanner.nextLine();
                    
                    //sends message to the server
                    upStream.writeUTF(message);

                    if (message.equals("bye")) {
                        System.out.println("goodbye");
                        break;
                    }

                    if(serverMessage.length() != 0){
                        System.out.println(serverMessage);
                    }
                }
                scanner.close();
                socket.close();
                upStream.close();
            } catch (IOException e) {
                System.out.println();
            }
        }

    }
}
