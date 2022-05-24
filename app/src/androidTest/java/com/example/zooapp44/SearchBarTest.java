package com.example.zooapp44;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.view.View;

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

@RunWith(AndroidJUnit4.class)
public class SearchBarTest {
    ToAddDatabase testDb;
    ToAddExhibitDao toAddExhibitDao;

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ToAddDatabase.class).allowMainThreadQueries().build();
        ToAddDatabase.injectTestDatabase(testDb);

        List<ToAddExhibits> todos = ToAddExhibits.loadJSON(context, "demo_todos.json");
        toAddExhibitDao = testDb.toAddExhibitDao();;
        toAddExhibitDao.insertAll(todos);
    }

    @Test
    public void testCheckoffTodo(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            List<ToAddExhibits> beforeTodoList = toAddExhibitDao.getAll();

            RecyclerView recyclerView = activity.recyclerView;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);


            long id = firstVH.getItemId();

            View checkOffButton = firstVH.itemView.findViewById(R.id.chosen);
            checkOffButton.performClick();

            List<ToAddExhibits> afterTodoList = toAddExhibitDao.getAll();

            assertNotEquals(afterTodoList.get(0).selected, beforeTodoList.get(0).selected);

        });

    }
}
