package com.example.zooapp44;

import static org.junit.Assert.assertEquals;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class GetDirectionTest {
    ToAddDatabase db;
    ToAddExhibitDao dao;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ToAddDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.toAddExhibitDao();
    }

    @Test
    public void testStartFromEntrance(){
        // For user input, check if the route starts from entrance
        route = ExhibitRoute.deserialize(getIntent().getStringExtra("Route"));
        String start_location = route.vertices.get(0).id;
        assertEquals(start_location, "entrance_exit_gate");
    }

    @Test
    public void testEndWIthEntrance(){
        // For user input, check if the route starts from entrance
        route = ExhibitRoute.deserialize(getIntent().getStringExtra("Route"));
        String end_location = route.vertices.get(route.getSize()).id;
        assertEquals(end_location, "entrance_exit_gate");
    }

    ZooGraph graph = ZooGraph.getSingleton(ApplicationProvider.getApplicationContext());
    List<ZooGraph.Vertex> vertices =
            Arrays.asList(graph.getVertex("entrance_exit_gate", ZooGraph.Kind.GATE, true),
                    graph.getVertex("entrance-plaza", ZooGraph.Kind.INTERSECTION, true),
                    graph.getVertex("gorillas", ZooGraph.Kind.EXHIBIT, true),
                    graph.getVertex("lions", ZooGraph.Kind.EXHIBIT, false),
                    graph.getVertex("elephant_odyssey", ZooGraph.Kind.EXHIBIT, true));
    List<ZooGraph.Edge> edges = Arrays.asList(graph.eInfo.get("edge-0"), graph.eInfo.get("edge-1"), graph.eInfo.get("edge-2"), graph.eInfo.get("edge-3"));
    List<Double> distance_double = Arrays.asList(10.0, 200.0, 200.0, 200.0);
    // List<String> distance = Arrays.asList("300ft", "500ft");
    List<String> exhibits = Arrays.asList("gorillas", "lions", "elephant_odyssey");
    ExhibitRoute route = new ExhibitRoute(vertices, edges, distance_double, exhibits);

    @Test
    public void testDistance(){
        int current = 0;
        String current_location = route.vertices.get(0).id; // start from entrance
        String targer_location = route.vertices.get(4).id; // end with elephant
        String currentDistance = route.getDistance(current, false);
        int s = 0;
        while(!route.vertices.get(s).id.equals(current_location))
            s++;
        int t = s;
        while(!route.vertices.get(t).id.equals(targer_location))
            t++;
        double sumDistance = 0;
        for(int i = s; i < t; i++){
            sumDistance = sumDistance + route.edges.get(i).weight;
        }
        String sumDistanceString = String.valueOf(sumDistance) + "ft";
        assertEquals(sumDistanceString, currentDistance);
    }
}
