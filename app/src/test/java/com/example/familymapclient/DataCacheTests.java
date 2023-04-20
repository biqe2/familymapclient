package com.example.familymapclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.app.Person;
import android.provider.ContactsContract;

import com.example.familymapclient.DataCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.EventModel;
import Model.PersonModel;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

public class DataCacheTests {
    private DataCache data;
    private PersonModel originalSpouse;
    private PersonModel originalUser;
    private HashMap<String, PersonModel> paternalAncestors;
    private HashMap<String, PersonModel> maternalAncestors;
    private Map<String, PersonModel> people;
    private Map<String, EventModel> events;
    private String serverHost;
    private String serverPort;
    private ServerProxy serverProxy;
    private LoginRequest request;
    private LoginResponse response;

    private PersonModel child;
    private PersonModel father;
    private PersonModel mother;
    private PersonModel spouse;


    @Before
    public void setUP(){
        data = DataCache.getInstance();
        serverProxy = new ServerProxy();
        serverHost = "10.34.18.70";
        serverPort = "8080";
        originalSpouse = new PersonModel();
        originalUser = new PersonModel();
        paternalAncestors = new HashMap<>();
        maternalAncestors = new HashMap<>();
        people = new HashMap<>();
        events = new HashMap<>();


        request = new LoginRequest(
                "sheila","parker"
        );
        response = serverProxy.login(request,serverHost,serverPort);
        data.setLoginAuthtoken(response.getAuthtoken());
        PersonResponse personResponse = serverProxy.getPeople(response.getAuthtoken(), serverHost, serverPort);

        PersonModel[] personLists = personResponse.getData();
        data.setPeople(personLists);

        EventResponse eventResponse = serverProxy.getEvents(response.getAuthtoken(), serverHost, serverPort);
        EventModel[] eventLists = eventResponse.getData();
        data.setEvents(eventLists);

        PersonModel userPerson = data.getPeople().get(response.getPersonID());
        data.setOriginalUser(userPerson);
        people = data.getPeople();
        events = data.getEvents();
        originalUser = data.getOriginalUser();
    }

    @After
    public void tearDown(){
        data = null;
        serverProxy = null;
        serverHost = null;
        serverPort = null;
        originalSpouse = null;
        originalUser = null;
        paternalAncestors = null;
        maternalAncestors = null;
        child = null;
        father = null;
        spouse = null;
        mother = null;
    }
    @Test
    public void testFamilyRelationships(){
        data.setDirectFamily(people,originalUser,originalUser.getPersonID());

        child = data.getDirectFamily().get("child");
        father = data.getDirectFamily().get("father");
        mother = data.getDirectFamily().get("mother");
        spouse = data.getDirectFamily().get("spouse");

        assertNotNull(mother);
        assertNotNull(father);
        assertNotNull(spouse);
        assertNull(child);

        assertEquals("Betty", mother.getFirstName());
        assertEquals("Blaine", father.getFirstName());
        assertEquals("Davis", spouse.getFirstName());

    }

    @Test
    public void testFamilyRelationshipsFieled(){
        PersonModel fakePerson = new PersonModel("sl;dhf","aksdbfsj",
                "aksdbfsj","aksdbfsj","aksdbfsj","aksdbfsj","aksdbfsj","aksdbfsj");

        data.setDirectFamily(people,fakePerson,"thisisisnot a user");

        child = data.getDirectFamily().get("child");
        father = data.getDirectFamily().get("father");
        mother = data.getDirectFamily().get("mother");
        spouse = data.getDirectFamily().get("spouse");

        assertNull(mother);
        assertNull(father);
        assertNull(spouse);
        assertNull(child);

    }

    @Test
    public void testFilterEvents(){
        data.setMaleFiltered(false);
        Map<String, PersonModel> peopleFiltered = data.getFilteredPeople();
        Map<String, EventModel> eventsFiltered = data.getFilteredEvents();
        for (Map.Entry<String, PersonModel> entry : peopleFiltered.entrySet()){
            PersonModel femalePerson = entry.getValue();
            assertEquals("f", femalePerson.getGender());
            for (Map.Entry<String, EventModel> entry2 : eventsFiltered.entrySet()){
                EventModel femaleEvent = entry2.getValue();
                PersonModel personEvent = peopleFiltered.get(femaleEvent.getPersonID());
                assertNotNull(personEvent);
            }
        }

        data.setMaleFiltered(false);
        data.setFemaleFiltered(false);

        Map<String, EventModel> eventsFilteredTwo = data.getFilteredEvents();
        Boolean compareOne = eventsFilteredTwo.isEmpty();
        assertEquals(true,compareOne);

        data.setMaleFiltered(true);
        Map<String, PersonModel> peopleFilteredThird = data.getFilteredPeople();
        Map<String, EventModel> eventsFilteredThird = data.getFilteredEvents();
        for (Map.Entry<String, PersonModel> entry : peopleFilteredThird.entrySet()){
            PersonModel femalePerson = entry.getValue();
            assertEquals("m", femalePerson.getGender());
            for (Map.Entry<String, EventModel> entry2 : eventsFilteredThird.entrySet()){
                EventModel femaleEvent = entry2.getValue();
                PersonModel personEvent = peopleFilteredThird.get(femaleEvent.getPersonID());
                assertNotNull(personEvent);
            }
        }
    }

