package socketServer;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketServer {
    private ServerSocket serverSocket;

    public void start(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            MessagesStorage messageStorage = new MessagesStorage();
            while (true)
                new ConnectionHandler(serverSocket.accept(), messageStorage).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
