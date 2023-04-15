package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class EventActivity extends AppCompatActivity {

    private String selectedEventID = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedEventID = getIntent().getExtras().getString("eventID");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FragmentManager fm = getSupportFragmentManager();
        MapsFragment mapFrag = (MapsFragment) fm.findFragmentById(R.id.myMapFragmentDifferent);

        if(mapFrag==null){
            mapFrag = new MapsFragment();
            mapFrag.setEventID(selectedEventID);
            mapFrag.setPastActivity(this);
            mapFrag.setEventComingActivity(true);
            fm.beginTransaction()
                    .replace(R.id.eventLayout,mapFrag)
                    .commit();
        }
    }
}