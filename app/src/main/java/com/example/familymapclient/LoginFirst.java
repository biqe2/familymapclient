package com.example.familymapclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
    private String serverHost = "10.37.178.31";
    private String serverPort = "8080";
    private String gender = "FEMALE";
    private LoginRequest loginRequest;
    EditText newUsername;
    EditText newPassword;
    EditText newEmailAddress;
    EditText newFirstName;
    EditText newLastName;
    Button loginButton;
    Button registerButton;
    private RegisterRequest registerRequest;

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = newUsername.getText().toString();
            String passwordInput = newPassword.getText().toString();
            String emailInput = newEmailAddress.getText().toString();
            String firstNameInput = newFirstName.getText().toString();
            String lastNameInput = newLastName.getText().toString();

            loginButton.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
            registerButton.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty() && !emailInput.isEmpty() && !firstNameInput.isEmpty() && !lastNameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
        // RegisterResponse registerResponse = new RegisterResponse();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_first, container, false);

         newUsername = view.findViewById(R.id.newUsernameField);
         newPassword = view.findViewById(R.id.newPasswordField);
         newEmailAddress = view.findViewById(R.id.newEmailAddressField);
         newFirstName = view.findViewById(R.id.newFirstNameField);
         newLastName = view.findViewById(R.id.newLastNameField);
        RadioGroup newGender = view.findViewById(R.id.radioGroup);
        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);

        newUsername.addTextChangedListener(textWatcher);
        newPassword.addTextChangedListener(textWatcher);
        newEmailAddress.addTextChangedListener(textWatcher);
        newFirstName.addTextChangedListener(textWatcher);
        newLastName.addTextChangedListener(textWatcher);

        newGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = checkedId == R.id.newMale ? "MALE" : "FEMALE";
            }

        });





        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest = new LoginRequest(newUsername.getText().toString(),newPassword.getText().toString());
                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String stringResponse = bundle.getString("loginKey", "");
                        //Create the toast
                       // Toast toast = Toast.makeText(getContext(), stringResponse, Toast.LENGTH_SHORT);
                        //toast.show();
                        if(stringResponse.equals("Error:processing longin")){
                            Toast toast = Toast.makeText(getContext(), stringResponse, Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            if(listener != null) {
                                listener.notifyDone();
                            }
                        }

                    }
                };

                // Create and execute the download task on a separate thread
                LoginTask loginTask = new LoginTask(uiThreadMessageHandler, loginRequest,
                        serverHost,serverPort);
                //get port and host from user);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);
            }
        });

        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerRequest = new RegisterRequest(newUsername.getText().toString(),newPassword.getText().toString(),newEmailAddress.getText().toString(),
                        newFirstName.getText().toString(),newLastName.getText().toString(),gender);
                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String stringResponse = bundle.getString("registerKey", "");
                        if(stringResponse.equals("Error:processing register")){
                            Toast toast = Toast.makeText(getContext(), stringResponse, Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            if(listener != null) {
                                listener.notifyDone();
                            }
                        }
                    }
                };

                // Create and execute the download task on a separate thread
                RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler, registerRequest,
                        serverHost,serverPort);
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
