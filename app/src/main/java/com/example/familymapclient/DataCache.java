package com.example.familymapclient;

import android.app.Person;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
        private PersonModel originalSpouse;
        private Boolean maleFiltered = true;
        private Boolean femaleFiltered = true;
        private Boolean spouseLineSwitch = true;
        private Boolean familyTreeLinesSwitch = true;
        private Boolean mothersSideSwitch = true;
        private Boolean fathersSideSwitch = true;
        private Boolean lifeStoryLinesSwitch = true;

        private HashMap<String, EventModel> filteredEvents;
        private HashMap<String, PersonModel> filteredPeople;
        private PersonModel originalUser;
        private HashMap<String, PersonModel> paternalAncestors;
        private HashMap<String, PersonModel> maternalAncestors;
        private HashMap<String, PersonModel> directFamily;


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
                originalSpouse = new PersonModel();
                directFamily = new HashMap<>();
        }

        public HashMap<String, PersonModel> getDirectFamily() {
                return directFamily;
        }

        public void setDirectFamily(Map<String, PersonModel> peopleMap, PersonModel personSelected, String selectedPersonID) {
                directFamily.clear();

                for(Map.Entry<String,PersonModel> entry: peopleMap.entrySet()){
                        PersonModel person = entry.getValue();

                        if(personSelected.getFatherID() != null || personSelected.getMotherID() != null) {
                                if (personSelected.getFatherID().equals(person.getPersonID())) {
                                        directFamily.put("father",person);
                                } else if (personSelected.getMotherID().equals(person.getPersonID())) {
                                        directFamily.put("mother",person);
                                }
                        }
                        if(personSelected.getSpouseID() != null) {
                                if (personSelected.getSpouseID().equals(person.getPersonID())) {
                                        directFamily.put("spouse",person);
                                }
                        }
                        if(person.getFatherID() != null || person.getMotherID() != null) {
                                if (selectedPersonID.equals(person.getFatherID()) || selectedPersonID.equals(person.getMotherID())) {
                                        directFamily.put("child",person);
                                }
                        }
                }
        }

        public PersonModel getOriginalSpouse() {
                return originalSpouse;
        }

        public void setOriginalSpouse(PersonModel originalSpouse) {
                this.originalSpouse = originalSpouse;
        }

        public Boolean getFamilyTreeLinesSwitch() {
                return familyTreeLinesSwitch;
        }

        public void setFamilyTreeLinesSwitch(Boolean familyTreeLinesSwitch) {
                this.familyTreeLinesSwitch = familyTreeLinesSwitch;
        }

        public Boolean getMothersSideSwitch() {
                return mothersSideSwitch;
        }

        public void setMothersSideSwitch(Boolean mothersSideSwitch) {
                this.mothersSideSwitch = mothersSideSwitch;
        }

        public Boolean getFathersSideSwitch() {
                return fathersSideSwitch;
        }

        public void setFathersSideSwitch(Boolean fathersSideSwitch) {
                this.fathersSideSwitch = fathersSideSwitch;
        }

        public Boolean getLifeStoryLinesSwitch() {
                return lifeStoryLinesSwitch;
        }

        public void setLifeStoryLinesSwitch(Boolean lifeStoryLinesSwitch) {
                this.lifeStoryLinesSwitch = lifeStoryLinesSwitch;
        }

        public HashMap<String, PersonModel> getPaternalAncestors() {
                return paternalAncestors;
        }

        public void setPaternalAncestors(HashMap<String, PersonModel> paternalAncestors) {
                this.paternalAncestors = paternalAncestors;
        }

        public HashMap<String, PersonModel> getMaternalAncestors() {
                return maternalAncestors;
        }

        public void setMaternalAncestors(HashMap<String, PersonModel> maternalAncestors) {
                this.maternalAncestors = maternalAncestors;
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
                if(!maleFiltered || !femaleFiltered || !mothersSideSwitch || !fathersSideSwitch) {
                        filteredPeople = new HashMap<>();
                        if (!femaleFiltered) {
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
                        if (!maleFiltered) {
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
                        if (!fathersSideSwitch) {
                                Map<String, PersonModel> maternalOnly = getMaternalAncestors();
                                for (Map.Entry<String, PersonModel> entry : maternalOnly.entrySet()) {
                                        PersonModel person = entry.getValue();
                                        String personKey = entry.getKey();
                                        if (person != null && personKey != null) {
                                                filteredPeople.put(personKey, person);
                                        }
                                }
                        }
                        if (!mothersSideSwitch) {
                                Map<String, PersonModel> parentalOnly = getPaternalAncestors();
                                for (Map.Entry<String, PersonModel> entry : parentalOnly.entrySet()) {
                                        PersonModel person = entry.getValue();
                                        String personKey = entry.getKey();
                                        if (person != null && personKey != null) {
                                                filteredPeople.put(personKey, person);
                                        }
                                }
                        }
                        if(!femaleFiltered && !maleFiltered){
                                filteredPeople.clear();
                        }
                        return filteredPeople;
                }

                return people;
        }
        public Map<String, EventModel> getFilteredEvents() {
                if(!maleFiltered || !femaleFiltered || !mothersSideSwitch || !fathersSideSwitch) {
                        filteredEvents = new HashMap<>();
                        if (!femaleFiltered) {
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
                        if (!maleFiltered) {
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
                        if (!fathersSideSwitch) {
                                Map<String, PersonModel> maternalOnly = getMaternalAncestors();
                                for (Map.Entry<String, PersonModel> entry : maternalOnly.entrySet()) {
                                        PersonModel person = entry.getValue();
                                        String personKey = entry.getKey();
                                        if (person != null && personKey != null && personKey != null) {
                                                for (Map.Entry<String, EventModel> entry2 : events.entrySet()) {
                                                        EventModel event = entry2.getValue();
                                                        String eventKey = entry2.getKey();
                                                        if (person != null && eventKey != null && event != null) {
                                                                if (personKey.equals(event.getPersonID())) {
                                                                        filteredEvents.put(eventKey, event);
                                                                }
                                                        }
                                                }
                                        }
                                }

                        }

                        if (!mothersSideSwitch) {
                                Map<String, PersonModel> parentalOnly = getPaternalAncestors();

                                for (Map.Entry<String, PersonModel> entry : parentalOnly.entrySet()) {
                                        PersonModel person = entry.getValue();
                                        String personKey = entry.getKey();
                                        if (person != null && personKey != null && personKey != null) {
                                                for (Map.Entry<String, EventModel> entry2 : events.entrySet()) {
                                                        EventModel event = entry2.getValue();
                                                        String eventKey = entry2.getKey();
                                                        if (person != null && eventKey != null && event != null) {
                                                                if (personKey.equals(event.getPersonID())) {
                                                                        filteredEvents.put(eventKey, event);
                                                                }
                                                        }
                                                }
                                        }
                                }

                        }
                        if(!femaleFiltered && !maleFiltered){
                                filteredEvents.clear();
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

                maternalAncestors.put(originalUser.getPersonID(),originalUser);
                paternalAncestors.put(originalUser.getPersonID(),originalUser);
                maternalAncestors.put(originalSpouse.getPersonID(),originalSpouse);
                paternalAncestors.put(originalSpouse.getPersonID(),originalSpouse);

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

        public List<EventModel> findEventsUser(PersonModel person){
                List<EventModel> personEvents = new ArrayList<EventModel>();
                String personID = person.getPersonID();
                for(Map.Entry<String,EventModel> entry: events.entrySet()){
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

        public List<EventModel> getSearchedEvents(String givenText){
                String text = givenText.toLowerCase();
                List<EventModel> searchedEvents = new ArrayList<EventModel>();
                for(Map.Entry<String,EventModel> entry: events.entrySet()){
                        EventModel event = entry.getValue();
                        StringBuilder sentence = new StringBuilder();
                        sentence.append(event.getCountry().toString().toLowerCase());
                        sentence.append(event.getCity().toString().toLowerCase());
                        sentence.append(event.getEventType().toString().toLowerCase());
                        sentence.append(event.getEventType().toString().toLowerCase());
                        sentence.append(event.getYear().toString().toLowerCase());
                        String sentence2 = sentence.toString();
                        if(sentence2.contains(text)){
                                searchedEvents.add(event);
                        }
                }
                return searchedEvents;
        }


        public List<PersonModel> getSearchedPeople(String givenText){
                String text = givenText.toLowerCase();
                List<PersonModel> searchedPeople = new ArrayList<PersonModel>();
                for(Map.Entry<String, PersonModel> entry: people.entrySet()){
                        PersonModel person = entry.getValue();
                        String sentence = person.getFirstName() + " " + person.getLastName();
                        sentence.toLowerCase();
                        if(person.getFirstName().toLowerCase().contains(text) || person.getLastName().toString().toLowerCase().contains(text)){
                                searchedPeople.add(person);
                        }
                }
                return searchedPeople;
        }




}
