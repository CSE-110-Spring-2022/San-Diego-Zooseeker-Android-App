package com.example.zooapp44;


import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class GetDirectionActivityTest {

    ExhibitRoute route;

    @Before
    public void initializeGraph(){

        Exhibits = {A, B, C};
        Vertex = {entrance, intersaction A, A, B, exhibit D, C};
        Edge = {entrance -> intersaction, intersaction a -> a, A -> B, B -> exhibit_D, exhibit_d -> C}
        double = {entrance -> A, A -> B, B -> C}




        ZooGraph graph = ZooGraph.getSingleton(ApplicationProvider.getApplicationContext());
        ExhibitList = {....., ...., ...};
        Start = ....
        graph.getOptimalPath(Start, ExhibitLis)
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
        route = new ExhibitRoute(vertices, edges, distance_double, exhibits);
    }


    @Test
    public void testGetDirection(){
        Intent getDirectionIntent = new Intent(ApplicationProvider.getApplicationContext(), GetDirectionActivity.class);
        getDirectionIntent.putExtra("Route", ExhibitRoute.serialize(route));





    }

}
