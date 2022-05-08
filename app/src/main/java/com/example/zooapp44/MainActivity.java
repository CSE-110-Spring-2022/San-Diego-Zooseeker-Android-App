package com.example.zooapp44;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    ExhibitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.search_view);

        ToAddExhibitDao toAddExhibitDao = ToAddDatabase.getSingleton(this).toAddExhibitDao();
        List<ToAddExhibits> exhibitItems = toAddExhibitDao.getAll();

        adapter = new ExhibitAdapter();
        adapter.setHasStableIds(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<ToAddExhibits> filteredItems = new ArrayList<ToAddExhibits>();
                //query_item stores the user's input
                String query_item = searchView.getQuery().toString();
                Log.i("query item", query_item);

                for(int i = 0; i < exhibitItems.size(); i ++){
                    List<String> tags = exhibitItems.get(i).tags;
                    Log.i("tags", tags.toString());
                    for(int j = 0; j < tags.size(); j ++){
                        if(query_item.equals(tags.get(j))){
                            filteredItems.add(exhibitItems.get(i));
                        }
                    }
                }
                adapter.setExhibitListItems(filteredItems);
                Log.i("filtered items", filteredItems.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        recyclerView = findViewById(R.id.exhibit_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void onSearchBarClicked(View view) {
        Intent intent = new Intent(this,ToAddExhibitsActivity.class);

//        Intent intent = new Intent(this,ToAddExhibitsActivity.class);
        startActivity(intent);

//        List<String> exhibits = Arrays.asList("Tiger", "Bear", "Dog", "Elephant");
//        List<String> distance = Arrays.asList("300ft", "500ft", "200ft", "100ft");
//        ExhibitRoute route = new ExhibitRoute(exhibits, distance);
//        Intent intent = new Intent(this, OpenExhibitListActivity.class);
//        intent.putExtra("Route", ExhibitRoute.serialize(route));
//        startActivity(intent);
    }

    public void onPlanClicked(View view){
        Intent intent = new Intent(this, OpenExhibitListActivity.class);
        ZooGraph g = ZooGraph.getSingleton(getApplicationContext());

        ToAddDatabase db = ToAddDatabase.getSingleton(getApplicationContext());
        ToAddExhibitDao dao = db.toAddExhibitDao();
        List<ToAddExhibits> exhibits = dao.getSelected();

        List<String> exhibitIds = new ArrayList<>();
        for (ToAddExhibits toAddExhibits : exhibits) {
            String id = toAddExhibits.id;
            exhibitIds.add(id);
        }
        String start = "entrance_exit_gate";
        ExhibitRoute route = g.getOptimalPath(start, exhibitIds);
        intent.putExtra("Route", ExhibitRoute.serialize(route));
        startActivity(intent);
    }
}