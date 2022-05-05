package com.example.zooapp44;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ExhibitRoute {
    List<String> exhibits;
    //List<String> Distance;
    List<ZooGraph.Edge> edges;
    List<ZooGraph.Vertex> vertices;
    List<Double> weight;

    /*
    public ExhibitRoute(List<String> exhibits, List<String> Distance){
        this.exhibits = exhibits;
        this.Distance = Distance;
    }

    public ExhibitRoute() {
        this.exhibits = new ArrayList<>();
        this.Distance = new ArrayList<>();
    }
    */


    /**
     *
     * @param vertices
     * @param edges
     * @param weight
     * @param exhibit
     */

    public ExhibitRoute(List<ZooGraph.Vertex> vertices,List<ZooGraph.Edge> edges,List<Double> weight,List<String> exhibit){
        this.edges=edges;
        this.exhibits=exhibit;
        this.vertices=vertices;
        this.weight=weight;
    }

    /**
     *
     * @param i index of exhibit that is to be returned
     * @return exhibit by index
     */
    public String getExhibit(int i){
        return exhibits.get(i);
    }

    public String getDistance(int i){
        return weight.get(i).toString();
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

    @Override
    public String toString() {
        return "ExhibitRoute{" +
                "exhibits=" + exhibits +
                ", edges=" + edges +
                ", vertices=" + vertices +
                ", weight=" + weight +
                '}';
    }
}
