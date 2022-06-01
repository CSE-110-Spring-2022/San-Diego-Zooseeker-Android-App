package com.example.zooapp44;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@RunWith(AndroidJUnit4.class)
public class LocationTest {
    /**
    @Test
    public void checkOneLocation(){
        int i=0;
        Stack<Coord> checker=new Stack<>();
        LocationModel location = null;
        checker.push(new Coord(32.7475300638514,-117.17681064859705));
        checker.push(new Coord(32.73453,-117.1));
        checker.push(new Coord(32.73453,-117.2));


        LocationModel finalLocation = location;
        final Observer<Coord> observe = new Observer<Coord>() {
            @Override
            public void onChanged(@Nullable Coord coord) {

                assert(finalLocation.getLastKnownCoords() == checker.pop());
            }
        };
        location= new LocationModel(observe);
        location.giveMutable().observe((LifecycleOwner) this,observe);


        Context context = ApplicationProvider.getApplicationContext();
        List<MockLocation> mock= MockLocation.loadMockJSON(context,"mock_route1.json");
        List<Coord> coordList= new ArrayList<>();
        List<Double> timeList= new ArrayList<>();
        for(var object: mock){
            object.change();
            coordList.add(object.mock);
            timeList.add(object.time);
        }
        location.mockRoute(coordList,timeList);
    }
    **/
    @Test
    public void TestingInsertLocation(){
        int i=0;
        Stack<Coord> checker=new Stack<>();
        LocationModel location = null;
        checker.push(new Coord(32.7475300638514,-117.17681064859705));
        checker.push(new Coord(32.73453,-117.1));
        checker.push(new Coord(32.73453,-117.2));
        Coord one = new Coord(32.73453,-117.2);
        Coord two = new Coord(32.73453,-117.1);
        Coord three = new Coord(32.7475300638514,-117.17681064859705);
        Observer<Coord> o = new Observer<Coord>() {
            @Override
            public void onChanged(Coord coord) {

            }
        };
        location = new LocationModel(o);
        location.giveMutable().setValue(one);
        Coord out = checker.pop();
        Coord live =location.getLastKnownCoords();
        assert(live.equals(out));

        location.giveMutable().setValue(two);
         out = checker.pop();
         live =location.getLastKnownCoords();
        assert(live.equals(out));

        location.giveMutable().setValue(three);
         out = checker.pop();
         live =location.getLastKnownCoords();
        assert(live.equals(out));
    }

}