    @Test
    public void testFilterEventsFailed(){
        data.setMaleFiltered(false);
        Map<String, PersonModel> peopleFiltered = data.getFilteredPeople();
        Map<String, EventModel> eventsFiltered = data.getFilteredEvents();
        for (Map.Entry<String, PersonModel> entry : peopleFiltered.entrySet()){
            PersonModel femalePerson = entry.getValue();
            assertNotEquals("m", femalePerson.getGender());
            for (Map.Entry<String, EventModel> entry2 : eventsFiltered.entrySet()){
                EventModel femaleEvent = entry2.getValue();
                PersonModel personEvent = peopleFiltered.get(femaleEvent.getPersonID());
                assertNotNull(personEvent);
            }
        }

        data.setMaleFiltered(false);
        data.setFemaleFiltered(false);

        Map<String, EventModel> eventsFilteredTwo = data.getFilteredEvents();
        assertNotEquals(false,eventsFilteredTwo.isEmpty());

        data.setMaleFiltered(true);
        Map<String, PersonModel> peopleFilteredThird = data.getFilteredPeople();
        Map<String, EventModel> eventsFilteredThird = data.getFilteredEvents();
        for (Map.Entry<String, PersonModel> entry : peopleFilteredThird.entrySet()){
            PersonModel femalePerson = entry.getValue();
            assertNotEquals("f", femalePerson.getGender());
            for (Map.Entry<String, EventModel> entry2 : eventsFilteredThird.entrySet()){
                EventModel femaleEvent = entry2.getValue();
                PersonModel personEvent = peopleFilteredThird.get(femaleEvent.getPersonID());
                assertNotNull(personEvent);
            }
        }
    }

    @Test
    public void testChronologicalSorting(){
        List<EventModel> userEvents = new ArrayList<EventModel>();
        userEvents = data.findEventsUser(originalUser);
        Integer numberEvents = userEvents.size();
        EventModel birthEvent = userEvents.get(0);
        EventModel deathEvent = userEvents.get(numberEvents-1);

        assertNotNull(userEvents);
        assertNotNull(birthEvent);
        assertNotNull(deathEvent);
        assertEquals("birth", birthEvent.getEventType());
        assertEquals("death", deathEvent.getEventType());
    }

    @Test
    public void testChronologicalSortingFailed(){
        List<EventModel> userEvents = new ArrayList<EventModel>();
        userEvents = data.findEventsUser(originalUser);
        Integer numberEvents = userEvents.size();

        assertNotNull(userEvents);
        for(int i = 1; i < numberEvents;i++){
            assertNotEquals("birth",userEvents.get(i).getEventType());
        }

        for(int i = 0; i < numberEvents-1;i++){
            assertNotEquals("death",userEvents.get(i).getEventType());
        }
    }

    @Test
    public void testSearch(){
        String textOne = "death";
        String textTwo = "Death";
        String textThree = "DEATH";

        List<EventModel> eventsSelectedOne = data.getSearchedEvents(textOne);
        List<PersonModel> peopleSelectedOne = data.getSearchedPeople(textOne);

        List<EventModel> eventsSelectedTwo = data.getSearchedEvents(textTwo);
        List<PersonModel> peopleSelectedTwo = data.getSearchedPeople(textTwo);

        List<EventModel> eventsSelectedThree = data.getSearchedEvents(textThree);
        List<PersonModel> peopleSelectedThree = data.getSearchedPeople(textThree);
        assertNotNull(eventsSelectedOne);
        assertNotNull(peopleSelectedOne);
        assertNotNull(eventsSelectedTwo);
        assertNotNull(peopleSelectedTwo);
        assertNotNull(eventsSelectedThree);
        assertNotNull(peopleSelectedThree);

        assertEquals(eventsSelectedOne.size(),eventsSelectedTwo.size());
        assertEquals(eventsSelectedThree.size(),eventsSelectedTwo.size());
        assertEquals(eventsSelectedOne.size(),eventsSelectedThree.size());

        assertEquals(peopleSelectedOne.size(),peopleSelectedTwo.size());
        assertEquals(peopleSelectedTwo.size(),peopleSelectedThree.size());
        assertEquals(peopleSelectedOne.size(),peopleSelectedThree.size());
    }

    @Test
    public void testSearchFailed(){
        String textOne = "garbage";
        String textTwo = "GARBAGE";
        String textThree = "GaRage";

        List<EventModel> eventsSelectedOne = data.getSearchedEvents(textOne);
        List<PersonModel> peopleSelectedOne = data.getSearchedPeople(textOne);

        List<EventModel> eventsSelectedTwo = data.getSearchedEvents(textTwo);
        List<PersonModel> peopleSelectedTwo = data.getSearchedPeople(textTwo);

        List<EventModel> eventsSelectedThree = data.getSearchedEvents(textThree);
        List<PersonModel> peopleSelectedThree = data.getSearchedPeople(textThree);
        assertNotNull(eventsSelectedOne);
        assertNotNull(peopleSelectedOne);
        assertNotNull(eventsSelectedTwo);
        assertNotNull(peopleSelectedTwo);
        assertNotNull(eventsSelectedThree);
        assertNotNull(peopleSelectedThree);

        assertEquals(true, eventsSelectedOne.isEmpty());
        assertEquals(true, eventsSelectedThree.isEmpty());
        assertEquals(true,eventsSelectedThree.isEmpty());
    }





}
