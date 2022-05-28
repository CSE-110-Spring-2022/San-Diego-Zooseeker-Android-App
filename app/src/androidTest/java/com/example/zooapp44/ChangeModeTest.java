package com.example.zooapp44;


import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ChangeModeTest {

    ExhibitRoute route;

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

            // datail_btn in a popup, probably won't work this way
            Button detail_btn = activity.findViewById(R.id.detail_btn);
            Button next = activity.findViewById(R.id.next_btn);

            for(int i = 0; i < route.getSize(); i++){
                setting.performClick();
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

            // datail_btn and brief_btn in a popup, probably won't work this way
            Button detail_btn = activity.findViewById(R.id.detail_btn);
            Button brief_btn = activity.findViewById(R.id.brief.btn);
            Button next = activity.findViewById(R.id.next_btn);

            for(int i = 0; i < route.getSize(); i++){
                setting.performClick();
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

}
