package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch maleSwitch;
    private Switch femaleSwitch;
    private Switch spouseLineSwitch;
    private Switch familyTreeLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        maleSwitch = (Switch) findViewById(R.id.maleSwitch);
        femaleSwitch = (Switch) findViewById(R.id.femaleSwitch);
        spouseLineSwitch = (Switch) findViewById(R.id.spouseLineSwitch);
        familyTreeLines = (Switch) findViewById(R.id.familyTreeLines);
        DataCache data = DataCache.getInstance();
        maleSwitch.setChecked(data.getMaleFiltered());
        femaleSwitch.setChecked(data.getFemaleFiltered());
        spouseLineSwitch.setChecked(data.getSpouseLineSwitch());
        familyTreeLines.setChecked(data.getSpouseLineSwitch());

        maleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "you changed the settings for male");
                if(maleSwitch.isChecked()){
                    data.setMaleFiltered(true);
                } else {
                    data.setMaleFiltered(false);
                }
            }
        });

        femaleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "you changed the settings for female");
                if(femaleSwitch.isChecked()){
                    data.setFemaleFiltered(true);
                } else {
                    data.setFemaleFiltered(false);
                }
            }
        });
        spouseLineSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "you changed the settings for female");
                if(spouseLineSwitch.isChecked()){
                    data.setSpouseLineSwitch(true);
                } else {
                    data.setSpouseLineSwitch(false);
                }
            }
        });



        RelativeLayout logout = (RelativeLayout) findViewById(R.id.logoutView);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }
        return(true);
    }
}