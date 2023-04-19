package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Model.EventModel;
import Model.PersonModel;

public class MapsFragment extends Fragment{
    private GoogleMap mMap;
    private EventModel eventSelected;
    private View view;
    private Boolean personInfo = false;
    private Boolean eventComingActivity = false;
    private String eventID;
    private Context pastActivity;
    private DataCache data = DataCache.getInstance();
    private Map<String, EventModel> eventLists;
    private Map<String, PersonModel> peopleLists;

    private List<Polyline> polylines = new ArrayList<Polyline>();

    public Boolean getEventComingActivity() {
        return eventComingActivity;
    }

    public void setEventComingActivity(Boolean eventComingActivity) {
        this.eventComingActivity = eventComingActivity;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public Context getPastActivity() {
        return pastActivity;
    }

    public void setPastActivity(Context pastActivity) {
        this.pastActivity = pastActivity;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            eventLists = data.getEvents();
            peopleLists = data.getPeople();
            List<String> invalidEvents = new ArrayList<>();

            for(Map.Entry<String, EventModel> entry : eventLists.entrySet()){
                EventModel event = entry.getValue();
                PersonModel person = peopleLists.get(event.getPersonID());
                if(person == null){
                    invalidEvents.add(event.getEventID());
                }
            }
            for(int i = 0; i< invalidEvents.size(); i++){
                eventLists.remove(invalidEvents.get(i));
                data.deleteEvent(invalidEvents.get(i));
            }
            data.createPaternalAndMaternalLines();
            drawMap(eventLists,peopleLists);
        }
    };

