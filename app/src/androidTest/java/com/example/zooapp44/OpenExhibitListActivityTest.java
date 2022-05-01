package com.example.zooapp44;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
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

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), OpenExhibitListActivity.class);
        List<String> exhibits = Arrays.asList("Tiger", "Bear", "Dog", "Elephant");
        List<String> distance = Arrays.asList("300ft", "500ft", "200ft", "100ft");
        ExhibitRoute route = new ExhibitRoute(exhibits, distance);
    }

    @Rule
    public ActivityScenarioRule<OpenExhibitListActivity> activityScenarioRule = new ActivityScenarioRule<>(intent);

    @Test
    public void testOpenExhibitListActivityNoneEmptyList(){
    }

}
