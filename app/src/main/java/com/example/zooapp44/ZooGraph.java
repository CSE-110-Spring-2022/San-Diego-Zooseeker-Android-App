package com.example.zooapp44;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

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
    private static ZooGraph singleton = null;


    public static enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION
    }

    public Map<String, Vertex> vInfo;
    public Map<String, Edge> eInfo;
    public Graph<String, IdentifiedWeightedEdge> graph;

    private ZooGraph(){
        vInfo = new HashMap<>();
        eInfo = new HashMap<>();
    }

    public synchronized static ZooGraph getSingleton(Context context){
        if(singleton == null){
            singleton = makeZooGraph(context);
        }
        return singleton;
    }

    private static ZooGraph makeZooGraph(Context context) {
        ZooGraph zooGraph = new ZooGraph();
        List<Vertex> vertices = loadVertexJson(context, "sample_node_info.json");
        List<Edge> edges = loadEdgeJson(context, "sample_edge_info.json");

        for(Vertex v:vertices)
            zooGraph.vInfo.put(v.id, v);
        for(Edge e:edges) zooGraph.eInfo.put(e.id, e);

        try {
            zooGraph.graph = loadZooGraphJSON(context, "sample_zoo_graph.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zooGraph;
    }

    private static List<Edge> loadEdgeJson(Context context, String path) {
        try{
            InputStream input=context.getAssets().open(path);
            Reader reader=new InputStreamReader(input);
            Gson gson = new Gson();
            Type type= new TypeToken<List<Edge>>(){}.getType();
            return gson.fromJson(reader,type);
        } catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<Vertex> loadVertexJson(Context context, String path) {
        try{
            InputStream input=context.getAssets().open(path);
            Reader reader=new InputStreamReader(input);
            Gson gson = new Gson();
            Type type= new TypeToken<List<Vertex>>(){}.getType();
            return gson.fromJson(reader,type);
        } catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(Context context, String path) throws IOException {
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

        // On Android, you would use context.getAssets().open(path) here like in Lab 5.
        InputStream inputStream = context.getAssets().open(path);
        Reader reader = new InputStreamReader(inputStream);

        // And now we just import it!
        importer.importGraph(g, reader);

        return g;
    }

    public class Edge {
        public String id;
        public String street;
    }

    public class Vertex{
        public String id;
        public Kind kind;
        public String name;
        public List<String> tags;
    }

}
