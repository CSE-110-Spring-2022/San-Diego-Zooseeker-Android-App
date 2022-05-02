package com.example.zooapp44;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZooGraph {


    public static Map<String, ZooGraph> loadJSON(Context context, String path){
        try{
            InputStream input=context.getAssets().open(path);
            Reader reader=new InputStreamReader(input);
            Gson gson = new Gson();
            Type type= new TypeToken<List<ToAddExhibits>>(){}.getType();
            List<ZooGraph> edges = gson.fromJson(reader,type);
            HashMap<String, ZooGraph> ret = new HashMap<>();
            for(ZooGraph edge:edges){
                ret.put(edge.id, edge);
            }
            return ret;
        } catch (IOException e){
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

}
