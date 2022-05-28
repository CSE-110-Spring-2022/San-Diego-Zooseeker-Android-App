package com.example.zooapp44;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MockLocation {
        public Coord mock;
        public double time;

    MockLocation(double lat, double lng, double time){
        this.mock=new Coord(lat,lng);
        this.time=time;
    }

    public static List<MockLocation> loadMockJSON(Context context, String path){
        try{
            InputStream input=context.getAssets().open(path);
            Reader reader=new InputStreamReader(input);
            Gson gson = new Gson();
            Type type= new TypeToken<List<MockLocation>>(){}.getType();
            return gson.fromJson(reader,type);
        } catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}