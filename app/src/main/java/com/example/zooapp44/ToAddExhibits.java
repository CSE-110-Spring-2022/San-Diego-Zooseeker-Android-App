package com.example.zooapp44;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity(tableName ="exhibit_items")
public class ToAddExhibits {


    @PrimaryKey(autoGenerate = true)
    public int identification;

    @NonNull
    public String id;
    public String kind;
    public boolean selected;
    public String name;
    public List<String> tags;
    public String group_id;
    public double lat;
    public double lng;




    //Constructor for each exhibit in json
    ToAddExhibits(String id, String kind, String name,String group_id,List<String> tags, double lat,double lng) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.selected = false;
        this.tags = tags;
        this.group_id=group_id;
        this.lat=lat;
        this.lng=lng;
    }

    public static List<ToAddExhibits> loadJSON(Context context, String path){
        try{
            InputStream input=context.getAssets().open(path);
            Reader reader=new InputStreamReader(input);
            Gson gson = new Gson();
            Type type= new TypeToken<List<ToAddExhibits>>(){}.getType();
            return gson.fromJson(reader,type);
        } catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "ToAddExhibits{" +
                "identification=" + identification +
                ", id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", selected=" + selected +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", group_id='" + group_id + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
