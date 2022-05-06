package com.example.zooapp44;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class OpenExhibitListActivityTest {

    // I am still confused about
    // how to implement a test for putting extras

//    static Intent intent;
//    static {
//        intent = new Intent(ApplicationProvider.getApplicationContext(), OpenExhibitListActivity.class);
//        List<String> exhibits = Arrays.asList("Tiger", "Bear", "Dog", "Elephant");
//        List<String> distance = Arrays.asList("300ft", "500ft", "200ft", "100ft");
//        ExhibitRoute route = new ExhibitRoute(exhibits, distance);
//    }
//
//    @Rule
//    public ActivityScenarioRule<OpenExhibitListActivity> activityScenarioRule = new ActivityScenarioRule<>(intent);

//    @Rule
//    public IntentsTestRule<OpenExhibitListActivity> intentsTestRule = new IntentsTestRule<>(OpenExhibitListActivity.class);

    @Test
    public void testOpenExhibitListActivityNoneEmptyList(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), OpenExhibitListActivity.class);
        ZooGraph graph = ZooGraph.getSingleton(ApplicationProvider.getApplicationContext());

        List<ZooGraph.Vertex> vertices =
                                Arrays.asList(graph.getVertex("entrance gate", ZooGraph.Kind.GATE, true),
                                                graph.getVertex("tiger", ZooGraph.Kind.EXHIBIT, true),
                                                graph.getVertex("intersaction", ZooGraph.Kind.INTERSECTION, true),
                                                graph.getVertex("bird", ZooGraph.Kind.EXHIBIT, false),
                                                graph.getVertex("lion", ZooGraph.Kind.EXHIBIT, true));
        List<ZooGraph.Edge> edges = Arrays.asList(graph.eInfo.get("edge-0"), graph.eInfo.get("edge-1"), graph.eInfo.get("edge-2"), graph.eInfo.get("edge-3"));
        List<String> distance = Arrays.asList("300ft", "500ft");
        List<String> exhibits = Arrays.asList("tiger", "lion");
        intent.putExtra("Route", ExhibitRoute.serialize(new ExhibitRoute(vertices, edges, distance, exhibits)));

        ActivityScenario<OpenExhibitListActivity> scenario = ActivityScenario.launch(intent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;
            for(int i = 0; i < exhibits.size(); i++){
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                assertNotNull(viewHolder);

                TextView animalNameTV = viewHolder.itemView.findViewById(R.id.animal_name);
                TextView distanceTV = viewHolder.itemView.findViewById(R.id.distance);

                assertEquals(animalNameTV.getText().toString(), exhibits.get(i));
                assertEquals(distanceTV.getText().toString(), distance.get(i));
            }
        });
    }

}
