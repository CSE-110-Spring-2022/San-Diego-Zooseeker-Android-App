package com.example.zooapp44;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Rule;
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
        List<String> exhibits = Arrays.asList("Tiger", "Bear", "Dog", "Elephant");
        List<String> distance = Arrays.asList("300ft", "500ft", "200ft", "100ft");
        intent.putExtra("Route", ExhibitRoute.serialize(new ExhibitRoute(exhibits, distance)));

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
