package com.example.zooapp44;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class GetDirectionActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private int current;
    ExhibitRoute route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_direction);

        route = ExhibitRoute.deserialize(getIntent().getStringExtra("Route"));

        current = 0;
        
        updateText();

    }

    @SuppressLint("SetTextI18n")
    private void updateText() {
        TextView currentAnimalView = findViewById(R.id.current_animal);

        currentAnimalView.setText(route.getExhibit(current));
        TextView currentDistanceView = findViewById(R.id.current_distance);
        currentDistanceView.setText(route.getDistance(current, false));

        TextView nextAnimalView = findViewById(R.id.next_animal);

        TextView instructionView = findViewById(R.id.route_instruction);
        instructionView.setText(route.getInstruction(current));

        if(current + 1 == route.getSize())
            nextAnimalView.setText("Entrance gate");
        else if(current + 1 > route.getSize())
            nextAnimalView.setText("");
        else
            nextAnimalView.setText(route.getExhibit(current + 1));

    }

    public void onHomeClicked(View view){
        super.onBackPressed();
        startActivity(new Intent(GetDirectionActivity.this, MainActivity.class));
        finish();
    }

    public void onNextClicked(View view){
        current++;
        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);
        }
        updateText();
    }

    public void onStopClicked(View view) { finish(); }
}