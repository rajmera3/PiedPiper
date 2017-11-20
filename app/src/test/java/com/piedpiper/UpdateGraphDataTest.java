package com.piedpiper;
        import org.junit.Before;
        import org.junit.Test;
        import java.util.Map;

        import com.google.firebase.database.Query;

        import java.util.Date;
        import java.util.LinkedList;
        import java.util.List;

        import static org.junit.Assert.assertTrue;
/**
 * Created by taabishkathawala on 11/20/17.
 */

public class UpdateGraphDataTest {
    private SightingsGraphActivity testClass;
    private Query snap;
    public static final List<RatSighting> sightingsList = new LinkedList<>();

    @Before
    public void setup() throws Exception {
        testClass = new SightingsGraphActivity();
        MainActivity main = new MainActivity();
    }

    //This test checks if the list that is created by a date range is not bigger than the original list of rat sightings
    @Test
    public void updateGraphDataTest() throws Exception {
        Map<String, Integer> compare = testClass.getSights();
        List<RatSighting> original = MainActivity.sightingsList;
        assertTrue("The list is at least the same size as the original list", compare.keySet().size() <= original.size());
    }

    @Test
    public void testStartDate() throws Exception {
        Date start = new Date(2015 - 1900, 11, 03);
        testClass.setStart(start);
        Date end = new Date(2015 - 1900, 11, 20);
        testClass.setEnd(end);
        Map<String, Integer> compare = testClass.getSights();
        assertTrue("The date range resulted in the correct sightings", compare.keySet().size() == 0);
    }

    @Test
    public void testDates() throws Exception {
        Date start = new Date(2016 - 1900, 11, 03);
        testClass.setStart(start);
        Date end = new Date(2015 - 1900, 5, 10);
        testClass.setEnd(end);
        Map<String, Integer> compare = testClass.getSights();
        assertTrue("The date range resulted in the correct sightings", compare.keySet().size() == 0);
    }


}
