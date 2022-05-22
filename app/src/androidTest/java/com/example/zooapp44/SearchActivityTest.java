package com.example.zooapp44;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

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
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    private ToAddDatabase testDb;
    private ToAddExhibitDao exhibitDao;

    private static void forceLayout(RecyclerView recyclerView){
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0, 0, 1080, 2280);
    }

    @Before
    public void resetDb(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ToAddDatabase.class).allowMainThreadQueries().build();
        ToAddDatabase.injectTestDatabase(testDb);

        List<ToAddExhibits> exhibits = ToAddExhibits.loadJSON(context, "sample_node_info.json");
        exhibitDao = testDb.toAddExhibitDao();
        exhibitDao.insertAll(exhibits);
    }

    @Test
    public void searchBarTest(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            SearchView searchView = activity.findViewById(R.id.search_view);
            searchView.requestFocus();
            searchView.setQuery("gor", true);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<ToAddExhibits> exhibits = exhibitDao.getAll();
            List<String> filtered = new ArrayList<>();

            for(ToAddExhibits exhibit:exhibits){
                if(exhibit.tags.contains("gorilla"))
                    filtered.add(exhibit.id);
            }


            RecyclerView recyclerView = activity.recyclerView;
            forceLayout(recyclerView);
            for(int i = 0; i < filtered.size(); i++){
                RecyclerView.ViewHolder currentVH = recyclerView.findViewHolderForAdapterPosition(i);
                assertNotNull(currentVH);

                TextView name = currentVH.itemView.findViewById(R.id.exhibits);
                assert(filtered.contains(name.getText().toString()));
            }
        });
    }

    @Test
    public void exhibitSelectTest(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            SearchView searchView = activity.findViewById(R.id.search_view);
            searchView.requestFocus();
            searchView.setQuery("mammal", true);

            List<ToAddExhibits> exhibits = exhibitDao.getAll();
            List<String> filtered = new ArrayList<>();

            for(ToAddExhibits exhibit:exhibits){
                if(exhibit.tags.contains("mammal"))
                    filtered.add(exhibit.id);
            }

            RecyclerView recyclerView = activity.recyclerView;
            forceLayout(recyclerView);
            for(int i = 0; i < filtered.size(); i++){
                RecyclerView.ViewHolder currentVH = recyclerView.findViewHolderForAdapterPosition(i);

                assertNotNull(currentVH);

                TextView name = currentVH.itemView.findViewById(R.id.exhibits);
                assert(filtered.contains(name.getText().toString()));

                CheckBox checkBox = currentVH.itemView.findViewById(R.id.chosen);
                checkBox.performClick();
            }

            exhibits = exhibitDao.getAll();

            for(ToAddExhibits exhibit:exhibits){
                if(exhibit.tags.contains("mammal")){
                    assertTrue(exhibit.selected);
                }
            }
        });
    }
}
