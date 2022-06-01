package com.example.zooapp44;

import android.content.Context;

import com.example.zooapp44.utils.VertexCoord;

import java.util.List;
import java.util.Map;

public class Rerouter {

    public static ExhibitRoute Reroute(Coord coord, Context context, List<String> unvisited){
        Map<String, Coord> vertexCoord = VertexCoord.getVertexCoordMap(context);

        double min_d = 1e9;
        String nearestVertex = "";
        for(Map.Entry<String, Coord> entry:vertexCoord.entrySet()){
            if(Coord.calcDistance(entry.getValue(), coord) < min_d){
                nearestVertex = entry.getKey();
            }
        }

        ZooGraph zooGraph = ZooGraph.getSingleton(context);
        ExhibitRoute newRoute = zooGraph.getOptimalPath(nearestVertex, unvisited);
        newRoute.current_coord = vertexCoord.get(nearestVertex);

        return newRoute;
    }
}