    public void drawMap(Map<String, EventModel> eventMap, Map<String, PersonModel> peopleMap){
        Map<String,Integer> eventsType = new HashMap<String,Integer>();

        for(Map.Entry<String, EventModel> entry : eventMap.entrySet()){
            Integer color;
            Random random = new Random();
            EventModel event = entry.getValue();
            PersonModel person = peopleMap.get(event.getPersonID());
            String eventType = event.getEventType().toLowerCase();
            Boolean mapEmpty = eventsType.isEmpty();
            if(mapEmpty){//eventsType == null){
                color = random.nextInt(350);
                eventsType.put(eventType, color);
            } else {
                Integer type = eventsType.get(eventType);
                if(type == null){
                    color = random.nextInt(350);
                    eventsType.put(eventType, color);
                } else {
                    color = eventsType.get(eventType);
                }
            }
            if(person != null) {
                LatLng eventPlace = new LatLng(event.getLatitude(), event.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(eventPlace).title(event.getCity() +
                        ", " + event.getCountry()).icon(BitmapDescriptorFactory.defaultMarker(color)));
                marker.setTag(event);
            }
        }

        if(eventComingActivity){
            eventSelected = eventMap.get(eventID);
            selectedEvent(eventSelected, eventMap, peopleMap);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                eventSelected = (EventModel) marker.getTag();
                selectedEvent(eventSelected, eventMap,peopleMap);
                Log.d("Click", "was clicked you");

                return false;
            }
        });
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


        LinearLayout changeActivity = view.findViewById(R.id.bottomOfScreen);
        changeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("personID",eventSelected.getPersonID());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }

    public void selectedEvent(EventModel eventSelected, Map<String, EventModel> eventMap, Map<String, PersonModel> peopleMap){

        PersonModel child;
        PersonModel spouse = null;
        PersonModel father = null;
        PersonModel mother = null;
        String selectedPersonID = new String();
        selectedPersonID = eventSelected.getPersonID();


        for(Polyline line : polylines){
            line.remove();
        }
        polylines.clear();

        LatLng placeToCenter  = new LatLng(eventSelected.getLatitude(),eventSelected.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(placeToCenter));
        PersonModel personSelected = peopleMap.get(eventSelected.getPersonID());
        ImageView img = (ImageView) view.findViewById(R.id.iconImageView);
        if(personSelected.getGender().equals("f")){
            img.setImageResource(R.drawable.femaleicon);
        } else{
            img.setImageResource(R.drawable.maleicon);
        }
        TextView textTop = view.findViewById(R.id.textTop);
        TextView textBottom = view.findViewById(R.id.textBottom);

        textTop.setText(personSelected.getFirstName() + " " + personSelected.getLastName());
        textBottom.setText(eventSelected.getEventType().toUpperCase() + ": " + eventSelected.getCity() +  ", " +
                eventSelected.getCountry() +" (" + eventSelected.getYear() +")");

        //polylines

        for(Map.Entry<String,PersonModel> entry: peopleMap.entrySet()){
            PersonModel person = entry.getValue();

            if(personSelected.getFatherID() != null || personSelected.getMotherID() != null) {
                if (personSelected.getFatherID().equals(person.getPersonID())) {
                    father = person;
                } else if (personSelected.getMotherID().equals(person.getPersonID())) {
                    mother = person;
                }
            }
            if(personSelected.getSpouseID() != null) {
                if (personSelected.getSpouseID().equals(person.getPersonID())) {
                    spouse = person;
                }
            }
            if(person.getFatherID() != null || person.getMotherID() != null) {
                if (selectedPersonID.equals(person.getFatherID()) || selectedPersonID.equals(person.getMotherID())) {
                    child = person;
                }
            }
        }
        //drawing spouse lines
        if(data.getSpouseLineSwitch()) {
            if (spouse != null) {
                List<EventModel> spouseEvents = new ArrayList<EventModel>();
                spouseEvents = findEventsUser(spouse);

                if (!spouseEvents.isEmpty()) {
                    EventModel event = spouseEvents.get(0);
                    LatLng spouseBirth = new LatLng(event.getLatitude(), event.getLongitude());
                    PolylineOptions spouseLine = new PolylineOptions()
                            .add(placeToCenter)
                            .add(spouseBirth)
                            //yellow lines
                            .color(0xffffff00);
                    Polyline polylineSpouse = mMap.addPolyline(spouseLine);
                    polylines.add(polylineSpouse);
                }
            }
        }
        //drawing family tree lines
        if(data.getFamilyTreeLinesSwitch()) {
            if (father != null) {
                List<EventModel> fatherEvents = new ArrayList<EventModel>();
                fatherEvents = findEventsUser(father);

                if (!fatherEvents.isEmpty()) {
                    EventModel event = fatherEvents.get(0);
                    LatLng spouseBirth = new LatLng(event.getLatitude(), event.getLongitude());
                    float lineWidth = 20;
                    PolylineOptions spouseLine = new PolylineOptions()
                            .add(placeToCenter)
                            .add(spouseBirth)
                            //green lines
                            .color(0xff00ff00)
                            .width(lineWidth);
                    Polyline polylineSpouse = mMap.addPolyline(spouseLine);
                    polylines.add(polylineSpouse);
                    generateFamilyLines(father, spouseBirth, lineWidth, peopleMap);
                }
            }

            if (mother != null) {
                List<EventModel> motherEvents = new ArrayList<EventModel>();
                motherEvents = findEventsUser(mother);
                if (!motherEvents.isEmpty()) {
                    EventModel event = motherEvents.get(0);
                    LatLng spouseBirth = new LatLng(event.getLatitude(), event.getLongitude());
                    float lineWidth = 20;
                    PolylineOptions spouseLine = new PolylineOptions()
                            .add(placeToCenter)
                            .add(spouseBirth)
                            //blue lines
                            .color(0xff0000ff)
                            .width(lineWidth);

                    Polyline polylineSpouse = mMap.addPolyline(spouseLine);
                    polylines.add(polylineSpouse);
                    generateFamilyLines(mother, spouseBirth, lineWidth, peopleMap);
                }
            }
        }
        //drawing events lines

        if(data.getLifeStoryLinesSwitch()) {
            List<EventModel> personEvents = new ArrayList<EventModel>();
            personEvents = findEventsUser(personSelected);

            if (!personEvents.isEmpty()) {
                for (int i = 0; i < personEvents.size()-1;i++){
                    LatLng firstEvent = new LatLng(personEvents.get(i).getLatitude(), personEvents.get(i).getLongitude());
                    LatLng secondEvent = new LatLng(personEvents.get(i+1).getLatitude(), personEvents.get(i+1).getLongitude());
                    PolylineOptions eventLine = new PolylineOptions()
                            .add(firstEvent)
                            .add(secondEvent)
                            //magenta lines
                            .color(0xffff00ff);
                    Polyline polylineEvent = mMap.addPolyline(eventLine);
                    polylines.add(polylineEvent);
                }
            }
        }

        personInfo = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchIcon = menu.findItem(R.id.search);
        searchIcon.setIcon(R.drawable.searchicon);
        MenuItem configIcon = menu.findItem(R.id.settings);
        configIcon.setIcon(R.drawable.configicon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:

                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (eventComingActivity){
            setHasOptionsMenu(false);
        } else {
            setHasOptionsMenu(true);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mMap != null) {
            if (!data.getMaleFiltered() || !data.getFemaleFiltered() || !data.getMothersSideSwitch() || !data.getFathersSideSwitch()) {
                Map<String, EventModel> selectedEvents = data.getFilteredEvents();
                Map<String, PersonModel> selectedPeople = data.getFilteredPeople();
                mMap.clear();
                drawMap(selectedEvents, selectedPeople);
            } else {
                Map<String, EventModel> selectedEvents = data.getEvents();
                Map<String, PersonModel> selectedPeople = data.getPeople();
                mMap.clear();
                drawMap(selectedEvents, selectedPeople);
            }
        }

        System.out.println("On Resume is working");
    }

    public void generateFamilyLines(PersonModel user, LatLng orgLocation,float lineWidth, Map<String, PersonModel> peopleMap){
        PersonModel father = peopleMap.get(user.getFatherID());
        PersonModel mother = peopleMap.get(user.getMotherID());
        if(father == null || user.getFatherID().equals("")){
            return;
        } else{
            List<EventModel> fatherEventLines = findEventsUser(father);
            if(!fatherEventLines.isEmpty()){
                EventModel event = fatherEventLines.get(0);
                LatLng fatherEventLine = new LatLng(event.getLatitude(),event.getLongitude());
                float lineWidthFather = lineWidth * 0.7f;
                PolylineOptions fatherLine = new PolylineOptions()
                        .add(orgLocation)
                        .add(fatherEventLine)
                        //gray lines
                        .color(0xff888888)
                        .width(lineWidthFather);
                Polyline polylineSpouse = mMap.addPolyline(fatherLine);
                polylines.add(polylineSpouse);
                generateFamilyLines(father,fatherEventLine, lineWidthFather, peopleMap);
            }
        }
        if(mother == null || user.getMotherID().equals("")){
            return;
        } else{
            List<EventModel> motherEventLines = findEventsUser(mother);
            if(!motherEventLines.isEmpty()){
                EventModel event = motherEventLines.get(0);
                LatLng motherEventLine = new LatLng(event.getLatitude(),event.getLongitude());
                float lineWidthMother = lineWidth *0.7f;
                PolylineOptions motherLine = new PolylineOptions()
                        .add(orgLocation)
                        .add(motherEventLine)
                        //gray lines
                        .color(0xff888888)
                        .width(lineWidthMother);
                Polyline polylineSpouse = mMap.addPolyline(motherLine);
                polylines.add(polylineSpouse);
                generateFamilyLines(father,motherEventLine,lineWidthMother, peopleMap);
            }
        }
    }

    public List<EventModel> findEventsUser(PersonModel person){
        List<EventModel> personEvents = new ArrayList<EventModel>();
        String personID = person.getPersonID();
        for(Map.Entry<String,EventModel> entry: eventLists.entrySet()){
            EventModel personEvent = entry.getValue();

            if(personEvent.getPersonID().equals(personID)){
                personEvents.add(personEvent);
            }
        }
        Collections.sort(personEvents, new Comparator<EventModel>() {
            @Override
            public int compare(EventModel o1, EventModel o2) {
                return o1.getYear().compareTo(o2.getYear());
            }
        });
        return personEvents;
    }


}