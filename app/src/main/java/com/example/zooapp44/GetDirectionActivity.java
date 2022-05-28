package com.example.zooapp44;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class GetDirectionActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private int current;
    ExhibitRoute route;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

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

        updateText();

    }

    @SuppressLint("SetTextI18n")
    private void updateText() {
        TextView currentAnimalView = findViewById(R.id.current_animal);

        currentAnimalView.setText(route.getExhibit(current));
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
        else if(current + 1 > route.getSize()) {
            nextAnimalView.setText("");
        }
        else
            nextAnimalView.setText(route.getExhibit(current + 1));


//        if(route.getSize() <= 1){
//            ImageView img = findViewById(R.id.skip_btn);
//            img.setVisibility(View.INVISIBLE);
//        }


        if(current == route.getSize()){
            ImageView img = findViewById(R.id.skip_btn);
            img.setVisibility(View.INVISIBLE);
        }

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

            ImageView img = findViewById(R.id.skip_btn);
            img.setVisibility(View.INVISIBLE);
        }
        updateText();
        editor.apply();
    }


    public void onSkipClicked(View view) {
        // get new distance
        Double new_dist = route.weight.get(current) + route.weight.get(current + 1);
        route.weight.set(current + 1, new_dist);
        route.weight.remove(current);

        // get new exhibit name
        route.exhibits.remove(current);

        // get new instruction
        route.edges.remove(current);
        route.vertices.remove(current + 1);
        updateText();

        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);

            ImageView img = findViewById(R.id.skip_btn);
            img.setVisibility(View.INVISIBLE);
        }

    }
  
    public void onStopClicked(View view) {
        editor.clear();
        editor.apply();
        finish();
    }
}