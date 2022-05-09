package com.example.zooapp44;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
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

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Direction_Test {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void direction_Test() {
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
                allOf(withId(R.id.direction_button), withText("Get Direction!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.home_btn), withText("HOME"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton3.perform(click());



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
        searchAutoComplete.perform(replaceText("gorilla"), closeSoftKeyboard());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text), withText("gorilla"),
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

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(androidx.appcompat.R.id.search_close_btn), withContentDescription("Clear query"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction searchAutoComplete3 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete3.perform(click());





        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.plan_btn), withText("PLAN"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.direction_button), withText("Get Direction!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());



        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.next_btn), withText("NEXT"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton6.perform(click());


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
