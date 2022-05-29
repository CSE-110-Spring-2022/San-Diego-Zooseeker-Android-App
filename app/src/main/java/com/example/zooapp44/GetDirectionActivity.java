package com.example.zooapp44;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zooapp44.utils.VertexCoord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GetDirectionActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private int current;
    ExhibitRoute route;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LocationModel location;
    Map<String, Coord> vertexCoordMap = VertexCoord.getVertexCoordMap(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_direction);

        route = ExhibitRoute.deserialize(getIntent().getStringExtra("Route"));
        preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        editor = preferences.edit();

        current = preferences.getInt("current_index", 0);
        current = 0;

        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);
        }


        //Observes when the location changes
        final Observer<Coord> observe = new Observer<Coord>() {
            @Override
            public void onChanged(@Nullable Coord coord) {
                Coord current_coord = location.getLastKnownCoords();
                route.current_coord = current_coord;
                if(!route.onRoute(vertexCoordMap, current)){
                    System.out.println("Off Route!");
                    // Do off-route pop up here.
                }
                System.out.println(current_coord.toString());
            }
        };

        location= new LocationModel(observe);
        location.giveMutable().observe(this,observe);



        List<MockLocation> mock= MockLocation.loadMockJSON(getApplicationContext(),"mock_route1.json");
        List<Coord> coordList= new ArrayList<>();
        List<Double> timeList= new ArrayList<>();
        for(var object: mock){
            object.change();
            coordList.add(object.mock);
            timeList.add(object.time);
        }
        location.mockRoute(coordList,timeList);
        updateText();
    }

    @SuppressLint("SetTextI18n")
    private void updateText() {
        TextView currentAnimalView = findViewById(R.id.current_animal);

        currentAnimalView.setText(route.getOriginal(current));
        TextView currentDistanceView = findViewById(R.id.current_distance);
        currentDistanceView.setText(route.getDistance(current, false));


        TextView instructionView = findViewById(R.id.route_instruction);
        instructionView.setMovementMethod(new ScrollingMovementMethod());
        instructionView.setText(route.getInstruction(current));

        updateNextAnimalView();
    }

    private void updateNextAnimalView() {
        TextView nextAnimalView = findViewById(R.id.next_animal);
        if(current + 1 == route.getSize())
            nextAnimalView.setText("Entrance gate");
        else if(current + 1 > route.getSize())
            nextAnimalView.setText("");
        else
            nextAnimalView.setText(route.getOriginal(current + 1));
    }

    public void onHomeClicked(View view){
        super.onBackPressed();
        startActivity(new Intent(GetDirectionActivity.this, MainActivity.class));
        finish();
    }

    public void onNextClicked(View view){
        current++;
        editor.putInt("current_index", current);
        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);
        }
        updateText();
        editor.apply();
    }

    public void onStopClicked(View view) {
        editor.clear();
        editor.apply();
        finish();
    }
}