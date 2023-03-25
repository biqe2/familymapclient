package com.example.familymapclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.LoginResponse;
import Responses.RegisterResponse;

public class ServerProxy {
    private LoginResponse login(LoginRequest request, String serverHost, String serverPort) {

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

    private RegisterResponse register(RegisterRequest request, String serverHost, String serverPort) {

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
