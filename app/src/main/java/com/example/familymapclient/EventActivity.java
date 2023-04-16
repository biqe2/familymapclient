package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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