package Tasks;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.ServerProxy;

import Model.*;
import Model.PersonModel;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

public class LoginTask implements Runnable{
    private LoginRequest request;
    private Handler messageHandler;
    private String serverHost;
    private String serverPort;

    public LoginTask(Handler messageHandler, LoginRequest request, String serverHost, String serverPort){
        this.request = request;
        this.messageHandler = messageHandler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }
    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy();
        LoginResponse loginResponse = serverProxy.login(request,serverHost,serverPort);
        if(loginResponse == null || loginResponse.getMessage() != null){
            sendMessage("Error:processing longin");
        } else {

            DataCache data = DataCache.getInstance();
            data.setLoginAuthtoken(loginResponse.getAuthtoken());
            PersonResponse personResponse = serverProxy.getPeople(loginResponse.getAuthtoken(), serverHost, serverPort);
            PersonModel[] personLists = personResponse.getData();
            data.setPeople(personLists);
            EventResponse eventResponse = serverProxy.getEvents(loginResponse.getAuthtoken(), serverHost, serverPort);
            EventModel[] eventLists = eventResponse.getData();
            data.setEvents(eventLists);
            PersonModel userPerson = data.getPeople().get(loginResponse.getPersonID());
            String firstName = userPerson.getFirstName();
            String lastName = userPerson.getLastName();

            //This is how you will get the specific name of the user

            sendMessage("login was successful correctly for " + firstName + " " + lastName);
        }
    }

    private void sendMessage(String message) {
        Message respondMessage = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putString("loginKey", message);
        respondMessage.setData(messageBundle);

        messageHandler.sendMessage(respondMessage);
    }
}
