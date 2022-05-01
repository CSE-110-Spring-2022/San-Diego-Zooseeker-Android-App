package com.example.zooapp44;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ExhibitRoute {
    List<String> exhibits;
    List<String> Distance;

    public ExhibitRoute(List<String> exhibits, List<String> Distance){
        this.exhibits = exhibits;
        this.Distance = Distance;
    }

    public ExhibitRoute() {
        this.exhibits = new ArrayList<>();
        this.Distance = new ArrayList<>();
    }

    public String getExhibit(int i){
        return exhibits.get(i);
    }

    public String getDistance(int i){
        return Distance.get(i);
    }

    public int getSize(){
        return exhibits.size();
    }

    static String serialize(ExhibitRoute route){
        Gson gson = new Gson();
        String myJson = gson.toJson(route);
        return myJson;
    }

    static ExhibitRoute deserialize(String s){
        Gson gson = new Gson();
        ExhibitRoute route = gson.fromJson(s, ExhibitRoute.class);
        return route;
    }
}
