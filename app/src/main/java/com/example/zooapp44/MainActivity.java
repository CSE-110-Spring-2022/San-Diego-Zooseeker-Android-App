package com.example.zooapp44;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    ExhibitAdapter adapter;
    ToAddExhibitsViewModel exhibitsViewModel;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exhibitsViewModel = new ViewModelProvider(this).get(ToAddExhibitsViewModel.class);

        searchView = findViewById(R.id.search_view);

        ToAddExhibitDao toAddExhibitDao = ToAddDatabase.getSingleton(this).toAddExhibitDao();
        List<ToAddExhibits> exhibitItems = toAddExhibitDao.getAll();
        TextView msg = findViewById(R.id.message);
        count = toAddExhibitDao.getSelected().size()-2;
        String temp = "There are "+Integer.toString(count)+" animals selected.";
        msg.setText(temp);
        adapter = new ExhibitAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnCheckBoxClickedHandler(new Consumer<ToAddExhibits>() {
            @Override
            public void accept(ToAddExhibits toAddExhibits) {
                toAddExhibits.selected = !toAddExhibits.selected;
                toAddExhibitDao.update(toAddExhibits);
                count = toAddExhibitDao.getSelected().size()-2;
                String current= "There are "+Integer.toString(count)+" animals selected.";
                msg.setText(current);
            }
        });

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