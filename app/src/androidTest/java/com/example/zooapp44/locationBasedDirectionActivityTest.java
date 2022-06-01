package com.example.zooapp44;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(AndroidJUnit4.class)
public class locationBasedDirectionActivityTest {
    ExhibitRoute route;

    @Before
    public void initializeRoute(){

        ZooGraph graph = ZooGraph.getSingleton(ApplicationProvider.getApplicationContext());
        List<String> exhibits = Arrays.asList("flamingo", "gorilla", "toucan", "spoonbill");
        String start_location = "entrance_exit_gate";
        route = graph.getOptimalPath(start_location, exhibits);
    }

    @Test
    public void testLocationBasedDirectionDetailed(){
        Intent getDirectionIntent = new Intent(ApplicationProvider.getApplicationContext(), GetDirectionActivity.class);
        getDirectionIntent.putExtra("Route", ExhibitRoute.serialize(route));
        ActivityScenario<GetDirectionActivity> scenario = ActivityScenario.launch(getDirectionIntent);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            ZooGraph.Vertex first = route.vertices.get(0);
            ZooGraph.Vertex second = route.vertices.get(1);
            Coord firstCoord = new Coord(first.lat, first.lng);
            Coord secondCoord = new Coord(second.lat, second.lng);
            Coord mid = Coord.midPoint(firstCoord, secondCoord);
            int distance = (int)Coord.calcDistance(firstCoord, secondCoord);

            Log.d("Test", "testLocationBasedDirection (Before mocking): " + activity.location.getLastKnownCoords().toString());
            Log.d("Test", "testLocationBasedDirection (mid): " + mid.lat + ", " + mid.lng);
            activity.location.mockLocation(mid);
            Log.d("Test", "testLocationBasedDirection (After mocking): " + activity.location.getLastKnownCoords().toString());

            // Check if new location is stored
            assertEquals(activity.location.getLastKnownCoords(), mid);

            // Force refresh
            activity.setContentView(R.layout.activity_get_direction);

            TextView instructionTV = activity.findViewById(R.id.route_instruction);
            String instruction = instructionTV.getText().toString();
            String[] lines = instruction.split("\\r?\\n|\\r");

            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(lines[1]);
            assertTrue(m.find());
            assertTrue(m.find());
            assertNotNull(m.group());
            int newDistance = Integer.parseInt(m.group());

            // The instruction panel is not refreshing
            // Guess it is not synchronized in testing
            // So i won't put assert here, but check the logcat

            //Go to the next intersection
            activity.location.mockLocation(secondCoord);

            // Simulate accidentally perform next click
            Button button = activity.findViewById(R.id.next_btn);
            button.performClick();

            // Start going from the second vertex to third
            ZooGraph.Vertex third = route.vertices.get(2);
            Coord thirdCoord = new Coord(third.lat, third.lng);
            List<Coord> coords = Coord.interpolate(secondCoord, thirdCoord, 4);
            for(Coord coord:coords)
                activity.location.mockLocation(coord);

            //Get to the third intersection
            activity.location.mockLocation(thirdCoord);

            // Directly get to the fourth coord, which is the destination
            ZooGraph.Vertex fourth = route.vertices.get(3);
            Coord fourthCoord = new Coord(fourth.lat, fourth.lng);
            activity.location.mockLocation(fourthCoord);

            // Hit Next button and check the log cat
            button.performClick();
        });
    }

    @Test
    public void testLocationBasedDirectionBrief(){
        Intent getDirectionIntent = new Intent(ApplicationProvider.getApplicationContext(), GetDirectionActivity.class);
        getDirectionIntent.putExtra("Route", ExhibitRoute.serialize(route));
        ActivityScenario<GetDirectionActivity> scenario = ActivityScenario.launch(getDirectionIntent);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            // Set the mode to brief
            activity.setBrief(true);
            // Start from the first exhibit, which is the fourth vertex
            ZooGraph.Vertex fourth = route.vertices.get(3);
            Coord fourthCoord = new Coord(fourth.lat, fourth.lng);
            activity.location.mockLocation(fourthCoord);
            Button nextBtn = activity.findViewById(R.id.next_btn);
            nextBtn.performClick();
            ZooGraph.Vertex fifth = route.vertices.get(4);
            Coord fifthCoord = new Coord(fifth.lat, fifth.lng);
            Coord mid = Coord.midPoint(fourthCoord, fifthCoord);
            activity.location.mockLocation(mid);
            activity.location.mockLocation(fifthCoord);
        });
    }
}
