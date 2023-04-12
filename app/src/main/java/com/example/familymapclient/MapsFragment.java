package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import Model.EventModel;
import Model.PersonModel;

public class MapsFragment extends Fragment{
    private GoogleMap mMap;
    private EventModel eventSelected;
    private View view;
    private Boolean personInfo = false;


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

            DataCache data = DataCache.getInstance();
            Map<String, EventModel> eventLists = data.getEvents();
            Integer color;
            for(Map.Entry<String, EventModel> entry : eventLists.entrySet()){
                EventModel event = entry.getValue();
                if(event.getEventType().equals("Birth")){
                    color = 120;
                } else if (event.getEventType().equals("Death")){
                    color = 0;
                } else {
                    color = 300;
                }
                LatLng eventPlace = new LatLng(event.getLatitude(),event.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(eventPlace).title(event.getEventType()).icon(BitmapDescriptorFactory.defaultMarker(color)));
                marker.setTag(event);
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    DataCache data = DataCache.getInstance();
                    eventSelected = (EventModel) marker.getTag();
                    PersonModel personSelected = data.getPeople().get(eventSelected.getPersonID());
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

                    personInfo = true;

                    Log.d("Click", "was clicked");

                    return false;
                }
            });

        }
    };



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
                startActivity(intent);
            }
        });
    }

}