package socketClient;

import socketClient.client.Client;

public class Main {
    public static void main(String[] args) {
       Client client = new Client("localhost", 5005);
       client.execute(); 
    }
}
