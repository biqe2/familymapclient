package Tasks;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.ServerProxy;

import Requests.RegisterRequest;
import Responses.RegisterResponse;

public class RegisterTask implements Runnable{
    private RegisterRequest request;
    private Handler messageHandler;
    private String serverHost;
    private String serverPort;

    public RegisterTask(Handler messageHandler, RegisterRequest request, String serverHost,String serverPort){
        this.request = request;
        this.messageHandler = messageHandler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }
    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy();
        RegisterResponse response = serverProxy.register(request,serverHost,serverPort);
        //maybe I can use the resquest to create a PersonModel and add it to the datacache.
        if(response == null){
            sendMessage("Error processing register");
        } else {
            sendMessage(response.getUsername() + "was registered correctly");
        }
    }

    private void sendMessage(String message) {
        Message respondMessage = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putString("registerKey", message);
        respondMessage.setData(messageBundle);

        messageHandler.sendMessage(respondMessage);
    }
}
