package com.example.zooapp44;


import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RoutePlanningTest {
    List<ToAddExhibits> selected;
    ToAddDatabase db;
    ToAddExhibitDao dao;
    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        this.db= Room.inMemoryDatabaseBuilder(context, ToAddDatabase.class)
                        .allowMainThreadQueries()
                        .build();
        this.dao = db.toAddExhibitDao();

        List<ToAddExhibits> exhibitsList = ToAddExhibits.loadJSON(context, "sample_node_info.json");
        selected = new ArrayList<>();

        for(int i = 0; i < exhibitsList.size(); i++){
            if(exhibitsList.get(i).kind.equals("exhibit")){
                if(Math.random() < 0.5){
                    exhibitsList.get(i).selected = true;
                    selected.add(exhibitsList.get(i));
                }
            }
        }

        this.dao.insertAll(exhibitsList);
    }

    @Test
    public void routePlanning(){
        List<ToAddExhibits> selected = dao.getSelected();
        List<String> exhibitIds = new ArrayList<>();
        for(ToAddExhibits exhibit:selected){
            exhibitIds.add(exhibit.id);
        }
        ZooGraph graph = ZooGraph.getSingleton(ApplicationProvider.getApplicationContext());
        ExhibitRoute route = graph.getOptimalPath("entrance_exit_gate", exhibitIds);

        assert(route.exhibits.size() == exhibitIds.size());
//        System.out.println(route.vertices.size());
//        System.out.println(route.edges.size());
        assert(route.vertices.size() == route.edges.size() + 1);

        for(String exhibitId : exhibitIds){
            assert(route.exhibits.contains(exhibitId));
        }
        return;
    }
}
