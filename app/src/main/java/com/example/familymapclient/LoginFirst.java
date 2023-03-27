package com.example.familymapclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.*;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.LoginResponse;
import Responses.RegisterResponse;
import Tasks.RegisterTask;
import Tasks.*;

public class LoginFirst extends Fragment {

    private Listener listener;
    private ServerProxy serverProxy = new ServerProxy();
   // RegisterResponse registerResponse = new RegisterResponse();

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_first, container, false);
        //Login section
        EditText serverHost = view.findViewById(R.id.serverHost);
        EditText serverPort = view.findViewById(R.id.serverPort);
        EditText username = view.findViewById(R.id.usernameField);
        String loginUsername = username.getText().toString();
        EditText password = view.findViewById(R.id.passwordField);
        String loginPassword = password.getText().toString();

        LoginRequest loginRequest = new LoginRequest(loginUsername,loginPassword);

        Button loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String stringResponse = bundle.getString("loginKey", "");
                    }
                };

                // Create and execute the download task on a separate thread
                LoginTask loginTask = new LoginTask(uiThreadMessageHandler, loginRequest,
                        serverHost.getText().toString(),serverPort.getText().toString());
                //get port and host from user);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);

                if(listener != null) {
                    listener.notifyDone();
                }

            }
        });

      //  doneButton.setOnClickListener();

        //Register Section


        EditText newUsername = view.findViewById(R.id.newUsernameField);
        EditText newPassword = view.findViewById(R.id.newPasswordField);
        EditText newEmailAddress = view.findViewById(R.id.newEmailAddressField);
        EditText newFirstName = view.findViewById(R.id.newFirstNameField);
        EditText newLastName = view.findViewById(R.id.newLastNameField);
        RadioGroup newGender = (RadioGroup) view.findViewById(R.id.radioGroup);

        String registerUsername = newUsername.getText().toString();
        String registerPassword = newPassword.getText().toString();
        String registerEmailAddress = newEmailAddress.getText().toString();
        String registerFirstName = newFirstName.getText().toString();
        String registerLastName = newLastName.getText().toString();

        RegisterRequest registerRequest = new RegisterRequest(registerUsername,registerPassword,registerEmailAddress,
                registerFirstName,registerLastName,null);


        newGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.newFemale){
                    registerRequest.setGender("FEMALE");
                } else {
                    registerRequest.setGender("MALE");
                }
            }
        });
        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String stringResponse = bundle.getString("registerKey", "");

                    }
                };

                // Create and execute the download task on a separate thread
                RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler, registerRequest,
                        serverHost.getText().toString(),serverPort.getText().toString());
                        //get port and host from user);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(registerTask);

            }
        });






/*
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.notifyDone();
                }
            }
        });*/

        return view;
    }


}
