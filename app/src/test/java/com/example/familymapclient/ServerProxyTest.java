package com.example.familymapclient;

//import static junit.framework.TestCase.*;

import static org.junit.Assert.*;

import com.example.familymapclient.ServerProxy;
//import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;


import Model.EventModel;
import Model.PersonModel;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

public class ServerProxyTest {
    private ServerProxy proxyTest;
    private String serverHost;

    private String serverPort;
    private RegisterRequest request;
    private RegisterResponse responseFirst;
    @Before
    public void setUP(){
        proxyTest = new ServerProxy();
        serverHost = "192.168.254.162";
        serverPort = "8080";

        request = new RegisterRequest(
                "derek","derek","email","derek","quinteros","MALE"
        );
        responseFirst = proxyTest.register(request,serverHost,serverPort);
    }

    @After
    public void tearDown(){
        proxyTest = null;
        serverHost = null;
        serverPort = null;
        request = null;
    }
    @Test
    public void testRegister(){
        RegisterRequest requestRegister = new RegisterRequest(
                "maria","isabel","email","maria","espinoza","FEMALE"
        );
        RegisterResponse response = proxyTest.register(requestRegister,serverHost,serverPort);

        String compareOne = requestRegister.getUsername();
        String compareTwo = response.getUsername();

        assertNotNull(compareOne);
        assertNotNull(compareTwo);
        assertEquals(compareOne, compareTwo);
    }

    @Test
    public void testRegisterFail(){
        RegisterResponse response = proxyTest.register(request,serverHost,serverPort);
        RegisterResponse failResponse = proxyTest.register(request,serverHost,serverPort);

        String compareOne = failResponse.getSuccess().toString();

        assertNotNull(compareOne);
        assertEquals("false",compareOne);
    }

    @Test
    public void testLogin(){
        LoginRequest request = new LoginRequest(
                "derek","derek"
        );
        LoginResponse response = proxyTest.login(request,serverHost,serverPort);

        String compareOne = request.getUsername();
        String compareTwo = response.getUsername();

        assertNotNull(compareOne);
        assertNotNull(compareTwo);
        assertEquals(compareOne, compareTwo);
    }

    @Test
    public void testLoginFail(){
        LoginRequest request = new LoginRequest(
                "israelque","espinozasnose"
        );
        LoginResponse failResponse = proxyTest.login(request,serverHost,serverPort);

        String compareOne = failResponse.getSuccess().toString();

        assertNotNull(compareOne);
        assertEquals(false,failResponse.getSuccess());
    }

    @Test
    public void testRetrievePeople(){
        LoginRequest request = new LoginRequest(
                "derek","derek"
        );
        LoginResponse response = proxyTest.login(request,serverHost,serverPort);
        PersonResponse gettingFamily = proxyTest.getPeople(response.getAuthtoken(),serverHost,serverPort);
        PersonModel[] familyData = gettingFamily.getData();

        assertNotNull(gettingFamily);
        assertNotNull(familyData);
        assertEquals(true,response.getSuccess() );
    }

    @Test
    public void testRetrievePeopleFail(){
        LoginRequest request = new LoginRequest(
                "derek","derek"
        );
        LoginResponse response = proxyTest.login(request,serverHost,serverPort);
        PersonResponse gettingFamily = proxyTest.getPeople("alskdhfouhasdufhoasasfherq",serverHost,serverPort);


        assertNotNull(gettingFamily);
        assertEquals(false,gettingFamily.getSuccess());
    }

    @Test
    public void testRetrieveEvents(){
        LoginRequest request = new LoginRequest(
                "derek","derek"
        );
        LoginResponse response = proxyTest.login(request,serverHost,serverPort);
        EventResponse gettingEvents = proxyTest.getEvents(response.getAuthtoken(),serverHost,serverPort);
        EventModel[] familyData = gettingEvents.getData();


        assertNotNull(gettingEvents);
        assertNotNull(familyData);
        assertEquals(true,response.getSuccess() );
    }

    @Test
    public void testRetrieveEventsFail(){
        LoginRequest request = new LoginRequest(
                "derek","derek"
        );
        LoginResponse response = proxyTest.login(request,serverHost,serverPort);
        EventResponse gettingEvents = proxyTest.getEvents("alskdhfouhasdufhoasasfherq",serverHost,serverPort);

        assertNotNull(gettingEvents);
        assertEquals(false, gettingEvents.getSuccess());
    }

}
