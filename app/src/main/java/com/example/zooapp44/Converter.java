package com.example.zooapp44;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    @TypeConverter
    public static List<String> fromString(String value){
        Type ListType = new TypeToken<ArrayList<String>>(){}.getType();
        return new Gson().fromJson(value, ListType);
    }

    @TypeConverter
    public static String fromArrayList(List<String> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

