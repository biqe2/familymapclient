package com.example.familymapclient;

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
        public synchronized static DataCache getInstance(){
                if(instance == null){
                        instance = new DataCache();
                }
                return instance;
        }

        private DataCache(){}


        //Map of each person stored by key = personID
        private HashMap<String, PersonModel> people;
        //Map of each event stored by key = eventID
        private HashMap<String, EventModel> events;
        private String loginAuthtoken;
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

        public String getLoginAuthtoken() {
                return loginAuthtoken;
        }

        public void setLoginAuthtoken(String loginAuthtoken) {
                this.loginAuthtoken = loginAuthtoken;
        }

        /*Map<PersonId, List<Event>> personEvents;
        Set<PersonId> paternalAncestors;
        Set<PersonId> maternalAncestors;

        Settings settings;

        Person getPersonById(PersonId id) {...}
        Event getEventById(EventId id) {...}
        List<Event> getPersonEvents(PersonId id) {...}*/

}
