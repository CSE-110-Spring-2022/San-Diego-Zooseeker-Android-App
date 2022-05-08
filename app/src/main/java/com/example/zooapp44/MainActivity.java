package com.example.zooapp44;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
//package com.example.test.searchview;
import android.os.Bundle;
import android.widget.Filter;


public class MainActivity extends AppCompatActivity {
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.lv1);

        list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Pineapple");
        list.add("Orange");
        list.add("Lychee");
        list.add("Gavava");
        list.add("Peech");
        list.add("Melon");
        list.add("Watermelon");
        list.add("Papaya");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });
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