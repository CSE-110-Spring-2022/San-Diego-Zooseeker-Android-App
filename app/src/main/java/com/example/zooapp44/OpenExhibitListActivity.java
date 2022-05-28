package com.example.zooapp44;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import java.util.ArrayList;
import java.util.List;

public class OpenExhibitListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
//    private ToAddExhibitsViewModel viewModel;
    ExhibitRoute route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_exhibit_list);


        Intent intent = getIntent();
        route = ExhibitRoute.deserialize(intent.getStringExtra("Route"));

        if(route.getSize() == 0){
            Button directionButton = findViewById(R.id.direction_button);
            directionButton.setVisibility(View.INVISIBLE);
        }

//        viewModel = new ViewModelProvider(this)
//                .get(ToAddExhibitsViewModel.class);
        OpenExhibitListAdapter adapter = new OpenExhibitListAdapter(route);
        adapter.setHasStableIds(true);
//        viewModel.getToaddExhibits().observe(this, adapter::setToaddExhibits);

        recyclerView = findViewById(R.id.route);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    public void onHomeClicked(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//         finish();
    }

    public void onGetDirectionClicked(View view){
        Intent intent = new Intent(this, GetDirectionActivity.class);
        intent.putExtra("Route", ExhibitRoute.serialize(route));

        LocationModel location= new LocationModel();
        Context context = ApplicationProvider.getApplicationContext();

        List<MockLocation> mock= MockLocation.loadMockJSON(context,"mock_route1.json");
        List<Coord> coordList= new ArrayList<>();
        List<Double> timeList= new ArrayList<>();
        for(var object: mock){
            coordList.add(object.mock);
            timeList.add(object.time);
        }
        location.mockRoute(coordList,timeList);

        startActivity(intent);
    }

    public void onClearClicked(View view) {
        ToAddExhibitDao toAddExhibitDao = ToAddDatabase.getSingleton(this).toAddExhibitDao();
        List<ToAddExhibits> exhibitItems = toAddExhibitDao.getAll();
        for(int i = 0; i < exhibitItems.size(); i ++){
            exhibitItems.get(i).selected = false;
            toAddExhibitDao.update(exhibitItems.get(i));
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // clear the route
        route.exhibits.clear();
        route.edges.clear();
        route.vertices.clear();
        route.weight.clear();

        setContentView(R.layout.activity_open_exhibit_list);
        // disable the get direction button
        Button directionButton = findViewById(R.id.direction_button);
        directionButton.setVisibility(View.INVISIBLE);
    }
}