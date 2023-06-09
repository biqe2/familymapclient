package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private PersonModel personSelected;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedPersonID = getIntent().getExtras().getString("personID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setTitle("Family Map: Person Details");

        TextView firstName = (TextView) findViewById(R.id.firstNameTitle);
        TextView lastName = (TextView) findViewById(R.id.lastNameTitle);
        TextView gender = (TextView) findViewById(R.id.genderTitle);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        DataCache data = DataCache.getInstance();
        personSelected = data.getPeople().get(selectedPersonID);

        firstName.setText(personSelected.getFirstName());
        lastName.setText(personSelected.getLastName());
        if(personSelected.getGender().equals("f")){
            gender.setText("Female");
        } else{
            gender.setText("Male");
        }
        //Generating the expandableListView
        Map<String, EventModel> events =  data.getFilteredEvents();

        List<EventModel> personEvents = new ArrayList<EventModel>();
        List<PersonModel> personFamily = new ArrayList<PersonModel>();

        personEvents = data.findEventsUser(personSelected);

        Map<String, PersonModel> people =  data.getPeople();

        data.setDirectFamily(people, personSelected, selectedPersonID);
        child = data.getDirectFamily().get("child");
        father = data.getDirectFamily().get("father");
        mother = data.getDirectFamily().get("mother");
        spouse = data.getDirectFamily().get("spouse");

        for(Map.Entry<String,PersonModel> entry: data.getDirectFamily().entrySet()){
            PersonModel person = entry.getValue();
            personFamily.add(person);
        }
        expandableListView.setAdapter(new ExpandableListAdapter(personFamily,personEvents));
    }
    private class ExpandableListAdapter extends BaseExpandableListAdapter{

        private static final int PERSON_FAMILY_GROUP_POSITION = 1;
        private static final int PERSON_EVENTS_GROUP_POSITION = 0;

        private final List<PersonModel> personFamily;
        private final List<EventModel> personEvents;

        ExpandableListAdapter(List<PersonModel> personFamily, List<EventModel> personEvents){
            this.personFamily = personFamily;
            this.personEvents = personEvents;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case PERSON_FAMILY_GROUP_POSITION:
                    return personFamily.size();
                case PERSON_EVENTS_GROUP_POSITION:
                    return personEvents.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case PERSON_FAMILY_GROUP_POSITION:
                    titleView.setText("FAMILY");
                    break;
                case PERSON_EVENTS_GROUP_POSITION:
                    titleView.setText("LIFE EVENTS");
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;
            switch(groupPosition) {
                case PERSON_FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.expandable_list_person_activity, parent, false);
                    initializeExpandableListFamily(itemView, childPosition);
                    break;
                case PERSON_EVENTS_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.expandable_list_person_activity, parent, false);
                    initializeExpandableListEvents(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }

        private void initializeExpandableListFamily(View familyView, final int childPosition) {
            PersonModel familyMember = personFamily.get(childPosition);

            ImageView img = (ImageView) familyView.findViewById(R.id.iconImageView);
            if(familyMember.getGender().equals("f")){
                img.setImageResource(R.drawable.femaleicon);
            } else{
                img.setImageResource(R.drawable.maleicon);
            }
            TextView textTop = familyView.findViewById(R.id.textTop);
            TextView textBottom = familyView.findViewById(R.id.textBottom);
            textTop.setText(familyMember.getFirstName() + " " + familyMember.getLastName());

            if(personSelected.getFatherID() != null || personSelected.getMotherID() != null) {
                //I might be able to make this one if statement with ors we will see.
                if (personSelected.getFatherID().equals(familyMember.getPersonID())) {
                    textBottom.setText("Father");
                } else if (personSelected.getMotherID().equals(familyMember.getPersonID())) {
                    textBottom.setText("Mother");
                }
            }
            if(personSelected.getSpouseID() != null) {
                if (personSelected.getSpouseID().equals(familyMember.getPersonID())) {
                    textBottom.setText("Spouse");
                }
            }
            if(familyMember.getFatherID() != null || familyMember.getMotherID() != null) {
                if (selectedPersonID.equals(familyMember.getFatherID()) || selectedPersonID.equals(familyMember.getMotherID())) {
                    textBottom.setText("Child");
                }
            }

            LinearLayout familyMemberSelected = familyView.findViewById(R.id.personDetail);
            familyMemberSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this,PersonActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("personID",familyMember.getPersonID());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });

        }

        private void initializeExpandableListEvents(View eventView, final int childPosition) {
            EventModel eventPerson = personEvents.get(childPosition);

            ImageView img = (ImageView) eventView.findViewById(R.id.iconImageView);
            img.setImageResource(R.drawable.markericon);
            TextView textTop = eventView.findViewById(R.id.textTop);
            TextView textBottom = eventView.findViewById(R.id.textBottom);
            textTop.setText(eventPerson.getEventType().toUpperCase() + ": " + eventPerson.getCity() +
                    ", " + eventPerson.getCountry() + " (" + eventPerson.getYear() + ")");
            textBottom.setText(personSelected.getFirstName() + " " + personSelected.getLastName());

            LinearLayout familyMemberSelected = eventView.findViewById(R.id.personDetail);
            familyMemberSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this,EventActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("eventID",eventPerson.getEventID());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
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