package com.example.zooapp44;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
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
        // Our logic for graph is:
        // Exhibits = {A, B, C};
        // Vertex = {entrance, intersaction A, A, B, exhibit D, C};
        // Edge = {entrance -> intersaction, intersaction a -> a, A -> B, B -> exhibit_D, exhibit_d -> C}
        // double = {entrance -> A, A -> B, B -> C}

        ZooGraph graph = ZooGraph.getSingleton(ApplicationProvider.getApplicationContext());
        List<String> exhibits = Arrays.asList("gorillas", "lions", "elephant_odyssey", "arctic_foxes");
        String start_location = "entrance_exit_gate";
        route = graph.getOptimalPath(start_location, exhibits);
        // If create the graph by hand
        /*List<ZooGraph.Vertex> vertices =
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
         */
    }

    @Test
    public void testGetDirection(){
        Intent getDirectionIntent = new Intent(ApplicationProvider.getApplicationContext(), GetDirectionActivity.class);
        getDirectionIntent.putExtra("Route", ExhibitRoute.serialize(route));
        ActivityScenario<GetDirectionActivity> scenario = ActivityScenario.launch(getDirectionIntent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            for(int i = 0; i < route.getSize(); i++){
                TextView textView = activity.findViewById(R.id.current_animal);
                assertEquals(textView.getText().toString(), route.exhibits.get(i));

                textView = activity.findViewById(R.id.current_distance);
                assertEquals(textView.getText().toString(), route.getDistance(i, false));
                //check distance etc
                Button button = activity.findViewById(R.id.next_btn);
                button.performClick();
//                TextView animalTV = viewHolder.itemView.findViewById(R.id.current_animal);
//                TextView distanceTV = viewHolder.itemView.findViewById(R.id.current_distance);
//
//                assertEquals(animalTV.getText().toString(), route.exhibits.get(i));
//                assertEquals(distanceTV.getText().toString(), route.weight.get(i));
            }
        });
    }

}
