package socketServer;

import socketServer.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(5005);
        server.execute();
    }
}
