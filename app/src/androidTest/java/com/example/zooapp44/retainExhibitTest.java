package com.example.zooapp44;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class retainExhibitTest {

    private static void forceLayout(RecyclerView recyclerView){
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0, 0, 1080, 2280);
    }

    @Before
    public void resetDb(){
        Context context = ApplicationProvider.getApplicationContext();
        ToAddDatabase testDb = Room.inMemoryDatabaseBuilder(context, ToAddDatabase.class).allowMainThreadQueries().build();
        ToAddDatabase.injectTestDatabase(testDb);

        List<ToAddExhibits> exhibits = ToAddExhibits.loadJSON(context, "sample_node_info.json");
        ToAddExhibitDao exhibitDao = testDb.toAddExhibitDao();
        exhibitDao.insertAll(exhibits);
    }

    @Test
    public void testRetain(){
        selectExhibitsGorilla();

        ActivityScenario<MainActivity> new_scenario = ActivityScenario.launch(MainActivity.class);
        new_scenario.moveToState(Lifecycle.State.CREATED);
        new_scenario.moveToState(Lifecycle.State.STARTED);
        new_scenario.moveToState(Lifecycle.State.RESUMED);

        // Check if previously selected exhibits still exists
        new_scenario.onActivity(activity -> {
            SearchView searchView = activity.findViewById(R.id.search_view);
            searchView.requestFocus();
            searchView.setQuery("gorilla", true);

            RecyclerView recyclerView = activity.recyclerView;
            forceLayout(recyclerView);
            for(int i = 0; i < Objects.requireNonNull(recyclerView.getAdapter()).getItemCount(); i++){
                RecyclerView.ViewHolder currentVH = recyclerView.findViewHolderForAdapterPosition(i);
                assertNotNull(currentVH);
                CheckBox checkBox = currentVH.itemView.findViewById(R.id.chosen);
                assertTrue(checkBox.isChecked());
            }
        });
    }

    @Test
    public void testRetainRoute(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), GetDirectionActivity.class);
        ZooGraph graph = ZooGraph.getSingleton(ApplicationProvider.getApplicationContext());

        List<ZooGraph.Vertex> vertices =
                Arrays.asList(graph.getVertex("entrance_exit_gate", ZooGraph.Kind.GATE, true,"",300,300),
                        graph.getVertex("gorilla", ZooGraph.Kind.EXHIBIT, true,"",300,300),
                        graph.getVertex("intxn_front_treetops", ZooGraph.Kind.INTERSECTION, true,"",300,300),
                        graph.getVertex("hippo", ZooGraph.Kind.EXHIBIT, false,"",300,300),
                        graph.getVertex("koi", ZooGraph.Kind.EXHIBIT, true,"",300,300));
        List<ZooGraph.Edge> edges = Arrays.asList(graph.eInfo.get("gate_to_front"), graph.eInfo.get("gate_to_front"), graph.eInfo.get("gate_to_front"), graph.eInfo.get("gate_to_front"));
        List<Double> distance_double = Arrays.asList(300.0, 200.0);
        List<String> exhibits = Arrays.asList("gorilla", "koi");
        List<String> original =Arrays.asList("gorilla", "koi");
        intent.putExtra("Route", ExhibitRoute.serialize(new ExhibitRoute(vertices, edges, distance_double, exhibits,original)));
        ActivityScenario<GetDirectionActivity> scenario = ActivityScenario.launch(intent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);
        scenario.onActivity(activity -> {
            Button button = activity.findViewById(R.id.next_btn);
            SharedPreferences sharedPreferences = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            button.performClick();
            assertEquals(1, sharedPreferences.getInt("current_index", 0));
            sharedPreferences.edit().clear().apply();
        });
    }



    @Test
    public void testClear(){
        selectExhibitsGorilla();

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), OpenExhibitListActivity.class);

        ExhibitRoute exhibitRoute = new ExhibitRoute(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),new ArrayList<>());
        intent.putExtra("Route", ExhibitRoute.serialize(exhibitRoute));

        ActivityScenario<OpenExhibitListActivity> scenario = ActivityScenario.launch(intent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            Button clearButton = activity.findViewById(R.id.clear_button);
            clearButton.performClick();
        });
        scenario.moveToState(Lifecycle.State.DESTROYED);


        ActivityScenario<MainActivity> new_scenario = ActivityScenario.launch(MainActivity.class);
        new_scenario.moveToState(Lifecycle.State.CREATED);
        new_scenario.moveToState(Lifecycle.State.STARTED);
        new_scenario.moveToState(Lifecycle.State.RESUMED);

        // Check if previously selected exhibits is cleared
        new_scenario.onActivity(activity -> {
            SearchView searchView = activity.findViewById(R.id.search_view);
            searchView.requestFocus();
            searchView.setQuery("gorilla", true);

            RecyclerView recyclerView = activity.recyclerView;
            forceLayout(recyclerView);
            for(int i = 0; i < Objects.requireNonNull(recyclerView.getAdapter()).getItemCount(); i++){
                RecyclerView.ViewHolder currentVH = recyclerView.findViewHolderForAdapterPosition(i);
                assertNotNull(currentVH);
                CheckBox checkBox = currentVH.itemView.findViewById(R.id.chosen);
                assertFalse(checkBox.isChecked());
            }
        });
    }

    private void selectExhibitsGorilla() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            SearchView searchView = activity.findViewById(R.id.search_view);
            searchView.requestFocus();
            searchView.setQuery("gorilla", true);

            RecyclerView recyclerView = activity.recyclerView;
            forceLayout(recyclerView);
            for(int i = 0; i < Objects.requireNonNull(recyclerView.getAdapter()).getItemCount(); i++){
                RecyclerView.ViewHolder currentVH = recyclerView.findViewHolderForAdapterPosition(i);
                assertNotNull(currentVH);
                CheckBox checkBox = currentVH.itemView.findViewById(R.id.chosen);
                checkBox.performClick();
            }
        });
        // Shut down the activity and restart
        scenario.moveToState(Lifecycle.State.DESTROYED);
    }
}
