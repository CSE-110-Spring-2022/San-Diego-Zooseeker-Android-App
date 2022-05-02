package com.example.zooapp44;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class ZooGraphTest {

    ZooGraph g;

    @Before
    public void createGraph(){
        Context context = ApplicationProvider.getApplicationContext();
        g = ZooGraph.getSingleton(context);
    }

    @Test
    public void testVInfo(){
        Map<String, ZooGraph.Vertex> vInfo = g.vInfo;

        assert(vInfo.containsKey("entrance_exit_gate"));
        assert(vInfo.containsKey("entrance_plaza"));
        assert(vInfo.containsKey("gorillas"));
        assert(vInfo.containsKey("gators"));
        assert(vInfo.containsKey("lions"));
    }
}
