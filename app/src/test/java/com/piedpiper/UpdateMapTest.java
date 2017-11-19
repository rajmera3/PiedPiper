package com.piedpiper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.piedpiper.SightingsMapActivity;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pranavbokey on 11/15/17.
 */

public class UpdateMapTest {
    SightingsMapActivity testClass;

    @Before
    public void setup() throws Exception {
        testClass = new SightingsMapActivity();
        MainActivity main = new MainActivity();
        main.onStart();
//        testClass.onCreate(savedInstance);
//        getActivity();
    }

    @Test
    public void updateMapTest() throws Exception {
        List<RatSighting> compare = testClass.getSightings();
        List<RatSighting> orginal = MainActivity.sightingsList;
        assertTrue("The list is at least the same size as the original list", compare.size() < orginal.size());
    }
}
