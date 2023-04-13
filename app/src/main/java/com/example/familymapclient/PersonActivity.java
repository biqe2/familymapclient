package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Person;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.EventModel;
import Model.PersonModel;

public class PersonActivity extends AppCompatActivity {
    private String selectedPersonID = new String();
    private RecyclerView recyclerView;
    private PersonModel father = null;
    private PersonModel mother = null;
    private PersonModel child = null;
    private PersonModel spouse = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedPersonID = getIntent().getExtras().getString("personID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        TextView firstName = (TextView) findViewById(R.id.firstNameTitle);
        TextView lastName = (TextView) findViewById(R.id.lastNameTitle);
        TextView gender = (TextView) findViewById(R.id.genderTitle);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        DataCache data = DataCache.getInstance();
        PersonModel personSelected = data.getPeople().get(selectedPersonID);

        firstName.setText(personSelected.getFirstName());
        lastName.setText(personSelected.getLastName());
        if(personSelected.getGender().equals("f")){
            gender.setText("Female");
        } else{
            gender.setText("Male");
        }
        Map<String, EventModel> events =  data.getEvents();
        List<EventModel> personEvents = new ArrayList<EventModel>();

        for(Map.Entry<String,EventModel> entry: events.entrySet()){
            EventModel event = entry.getValue();
            if(event.getPersonID().equals(selectedPersonID)){
                personEvents.add(event);
            }
        }

        Map<String, PersonModel> people =  data.getPeople();

        for(Map.Entry<String,PersonModel> entry: people.entrySet()){
            PersonModel person = entry.getValue();
            String personGender = person.getGender();
            if(personSelected.getFatherID().equals(person.getPersonID())){
                father = person;
            } else if(personSelected.getMotherID().equals(person.getPersonID())){
                mother = person;
            } else if(personSelected.getSpouseID().equals(person.getPersonID())){
                spouse = person;
            } else if(selectedPersonID.equals(person.getFatherID()) || selectedPersonID.equals(person.getMotherID())){
                child = person;
            }
        }









    }
}