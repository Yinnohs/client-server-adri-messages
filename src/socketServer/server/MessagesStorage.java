
package socketServer.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessagesStorage {

    private List<String> messages;
    public AtomicInteger length;

    public MessagesStorage(){
        this.messages = new ArrayList<>();
        this.length = new AtomicInteger(0);
    }

    public synchronized String  getAllMessages(){
        String result = "";
        if(this.messages.size() != 0){
            for(String element : messages){
                result += element + "\n";
            }
        }
        return result;
    }

    public synchronized void addMessage(String message){
        this.messages.add(message);
        this.length.addAndGet(1);
    }

}
