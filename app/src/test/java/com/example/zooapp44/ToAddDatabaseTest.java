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
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ToAddDatabaseTest {
    private ToAddExhibitDao dao;
    private ToAddDatabase db;

    @Before
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
        String[] one ={"Alligator"};

        String[] two ={"Alligator"};

        ToAddExhibits item1 = new ToAddExhibits("R301","exhibit","no" );
        ToAddExhibits item2 = new ToAddExhibits("R99","exhibit", "yes");

        long id1=dao.insert(item1);
        long id2=dao.insert(item2);

        assertNotEquals(id1,id2);
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


