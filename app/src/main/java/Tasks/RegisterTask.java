package Tasks;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.ServerProxy;

import Model.EventModel;
import Model.PersonModel;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.PersonResponse;
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
        if(response == null || response.getMessage() != null){
            sendMessage("Error:processing register");
        } else {
            DataCache data = DataCache.getInstance();
            data.setLoginAuthtoken(response.getAuthtoken());
            PersonResponse personResponse = serverProxy.getPeople(response.getAuthtoken(), serverHost, serverPort);
            PersonModel[] personLists = personResponse.getData();
            data.setPeople(personLists);
            EventResponse eventResponse = serverProxy.getEvents(response.getAuthtoken(), serverHost, serverPort);
            EventModel[] eventLists = eventResponse.getData();
            data.setEvents(eventLists);
            PersonModel userPerson = data.getPeople().get(response.getPersonID());
            String firstName = userPerson.getFirstName();
            String lastName = userPerson.getLastName();


            sendMessage("Register was successful for " + firstName + " " + lastName);
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
