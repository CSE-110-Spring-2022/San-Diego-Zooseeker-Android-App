package com.example.zooapp44.utils;

import android.content.Context;

import com.example.zooapp44.Coord;
import com.example.zooapp44.ToAddDatabase;
import com.example.zooapp44.ToAddExhibitDao;
import com.example.zooapp44.ToAddExhibits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VertexCoord {
    public static Map<String, Coord> getVertexCoordMap(Context context){
        ToAddDatabase db = ToAddDatabase.getSingleton(context);
        ToAddExhibitDao dao = db.toAddExhibitDao();
        List<ToAddExhibits> exhibits = dao.getAll();

        Map<String, Coord> ret = new HashMap<>();
        for(var exhibit:exhibits)
            ret.put(exhibit.id, new Coord(exhibit.lat, exhibit.lng));
        return ret;
    }
}
