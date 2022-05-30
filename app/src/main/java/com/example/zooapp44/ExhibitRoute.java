package com.example.zooapp44;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class ExhibitRoute {
    List<String> exhibits;
    //List<String> Distance;
    List<ZooGraph.Edge> edges;
    List<ZooGraph.Vertex> vertices;
    List<Double> weight;
    List<String> original;
    Coord current_coord;


    /**
     *
     * @param vertices
     * @param edges
     * @param weight
     * @param exhibit contains list of Strings that contain selected exhibits by name
     * @param original contains list of Strings that contain selected animals/ entrance by name
     */

    public ExhibitRoute(List<ZooGraph.Vertex> vertices,List<ZooGraph.Edge> edges,List<Double> weight,List<String> exhibit,List<String> original){
        this.edges=edges;
        this.exhibits=exhibit;
        this.vertices=vertices;
        this.weight=weight;
        this.original=original;
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

    public String getOriginal(int i){
        if(i == getSize()){
            return "entrance gate";
        }
        return original.get(i);
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
                ", original=" + original +
                '}';
    }



    public String getInstruction(int current) {
        String current_location;
        if(current == 0)
            current_location = vertices.get(0).id;  //set current location to entrance
        else current_location = exhibits.get(current - 1);

        String target_location;
        if(current == getSize())
            target_location = vertices.get(0).id;
        else target_location = exhibits.get(current);

        return findPathBetween(current_location, target_location);
    }

    private String getCurrent_exhibit(int current) {
        String current_location;
        if(current == 0)
            current_location = vertices.get(0).id;  //set current location to entrance
        else current_location = exhibits.get(current - 1);
        return current_location;
    }


    public String getBackInstruction(int current) {
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
        return findBackPathBetween(current_location, target_location);
    }

    private String findPathBetween(String current_location, String target_location) {
        int s = 0;
        while(!vertices.get(s).id.equals(current_location))
            s++;
        int t = s;
        while(!vertices.get(t).id.equals(target_location))
            t++;

        String ret = String.format("The shortest path from %s to %s is:\n\n", vertices.get(s).name, vertices.get(t).name);

        int num = 0;
        for(int i = s; i < t; i++){
            num++;
            ret += num + ". ";
            ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
                    edges.get(i).weight + "ft", edges.get(i).street, vertices.get(i).name, vertices.get(i + 1).name);
        }

        return ret;
    }

    /**
     *
     * @param vertexCoord the map from vertex id to Coord
     *                    this should be get from utils/VertexCoord
     * @param current index of current exhibits
     * @return whether the user is on route
     */
    public boolean onRoute(Map<String, Coord> vertexCoord, int current){
        String current_exhibit = getCurrent_exhibit(current);
        String next_exhibit = getTarget_exhibit(current + 1);

        int s = 0;
        while(!vertices.get(s).id.equals(current_exhibit))
            s++;
        int t = s;
        while(!vertices.get(t).id.equals(next_exhibit))
            t++;

        for(int i = s; i < t; i++){
            Coord st = vertexCoord.get(vertices.get(i).id);
            Coord ed = vertexCoord.get(vertices.get(i + 1).id);

            double dis1 =Coord.calcDistance(st, current_coord);
            double dis2 = Coord.calcDistance(current_coord, ed);
            double tot_dis = Coord.calcDistance(st, ed);

            if(Math.abs(dis1 + dis2 - tot_dis) < 100)
                return true;
        }
        return false;
    }

    private String getTarget_exhibit(int current) {
        String target_location;
        if(current == getSize())
            target_location = vertices.get(0).id;
        else target_location = exhibits.get(current);
        return target_location;
    }


    private String findBackPathBetween(String current_location, String target_location) {
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

        int num = 0;
        for(int i = s; i > t; i--){
            num++;
            ret += num + ". ";
            ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
                    edges.get(i-1).weight + "ft", edges.get(i-1).street, vertices.get(i).name, vertices.get(i - 1).name);
        }

        return ret;
    }


}
