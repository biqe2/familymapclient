package com.example.familymapclient;


import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

