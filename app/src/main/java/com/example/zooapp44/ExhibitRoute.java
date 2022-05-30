package com.example.zooapp44;

import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

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
        if(i == getSize()){
            return "entrance gate";
        }
        return exhibits.get(i);
    }

    /**
     *
     * @param i index of exhibit that is to be returned
     * @param flg whether do accumulate
     * @return exhibit by index
     */
    public String getDistance(int i, boolean flg){
        if(flg == false) return weight.get(i) + "ft";
        int sum = 0;
        for(int j = 0; j <= i; j++)
            sum += weight.get(j);
        return sum + "ft";
    }

    public String getBackDistance(int i, boolean flg){
        if(flg == false) return weight.get(i+1) + "ft";
        int sum = 0;
        for(int j = 0; j <= i; j++)
            sum += weight.get(j);
        return sum + "ft";
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



    public String getBriefInstruction(int current) {
        String current_location;
        if(current == 0)
            current_location = vertices.get(0).id;  //set current location to entrance
        else current_location = exhibits.get(current - 1);

        String target_location;
        if(current == getSize())
            target_location = vertices.get(0).id;
        else target_location = exhibits.get(current);

        return findPathBetween(current_location, target_location, false);
    }


    public String getDetailedInstruction(int current) {
        String current_location;
        if (current == 0)
            current_location = vertices.get(0).id;  //set current location to entrance
        else current_location = exhibits.get(current - 1);


        String target_location;
        if (current == getSize())
            target_location = vertices.get(0).id;
        else target_location = exhibits.get(current);

        return findPathBetween(current_location, target_location, true);
    }

//    public String getBackInstruction(int current) {
//        String current_location;
//        String target_location;
//
//        if(current == getSize()){
//            current_location = "entrance_exit_gate";
//        } else{
//            current_location = exhibits.get(current);
//        }
//
//        if(current == 0) {
//            target_location = vertices.get(0).id;   //set target location to entrance
//        } else{
//            target_location = exhibits.get(current - 1);
//        }
//        return findBackPathBetween(current_location, target_location, false);
//    }

    public String getBriefBackInstruction(int current) {
        String current_location;
        String target_location;

        if(current == getSize()){
            current_location = "entrance_exit_gate";
        } else{
            current_location = exhibits.get(current);
        }

        if(current == 0) {
            target_location = vertices.get(0).id;   //set target location to entrance
        } else{
            target_location = exhibits.get(current - 1);
        }
        return findBackPathBetween(current_location, target_location, false);
    }

    public String getDetailedBackInstruction(int current) {
        String current_location;
        String target_location;

        if(current == getSize()){
            current_location = "entrance_exit_gate";
        } else{
            current_location = exhibits.get(current);
        }

        if(current == 0) {
            target_location = vertices.get(0).id;   //set target location to entrance
        } else{
            target_location = exhibits.get(current - 1);
        }
        return findBackPathBetween(current_location, target_location, true);
    }

    private String findPathBetween(String current_location, String target_location, boolean detailed) {
        String ret = "";
        int s = 0;
        while (!vertices.get(s).id.equals(current_location))
            s++;
        int t = s;
        while (!vertices.get(t).id.equals(target_location))
            t++;

        ret = String.format("The shortest path from %s to %s is:\n\n", vertices.get(s).name, vertices.get(t).name);
        if (detailed) {
            int num = 0;
            for (int i = s; i < t; i++) {
                num++;
                ret += num + ". ";
                ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
                        edges.get(i).weight + "ft", edges.get(i).street, vertices.get(i).name, vertices.get(i + 1).name);
            }
        } else {
            int num = 0;
            float weight_temp = 0;
            String street = "";
            String vert_temp = "";
            for (int i = s; i < t; i++) {
                if (i == s) {
                    street = edges.get(i).street; //initialization
                    vert_temp = vertices.get(i).name;
                }
                weight_temp += (float)edges.get(i).weight;
                if (i != t-1 && edges.get(i).street.equals(edges.get(i+1).street)) {
                    //skip if street does not change
                    continue;
                }
                num++;
                ret += num + ". ";
                ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
                        weight_temp + "ft", street, vert_temp, vertices.get(i + 1).name);
                if (i!= t-1) {
                    weight_temp = 0;    //reset
                    street = edges.get(i + 1).street;
                    vert_temp = vertices.get(i + 1).name;
                }
            }
        }

        return ret;
    }

    private String findBackPathBetween(String current_location, String target_location, boolean detailed) {
        int s = 0;
        while (!vertices.get(s).id.equals(current_location))
                s++;
        int t = 0;
        while(!vertices.get(t).id.equals(target_location))
            t++;

        if(current_location == "entrance_exit_gate"){
            s = vertices.size()-1;
        }
        //s > t

        String ret = String.format("The shortest path from %s to %s is:\n\n", vertices.get(s).name, vertices.get(t).name);

//        int num = 0;
//        for(int i = s; i > t; i--){
//            num++;
//            ret += num + ". ";
//            ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
//                    edges.get(i-1).weight + "ft", edges.get(i-1).street, vertices.get(i).name, vertices.get(i - 1).name);
//        }

        if (detailed) {
            int num = 0;
            for (int i = s; i > t; i--) {
                num++;
                ret += num + ". ";
                ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
                        edges.get(i-1).weight + "ft", edges.get(i-1).street, vertices.get(i).name, vertices.get(i - 1).name);
            }
        } else {
            int num = 0;
            float weight_temp = 0;
            String street = "";
            String vert_temp = "";
            for (int i = s; i > t; i--) {
                if (i == s) {
                    street = edges.get(i-1).street; //initialization
                    vert_temp = vertices.get(i).name;
                }
                weight_temp += (float)edges.get(i-1).weight;
                if (i != t+1 && edges.get(i-1).street.equals(edges.get(i-2).street)) {
                    //skip if street does not change
                    continue;
                }
                num++;
                ret += num + ". ";
                ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
                        weight_temp + "ft", street, vert_temp, vertices.get(i - 1).name);
                if (i!= t+1) {
                    weight_temp = 0;    //reset
                    street = edges.get(i - 2).street;
                    vert_temp = vertices.get(i - 1).name;
                }
            }
        }


        return ret;
    }


}
