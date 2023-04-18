package com.example.familymapclient;

import android.app.Person;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import Model.EventModel;
import Model.PersonModel;
import Responses.PersonResponse;
import java.lang.Object.*;

public class DataCache {
        private static DataCache instance;

        //Map of each person stored by key = personID
        private HashMap<String, PersonModel> people;
        //Map of each event stored by key = eventID
        private HashMap<String, EventModel> events;
        private String loginAuthtoken;

        private Boolean maleFiltered = false;
        private Boolean femaleFiltered = false;
        private Boolean spouseLineSwitch = false;



        private HashMap<String, EventModel> filteredEvents;
        private HashMap<String, PersonModel> filteredPeople;
        private PersonModel originalUser;
        private HashMap<String, PersonModel> paternalAncestors;
        private HashMap<String, PersonModel> maternalAncestors;


        public synchronized static DataCache getInstance(){
                if(instance == null){
                        instance = new DataCache();
                }
                return instance;
        }

        private DataCache(){
                filteredEvents = new HashMap<>();
                filteredPeople = new HashMap<>();
                events = new HashMap<>();
                people = new HashMap<>();
                originalUser = new PersonModel();
                paternalAncestors = new HashMap<>();
                maternalAncestors = new HashMap<>();
        }

        public PersonModel getOriginalUser() {
                return originalUser;
        }

        public void setOriginalUser(PersonModel originalUser) {
                this.originalUser = originalUser;
        }

        public Boolean getSpouseLineSwitch() {
                return spouseLineSwitch;
        }

        public void setSpouseLineSwitch(Boolean spouseLineSwitch) {
                this.spouseLineSwitch = spouseLineSwitch;
        }

        public void setFilteredPeople(HashMap<String, PersonModel> filteredPeople) {
                this.filteredPeople = filteredPeople;
        }


        public Boolean getMaleFiltered() {
                return maleFiltered;
        }

        public void setMaleFiltered(Boolean maleFiltered) {
                this.maleFiltered = maleFiltered;
        }

        public Boolean getFemaleFiltered() {
                return femaleFiltered;
        }

        public void setFemaleFiltered(Boolean femaleFiltered) {
                this.femaleFiltered = femaleFiltered;
        }
        public Map<String, PersonModel> getFilteredPeople() {
                if(maleFiltered || femaleFiltered) {
                        filteredPeople = new HashMap<>();
                        if (maleFiltered) {
                                for (Map.Entry<String, PersonModel> entry : people.entrySet()) {
                                        PersonModel person = entry.getValue();
                                        String personKey = entry.getKey();
                                        if (person != null && personKey != null) {
                                                if (!person.getGender().equals("f")) {
                                                        filteredPeople.put(personKey, person);
                                                }
                                        }
                                }
                        }
                        if (femaleFiltered) {
                                for (Map.Entry<String, PersonModel> entry : people.entrySet()) {
                                        PersonModel person = entry.getValue();
                                        String personKey = entry.getKey();
                                        if (person != null && personKey != null) {
                                                if (person.getGender().equals("f")) {
                                                        filteredPeople.put(personKey, person);
                                                }
                                        }
                                }
                        }
                        return filteredPeople;
                }

                return people;
        }
        public Map<String, EventModel> getFilteredEvents() {
                if(maleFiltered || femaleFiltered) {
                        filteredEvents = new HashMap<>();
                        if (maleFiltered) {
                                for (Map.Entry<String, EventModel> entry : events.entrySet()) {
                                        EventModel event = entry.getValue();
                                        String eventKey = entry.getKey();
                                        PersonModel person = people.get(event.getPersonID());
                                        if (person != null && eventKey != null && event != null) {
                                                if (!person.getGender().equals("f")) {
                                                        filteredEvents.put(eventKey, event);
                                                }
                                        }
                                }

                        }
                        if (femaleFiltered) {
                                for (Map.Entry<String, EventModel> entry : events.entrySet()) {
                                        EventModel event = entry.getValue();
                                        String eventKey = entry.getKey();
                                        PersonModel person = people.get(event.getPersonID());
                                        if (person != null && eventKey != null && event != null) {
                                                if (person.getGender().equals("f")) {
                                                        filteredEvents.put(eventKey, event);
                                                }
                                        }
                                }

                        }
                        return filteredEvents;
                }

                return events;
        }

        public void setFilteredEvents(HashMap<String, EventModel> filteredEvents) {
                this.filteredEvents = filteredEvents;
        }
//  private TreeMultimap<String, EventModel> eventsByPersonID;


        public static void setInstance(DataCache instance) {
                DataCache.instance = instance;
        }

        public Map<String, PersonModel> getPeople() {
                return people;
        }

        public void setPeople(PersonModel[] personList) {
                if(personList != null){
                        people = new HashMap<String, PersonModel>();
                        for(int i = 0; i < personList.length; i++){
                                people.put(personList[i].getPersonID(),personList[i]);
                        }
                }
        }

        public Map<String, EventModel> getEvents() {
                return events;
        }

        public void setEvents(EventModel[] eventList) {
                if(eventList != null){
                        events = new HashMap<String, EventModel>();
                        for(int i = 0; i < eventList.length; i++){
                                events.put(eventList[i].getEventID(),eventList[i]);
                        }
                }
        }

        public void deleteEvent(String eventID) {
                events.remove(eventID);
        }

        public String getLoginAuthtoken() {
                return loginAuthtoken;
        }

        public void setLoginAuthtoken(String loginAuthtoken) {
                this.loginAuthtoken = loginAuthtoken;
        }

        //Generating ancestor lines
        public void createPaternalAndMaternalLines(){
                PersonModel motherOfUser = people.get(originalUser.getMotherID());
                PersonModel fatherOfUser = people.get(originalUser.getFatherID());

                addParentsToMaternalOrPaternal(motherOfUser,true);
                addParentsToMaternalOrPaternal(fatherOfUser,false);
        }

        private void addParentsToMaternalOrPaternal(PersonModel personReceived, Boolean isMaternalSide){
                if (isMaternalSide){
                        maternalAncestors.put(personReceived.getPersonID(),personReceived);
                } else {
                        paternalAncestors.put(personReceived.getPersonID(),personReceived);
                }

                if (personReceived.getFatherID() == null || personReceived.getFatherID().equals("")){
                        return;
                } else {
                        if(isMaternalSide){
                                PersonModel motherOfPersonReceived = people.get(personReceived.getMotherID());
                                PersonModel fatherOfPersonReceived = people.get(personReceived.getFatherID());
                                addParentsToMaternalOrPaternal(motherOfPersonReceived, true);
                                addParentsToMaternalOrPaternal(fatherOfPersonReceived, true);
                        } else {
                                PersonModel motherOfPersonReceived = people.get(personReceived.getMotherID());
                                PersonModel fatherOfPersonReceived = people.get(personReceived.getFatherID());
                                addParentsToMaternalOrPaternal(motherOfPersonReceived,false);
                                addParentsToMaternalOrPaternal(fatherOfPersonReceived,false);
                        }
                }
        }

}
