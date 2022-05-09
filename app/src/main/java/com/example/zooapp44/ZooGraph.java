package com.example.zooapp44;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    // Code copied from CSE110 instructing team
    private static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(Context context, String path) throws IOException {
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
        public double weight;

        Edge(){ id = ""; street = "";}
        Edge(String id, String street){
            this.id = id; this.street = street;
        }
    }

    public Edge getEdge(String id, String street){
        return new Edge(id, street);
    }

    public class Vertex{
        public String id;
        public Kind kind;
        public String name;
        public List<String> tags;
        public boolean selected;

        Vertex(){}
        Vertex(String name, Kind kind, boolean selected){
            this.name = name;
            this.kind = kind;
            this.selected = selected;
        }
    }

    public Vertex getVertex(String name, Kind kind, boolean selected){
        return new Vertex(name, kind, selected);
    }

    /**
     *
     * @param start The starting entrance in form of string
     * @param exhibits All selected exhibits in a list of strings
     *
     * @return ExhibitRoute(List<Vertex>,List<Edges>,List<Double>weights from vertex to vertex,List<String>selected exhibits)
     */
    public ExhibitRoute getOptimalPath(String start, List<String> exhibits){

        DijkstraShortestPath<String,IdentifiedWeightedEdge> algorithm = new DijkstraShortestPath<>(graph);
        ShortestPathAlgorithm.SingleSourcePaths<String,IdentifiedWeightedEdge> allPaths=algorithm.getPaths(start);
        List<String> toRemove = new ArrayList<>(exhibits);

        double smallestWeight=0;
        //Weight from one vertex to another
        List<Double> weights=new ArrayList<>();

        //Used to hold edges that are in the optimal path
        List<IdentifiedWeightedEdge> shortestEdges=new ArrayList<>();

        //Used to hold vertices that are in the optimal path
        List<String> shortestVertex= new ArrayList<>();

        GraphPath<String,IdentifiedWeightedEdge> temp;

        //Store exhibit in order
        List<String> exhibitInOrder = new ArrayList<>();


        //Find the shortest path from beginning node to another
        //Want to loop through the list of exhibits until it is empty
        //When we find the shortest path one one vertex to another, remove the ending vertex from exhibits
        //Use that ending vertex to run getPaths
        while(toRemove.size()>0) {
            temp=allPaths.getPath(exhibits.get(0));
            //Runs through all exhibits we need to go to see which one is shortest
            smallestWeight=10000000;
            //Runs through all exhibits we need to go to see which one is shortest
            for (int i = 0; i < exhibits.size(); i++) {
                //If path is shorter than the current stored
                //Store path in temp
                //Store shortest weight in smallestWeight
                if(smallestWeight>allPaths.getPath(exhibits.get(i)).getWeight()){
                    smallestWeight=allPaths.getPath(exhibits.get(i)).getWeight();
                    temp=allPaths.getPath(exhibits.get(i));
                }
                //Set smallestWeight again
                //Set edges list
                //Set Vertices
                //Set new search graph path using removed ending vertex
                //Remove ending vertex
                smallestWeight=10000000;
                shortestEdges.addAll(temp.getEdgeList());
                shortestVertex.addAll(temp.getVertexList());
                //get rid of duplicates
                shortestVertex.remove(shortestVertex.size() - 1);
                toRemove.remove(temp.getEndVertex());
                exhibitInOrder.add(temp.getEndVertex());
                weights.add(temp.getWeight());
                allPaths=algorithm.getPaths(temp.getEndVertex());
            }
        }
        //Have shortestEdges,shortestVertex,exhibits selected
        //List<String>=shortestVertex,List<Identified>=shortestEdges,List<String>

        //go back to the gate
        temp = allPaths.getPath(start);
        shortestEdges.addAll(temp.getEdgeList());
        shortestVertex.addAll(temp.getVertexList());
        weights.add(temp.getWeight());

        //Change List<String> of vertices to List<Vertex>
        //Use for loop
        List<Vertex> toPassV=new ArrayList<>();
            for(int where=0;where<shortestVertex.size();where++){
                toPassV.add(vInfo.get(shortestVertex.get(where)));
            }

        //Change List<String> of Identified to List<Edges>
        List<Edge> toPassE=new ArrayList<>();
        for(int where=0;where<shortestEdges.size();where++){
            IdentifiedWeightedEdge graphEdge = shortestEdges.get(where);
            Edge infoEdge = eInfo.get(graphEdge.getId());
            infoEdge.weight = graphEdge.getWeight();
            toPassE.add(infoEdge);
        }

        // Return ExhibitRoute
        // which includes List<Vertex>, List<edge>, list<double> which is distance
        return new ExhibitRoute(toPassV,toPassE, weights, exhibitInOrder);
    }

}
