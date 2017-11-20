package com.piedpiper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.piedpiper.SightingsMapActivity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pranavbokey on 11/15/17.
 */

public class UpdateMapTest {
    SightingsMapActivity testClass;
    private FirebaseAuth auth;
    private Query snap;
    public static final List<RatSighting> sightingsList = new LinkedList<>();

    @Before
    public void setup() throws Exception {
        testClass = new SightingsMapActivity();
        MainActivity main = new MainActivity();
    }

    //This test checks if the list that is created by a date range is not bigger than the original list of rat sightings
    @Test
    public void updateMapTest() throws Exception {
        List<RatSighting> compare = testClass.getSightings();
        List<RatSighting> original = MainActivity.sightingsList;
        assertTrue("The list is at least the same size as the original list", compare.size() <= original.size());
    }

    @Test
    public void testStartDate() throws Exception {
        Date start = new Date(2015 - 1900, 11, 03);
        testClass.setStart(start);
        Date end = new Date(2015 - 1900, 11, 20);
        testClass.setEnd(end);
        List<RatSighting> compare = testClass.getSightings();
        assertTrue("The date range resulted in the correct sightings", compare.size() == 0);
    }
}
