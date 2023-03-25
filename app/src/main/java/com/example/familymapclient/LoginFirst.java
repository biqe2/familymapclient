package com.example.familymapclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.*;

public class LoginFirst extends Fragment {

    private Listener listener;

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

        Button doneButton = view.findViewById(R.id.loginButton);
        EditText username = view.findViewById(R.id.usernameField);
        String loginUsername = username.getText().toString();
        EditText password = view.findViewById(R.id.passwordField);
        String loginPassword = password.getText().toString();


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.notifyDone();
                }
            }
        });

        return view;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.newMale:
                if (checked)
                    // Gender = Male
                    break;
            case R.id.newFemale:
                if (checked)
                    // Gender = Female
                    break;
        }
    }
}
