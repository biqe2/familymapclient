package com.example.familymapclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

import Model.PersonModel;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

public class ServerProxy {


    public LoginResponse login(LoginRequest request, String serverHost, String serverPort) {

        try {
            Gson gson = new Gson();
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");

            http.setDoOutput(true);    // There is a request body

            http.connect();

            String reqData = gson.toJson(request);

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream resultHttp = http.getInputStream();

                String resultData = readString(resultHttp);

                System.out.println(reqData);

                LoginResponse response = gson.fromJson(resultData, LoginResponse.class);
                return response;
            } else {

                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String resultData = readString(respBody);

                System.out.println(reqData);


                LoginResponse response = gson.fromJson(resultData, LoginResponse.class);
                return response;

            }
        } catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public RegisterResponse register(RegisterRequest request, String serverHost, String serverPort) {

        try {
            Gson gson = new Gson();
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");

            http.setDoOutput(true);    // There is a request body

            http.connect();

            String reqData = gson.toJson(request);

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream resultHttp = http.getInputStream();

                String resultData = readString(resultHttp);

                System.out.println(reqData);

                RegisterResponse response = gson.fromJson(resultData, RegisterResponse.class);
                return response;
            } else {

                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String resultData = readString(respBody);

                System.out.println(reqData);


                RegisterResponse response = gson.fromJson(resultData, RegisterResponse.class);
                return response;

            }
        } catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }


    public PersonResponse getPeople(String authtoken, String serverHost, String serverPort) {

        try {
            Gson gson = new Gson();
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");

            http.setDoOutput(false);    // There is a request body

            http.addRequestProperty("Authorization", authtoken);

            http.connect();

            //For get I dont need the next 4 lines? 147-153

            String reqData = gson.toJson(authtoken);

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //We added this basically
                InputStream resultHttp = http.getInputStream();

                String resultData = readString(resultHttp);

                System.out.println(reqData);

                PersonResponse response = gson.fromJson(resultData, PersonResponse.class);
                return response;
            } else {

                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String resultData = readString(respBody);

                System.out.println(reqData);

                //We added the next two lines
                PersonResponse response = gson.fromJson(resultData, PersonResponse.class);
                return response;

            }
        } catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }
    public EventResponse getEvents(String authtoken, String serverHost, String serverPort) {

        try {
            Gson gson = new Gson();
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");

            http.setDoOutput(false);    // There is a request body

            http.addRequestProperty("Authorization", authtoken);

            http.connect();

            String reqData = gson.toJson(authtoken);

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream resultHttp = http.getInputStream();

                String resultData = readString(resultHttp);

                System.out.println(reqData);

                EventResponse response = gson.fromJson(resultData, EventResponse.class);
                return response;
            } else {

                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String resultData = readString(respBody);

                System.out.println(reqData);


                EventResponse response = gson.fromJson(resultData, EventResponse.class);
                return response;

            }
        } catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
