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
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "toadd_exhibits")
public class ToAddExhibits {

    @NonNull
    public String id;
    public String itemType;
    public List<String> tags;

    //Constructor for each exhibit in json
    ToAddExhibits(@NonNull String id, String itemType,List<String> tags){
        this.id=id;
        this.itemType=itemType;
        this.tags=tags;
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
                "id='" + id + '\'' +
                ", itemType='" + itemType + '\'' +
                ", tags=" + tags +
                '}';
    }
}
