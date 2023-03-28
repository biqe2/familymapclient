package com.example.familymapclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LoginSecond extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_second, container, false);
        if(getArguments() != null){
            TextView textView = view.findViewById(R.id.receivedLogin);
            String receivedText = getArguments().getString("loginViewText");
            textView.setText(receivedText);
        }


        return view;

    }
}
