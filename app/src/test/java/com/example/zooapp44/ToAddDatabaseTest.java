package com.example.zooapp44;

import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ToAddDatabaseTest {
    private ToAddExhibitDao dao;
    private ToAddDatabase db;

    @Before
//    public void resetDatabase(){
//        Context context = ApplicationProvider.getApplicationContext();
//        testDb = Room.inMemoryDatabaseBuilder(context, ToAddDatabase.class).allowMainThreadQueries().build();
//        ToAddDatabase.injectTestDatabase(testDb);
//
//        List<ToAddExhibits> exhibits = ToAddExhibits.loadJSON(context, "sample_node_info.json");
//        dao = testDb.toAddExhibitDao();
//        dao.insertAll(exhibits);
//    }
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db= Room.inMemoryDatabaseBuilder(context, ToAddDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao=db.toAddExhibitDao();
    }

    @After
    public void closeBd() throws IOException{
        db.close();
    }

    @Test
    public void testInsert(){

        List<String> tags1 = Arrays.asList("tag1", "tag2", "tag3");
        List<String> tags2 = Arrays.asList("tag4", "tag5", "tag6");

        ToAddExhibits item1 = new ToAddExhibits("R301","exhibit","no" , tags1);
        ToAddExhibits item2 = new ToAddExhibits("R99","exhibit", "yes", tags2);

        long id1=dao.insert(item1);
        long id2=dao.insert(item2);

        assertNotEquals(id1,id2);
    }

    @Test
    public void testGetSelected(){
        List<String> tags1 = Arrays.asList("tag1", "tag2", "tag3");
        List<String> tags2 = Arrays.asList("tag4", "tag5", "tag6");

        ToAddExhibits item1 = new ToAddExhibits("R301","exhibit","no" , tags1);
        ToAddExhibits item2 = new ToAddExhibits("R99","exhibit", "yes", tags2);
        ToAddExhibits item3 = new ToAddExhibits("Longbow", "intersection", "no", tags2);

        item1.selected = false;
        item2.selected = true;
        item3.selected = true;

        dao.insert(item1);
        dao.insert(item2);
        dao.insert(item3);

        List<ToAddExhibits> selected = dao.getSelected();
        assert(selected.size() == 2);
        return;
    }
//    @Test
//    public void testInsert(){
//        String[] one ={"Alligator"};
//
//        String[] two ={"Alligator"};
//
//        ToAddExhibits item1 = new ToAddExhibits("R301","exhibit");
//        ToAddExhibits item2 = new ToAddExhibits("R99","exhibit");
//
//        long id1=dao.insert(item1);
//        long id2=dao.insert(item2);
//
//        assertNotEquals(id1,id2);
//    }
}


