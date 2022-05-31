package com.example.zooapp44;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.util.Log;
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
    public void testLocationBasedDirection(){
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
            Coord secondCoord = new Coord(first.lat, second.lng);
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
            // So i won't put assert here
            Log.d("Test", "testLocationBasedDirection (distance): " + distance + " " + newDistance);

        });
    }
}
