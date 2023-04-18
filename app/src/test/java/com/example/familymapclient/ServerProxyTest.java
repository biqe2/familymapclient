package com.example.familymapclient;

//import static junit.framework.TestCase.*;

import static org.junit.Assert.*;

import com.example.familymapclient.ServerProxy;
//import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;


import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.LoginResponse;
import Responses.RegisterResponse;

public class ServerProxyTest {
    private ServerProxy proxyTest;
    private String serverHost;

    private String serverPort;
    @Before
    public void setUP(){
        proxyTest = new ServerProxy();
        serverHost = "192.168.253.73";
        serverPort = "8080";
    }

    @After
    public void tearDown(){
        proxyTest = null;
        serverHost = null;
        serverPort = null;
    }
    @Test
    public void testGetRegisterURL(){
        RegisterRequest request = new RegisterRequest(
                "maria","isabel","email","maria","espinoza","FEMALE"
        );
        RegisterResponse response = proxyTest.register(request,serverHost,serverPort);

        String compareOne = request.getUsername();
        String compareTwo = response.getUsername();

        assertNotNull(compareOne);
        assertNotNull(compareTwo);
        assertEquals(compareOne, compareTwo);
    }

    @Test
    public void testLoginRequest(){
        LoginRequest request = new LoginRequest(
                "maria","isabel"
        );
        LoginResponse response = proxyTest.login(request,serverHost,serverPort);

        String compareOne = request.getUsername();
        String compareTwo = response.getUsername();

  //      assertNotNull(compareOne);
    //    assertNotNull(compareTwo);
        assertEquals(compareOne, compareTwo);
    }

}
