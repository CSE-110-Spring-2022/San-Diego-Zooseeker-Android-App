package com.example.zooapp44;


import static org.junit.Assert.assertEquals;
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

import java.util.List;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class SelectedDisplayTest {
    ToAddDatabase testDb;
    ToAddExhibitDao toAddExhibitDao;
    List<ToAddExhibits> selected;

    private static void forceLayout(RecyclerView recyclerView){
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0, 0, 1080, 2280);
    }

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ToAddDatabase.class).allowMainThreadQueries().build();
        ToAddDatabase.injectTestDatabase(testDb);

        List<ToAddExhibits> todos = ToAddExhibits.loadJSON(context, "sample_node_info.json");
        // Rondomly select some exhibit
        for(ToAddExhibits exhibit:todos){
            if(Math.random() < 0.5)
                exhibit.selected = true;
        }
        toAddExhibitDao = testDb.toAddExhibitDao();;
        toAddExhibitDao.insertAll(todos);
        selected = toAddExhibitDao.getSelected();
    }

    @Test
    public void testSelectDisplay(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            TextView textView = activity.findViewById(R.id.selectedExhibits);
            assertNotNull(textView);
            String allExhibits = textView.getText().toString();
            for(ToAddExhibits exhibit:selected)
                assertTrue(allExhibits.contains((CharSequence) exhibit.name));

            // select/unselect gorilla
            SearchView searchView = activity.findViewById(R.id.search_view);
            searchView.requestFocus();
            searchView.setQuery("gorilla", true);

            RecyclerView recyclerView = activity.recyclerView;
            forceLayout(recyclerView);
            assertNotNull(recyclerView);

            boolean gorilla_exist = false;
            for(int i = 0; i < Objects.requireNonNull(recyclerView.getAdapter()).getItemCount(); i++){
                RecyclerView.ViewHolder currentVH = recyclerView.findViewHolderForAdapterPosition(i);
                assertNotNull(currentVH);
                CheckBox checkBox = currentVH.itemView.findViewById(R.id.chosen);
                if(checkBox.isChecked()){
                    gorilla_exist = false;
                }else{
                    gorilla_exist = true;
                }
                checkBox.performClick();
            }
            allExhibits = textView.getText().toString();
            assertEquals(allExhibits.contains("gorilla"), gorilla_exist);
        });
    }



}
