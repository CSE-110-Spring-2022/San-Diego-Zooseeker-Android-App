package com.example.zooapp44;

import android.util.Pair;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Objects;

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



    public String getBriefInstruction(int current) {
        String current_location;
        if(current == 0)
            current_location = vertices.get(0).id;  //set current location to entrance
        else current_location = exhibits.get(current - 1);

        String target_location;
        if(current == getSize())
            target_location = vertices.get(0).id;
        else target_location = exhibits.get(current);

        Pair<Double,String> length=this.findClosest(current_location,target_location, current_coord);
        if(length.second==null){
            //Replan
            return null;
        }
        return findPathBetween(length.second, target_location, length.first, false);
    }

    private String getCurrent_exhibit(int current) {
        String current_location;
        if(current == 0)
            current_location = vertices.get(0).id;  //set current location to entrance
        else current_location = exhibits.get(current - 1);
        return current_location;
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

        Pair<Double,String> length=this.findClosest(current_location,target_location, current_coord);
        if(length.second==null){
            //Replan
            return null;
        }
        return findPathBetween(length.second, target_location, length.first, true);
    }



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

    private String findPathBetween(String current_location, String target_location, double distance, boolean detailed) {
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
            for(int i = s; i < t; i++) {
                num++;
                ret += num + ". ";
                if (i == s) {
                    ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
                            (int)distance + "ft", edges.get(i).street, vertices.get(i).name, vertices.get(i + 1).name);
                } else {
                    ret += String.format("Walk %s meters along %s from %s to %s.\n\n",
                            edges.get(i).weight + "ft", edges.get(i).street, vertices.get(i).name, vertices.get(i + 1).name);
                }
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
                    weight_temp += (int)distance;
                }
                else {
                    weight_temp += (float) edges.get(i).weight;
                }
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

    /**
     *
     * @param vertexCoord the map from vertex id to Coord
     *                    this should be get from utils/VertexCoord
     * @param current index of current exhibits
     * @return whether the user is on route
     */
    public boolean onRoute(int current){
        String current_exhibit = getCurrent_exhibit(current);
        // Deleted +1
        String next_exhibit = getTarget_exhibit(current );

        int s = 0;
        while(!vertices.get(s).id.equals(current_exhibit))
            s++;
        int t = s;
        while(!vertices.get(t).id.equals(next_exhibit))
            t++;

        for(int i = s; i < t; i++){
            Coord st = new Coord(vertices.get(i).lat, vertices.get(i).lng);
            Coord ed = new Coord(vertices.get(i + 1).lat, vertices.get(i + 1).lng);

            double dis1 =Coord.calcDistance(st, current_coord);
            double dis2 = Coord.calcDistance(current_coord, ed);
            double tot_dis = Coord.calcDistance(st, ed);

            if(Math.abs(dis1 + dis2 - tot_dis) < 10)
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

    /**
     * This method returns the next node to travel to, along side the distance needed to travel to that node
     *
     * @param dot
     * @returns the distance to that node
     */
    public Pair<Double, String> findClosest(String current_location, String target_location, Coord dot) {
        double distance = 1000000;
        String nearest = null;
        int first=-1;
        int second=-1;
        Pair<Double, String> toReturn = new Pair<>(distance, nearest);
        for(int j=0;j<vertices.size();j++){
            if(vertices.get(j).id.equals(current_location)&&first==-1){
                first=j;
            }
            if(vertices.get(j).id.equals(target_location)&&second==-1){
                second=j;
            }
        }
        for (int i = first; i < second ; i++) {
            Coord tester1 = new Coord(vertices.get(i).lat, vertices.get(i).lng);
            Coord tester2 = new Coord(vertices.get(i + 1).lat, vertices.get(i + 1).lng);
            /*
            if(Coord.calcDistance(dot,tester1)<distance){
                distance=Coord.calcDistance(dot,tester1);
                nearest=vertices.get(i).id;
                toReturn=new Pair<>(distance,nearest);
            }
        }
        */
            //If current location is in the route between
            if (Coord.isInBetween(tester1, tester2, dot)) {
                nearest = vertices.get(i).id;
                distance=Coord.calcDistance(dot,tester2);
                toReturn= new Pair<>(distance,nearest);
            }
        }
        toReturn = new Pair<Double,String>(distance, nearest);
        return toReturn;
    }

    public Pair<Double, String> findAllClosest(Coord dot){
        double distance=10000000;
        String closest=null;
        for(int i=0;i<vertices.size();i++){
            Coord tester1 = new Coord(vertices.get(i).lat, vertices.get(i).lng);
            if(Coord.calcDistance(tester1,dot)<distance){
                distance=Coord.calcDistance(tester1,dot);
                closest=vertices.get(i).id;
            }
        }
        return new Pair<Double,String>(distance,closest);
    }

    public Coord getExhibitCoord(int current){
        int t = 0;
        while(!Objects.equals(vertices.get(t).id, exhibits.get(current + 1)))
            t++;
        return new Coord(vertices.get(t).lat, vertices.get(t).lng);
    }

}
