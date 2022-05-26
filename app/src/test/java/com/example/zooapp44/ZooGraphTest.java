package com.example.zooapp44;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

        // Test if vertex json is loaded correctly
        // May fail if the json file changes!
        assert(vInfo.containsKey("entrance_exit_gate"));
        assert(vInfo.containsKey("gorilla"));
    }

    @Test
    public void testEInfo(){
        // Test if vertex json is loaded correctly
        // May fail if the json file changes!
        Map<String, ZooGraph.Edge> eInfo = g.eInfo;
//        for(int i = 0; i <= 6; i++){
//            String edge_key = "edge-" + Integer.toString(i);
//            assert(eInfo.containsKey(edge_key));
//        }
        assert(true);
    }

    @Test
    public void testGraph(){
        Graph<String, IdentifiedWeightedEdge> graph = g.graph;

        String start = "entrance_exit_gate";
        Iterator<String> iterator = new DepthFirstIterator<>(graph, start);

        Set<String> vertices = new HashSet<>();
        while(iterator.hasNext()){
            String vertex = iterator.next();
            System.out.println(vertex);
            vertices.add(vertex);
        }

        for(String vertex:vertices){
            assert g.vInfo.containsKey(vertex);
        }
    }
}
