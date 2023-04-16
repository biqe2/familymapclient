package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Person;
import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

import Model.EventModel;
import Model.PersonModel;

public class SearchActivity extends AppCompatActivity {

    private static final int PEOPLE_GROUP_POSITION = 0;
    private static final int EVENTS_GROUP_POSITION = 1;
    private DataCache data;

    private final List<PersonModel> searchedPeople = null;
    private final List<EventModel> searchedEvents = null;
    Map<String, EventModel> events;
    Map<String, PersonModel> people;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        getSupportActionBar().setTitle("Family Map: Search");

        data = DataCache.getInstance();

        events =  data.getEvents();
        people =  data.getPeople();

        List<EventModel> allEvents = new ArrayList<EventModel>();
        List<PersonModel> allPeople = new ArrayList<PersonModel>();

        for(Map.Entry<String,EventModel> entry: events.entrySet()){
            EventModel event = entry.getValue();
            allEvents.add(event);
        }

        for(Map.Entry<String,PersonModel> entry: people.entrySet()){
            PersonModel person = entry.getValue();
            allPeople.add(person);
        }

        SearcherAdaptor adapter = new SearcherAdaptor(allPeople, allEvents);
        recyclerView.setAdapter(adapter);

    }

    private class SearcherAdaptor extends RecyclerView.Adapter<SearcherViewHolder>{
        private final List<PersonModel> people;
        private final List<EventModel> events;
        SearcherAdaptor(List<PersonModel> people,List<EventModel> events){
            this.events = events;
            this.people = people;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PEOPLE_GROUP_POSITION : EVENTS_GROUP_POSITION;
        }

        @NonNull
        @Override
        public SearcherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view;
            view = getLayoutInflater().inflate(R.layout.expandable_list_person_activity, parent,false);
            return new SearcherViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearcherViewHolder holder, int position) {
            if(position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class SearcherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textTop;
        private final TextView textBottom;
        private final ImageView img;
        private final int viewType;
        private PersonModel person;
        private EventModel event;

        SearcherViewHolder(View view, int viewType){
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);
            textTop = itemView.findViewById(R.id.textTop);
            textBottom = itemView.findViewById(R.id.textBottom);
            img = itemView.findViewById(R.id.iconImageView);

        }

        private void bind(PersonModel person){
            this.person = person;
            textTop.setText(person.getFirstName() + " " + person.getLastName());
            textBottom.setText("");
            if(person.getGender().equals("f")){
                img.setImageResource(R.drawable.femaleicon);
            } else{
                img.setImageResource(R.drawable.maleicon);
            }
        }
        private void bind(EventModel event){
            this.event = event;
            PersonModel personSelected = people.get(event.getPersonID());

            textTop.setText(event.getEventType().toUpperCase() + ": " + event.getCity() +
                    ", " + event.getCountry() + " (" + event.getYear() + ")");
            textBottom.setText(personSelected.getFirstName() + " " + personSelected.getLastName());
            img.setImageResource(R.drawable.markericon);
        }
        @Override
        public void onClick(View view){
            if(viewType == EVENTS_GROUP_POSITION){
                Intent intent = new Intent(SearchActivity.this,EventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle mBundle = new Bundle();
                mBundle.putString("eventID",event.getEventID());
                intent.putExtras(mBundle);
                startActivity(intent);
            } else{
                Intent intent = new Intent(SearchActivity.this,PersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle mBundle = new Bundle();
                mBundle.putString("personID",person.getPersonID());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
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