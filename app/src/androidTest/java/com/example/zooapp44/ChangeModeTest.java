package com.example.zooapp44;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ChangeModeTest {

    ExhibitRoute route;

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void initializeGraph(){
        ZooGraph graph = ZooGraph.getSingleton(ApplicationProvider.getApplicationContext());
        List<String> exhibits = Arrays.asList("flamingo", "gorilla", "toucan", "spoonbill");
        String start_location = "entrance_exit_gate";
        route = graph.getOptimalPath(start_location, exhibits);
    }

    @Test
    public void briefAsDefault(){
        Intent getDirectionIntent = new Intent(ApplicationProvider.getApplicationContext(), GetDirectionActivity.class);
        getDirectionIntent.putExtra("Route", ExhibitRoute.serialize(route));
        ActivityScenario<GetDirectionActivity> scenario = ActivityScenario.launch(getDirectionIntent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);


        scenario.onActivity(activity -> {

            for(int i = 0; i < route.getSize(); i++){
                TextView instruction = activity.findViewById(R.id.route_instruction);
                assertEquals(instruction.getText().toString(),
                        route.getBriefInstruction(i));

                Button next = activity.findViewById(R.id.next_btn);
                next.performClick();
            }
        });
    }


    @Test
    public void b2d(){
        Intent getDirectionIntent = new Intent(ApplicationProvider.getApplicationContext(), GetDirectionActivity.class);
        getDirectionIntent.putExtra("Route", ExhibitRoute.serialize(route));
        ActivityScenario<GetDirectionActivity> scenario = ActivityScenario.launch(getDirectionIntent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);


        scenario.onActivity(activity -> {
            Button setting = activity.findViewById(R.id.Setting_btn);

            Button next = activity.findViewById(R.id.next_btn);

            for(int i = 0; i < route.getSize(); i++){
                setting.performClick();

                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.popup_window,
                        (ViewGroup) activity.findViewById(R.id.change_mode_popup));

                Button detail_btn = (Button) layout.findViewById(R.id.detail_btn);

                detail_btn.performClick();

                TextView instruction = activity.findViewById(R.id.route_instruction);
                assertEquals(instruction.getText().toString(),
                        route.getDetailedInstruction(i));

                // close settings window and go to the next exhibit
                setting.performClick();
                next.performClick();
            }
        });
    }

    @Test
    public void d2b(){
        Intent getDirectionIntent = new Intent(ApplicationProvider.getApplicationContext(), GetDirectionActivity.class);
        getDirectionIntent.putExtra("Route", ExhibitRoute.serialize(route));
        ActivityScenario<GetDirectionActivity> scenario = ActivityScenario.launch(getDirectionIntent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);


        scenario.onActivity(activity -> {
            Button setting = activity.findViewById(R.id.Setting_btn);

            Button next = activity.findViewById(R.id.next_btn);

            for(int i = 0; i < route.getSize(); i++){
                setting.performClick();

                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.popup_window,
                        (ViewGroup) activity.findViewById(R.id.change_mode_popup));

                Button detail_btn = (Button) layout.findViewById(R.id.detail_btn);
                Button brief_btn = (Button) layout.findViewById(R.id.brief_btn);

                detail_btn.performClick();
                brief_btn.performClick();

                TextView instruction = activity.findViewById(R.id.route_instruction);
                assertEquals(instruction.getText().toString(),
                        route.getBriefInstruction(i));

                // close settings window and go to the next exhibit
                setting.performClick();
                next.performClick();
            }
        });
    }


    @Test
    public void buttonsAppear() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.plan_btn), withText("PLAN"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.home_button), withText("HOME"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(androidx.appcompat.R.id.search_button), withContentDescription("Search"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_bar),
                                        childAtPosition(
                                                withId(R.id.search_view),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("mammal"), closeSoftKeyboard());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text), withText("mammal"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete2.perform(pressImeActionButton());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.chosen),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        0),
                                0),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.chosen),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        1),
                                0),
                        isDisplayed()));
        materialCheckBox2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.plan_btn), withText("PLAN"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.direction_button), withText("Get Direction!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.Setting_btn), withText("SETTINGS"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.detail_btn), withText("Detail"),
                        withParent(allOf(withId(R.id.change_mode_popup),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.brief_btn), withText("Brief"),
                        withParent(allOf(withId(R.id.change_mode_popup),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}
