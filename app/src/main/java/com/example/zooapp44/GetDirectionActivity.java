package com.example.zooapp44;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zooapp44.utils.VertexCoord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GetDirectionActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private int current;
    private boolean show = true;
    ExhibitRoute route;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    LocationModel location;
    Map<String, Coord> vertexCoordMap = VertexCoord.getVertexCoordMap(this);

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button replan_btn, cancel_btn;


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
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("")
                .setPositiveButton("Replan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //implementation of replan goes here
                        show = false;
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        show=false;
                        dialog.dismiss();
                    }
                })
                .setTitle("Off track, replan?")
                .create();


        //Observes when the location changes
        final Observer<Coord> observe = new Observer<Coord>() {
            @Override
            public void onChanged(@Nullable Coord coord) {
                Coord current_coord = location.getLastKnownCoords();
                route.current_coord = current_coord;
                if(!route.onRoute(vertexCoordMap, current)){
                    System.out.println("Off Route!");
                    // Do off-route pop up here.
                    route.onRoute(vertexCoordMap, current);
                    if (show) {
                        myAlert.show();
                    }
                }
                else{

                }
                System.out.println(current_coord.toString());
            }
        };

        location= new LocationModel(observe);
        location.giveMutable().observe(this,observe);


        /**
        List<MockLocation> mock= MockLocation.loadMockJSON(getApplicationContext(),"mock_route1.json");
        List<Coord> coordList= new ArrayList<>();
        List<Double> timeList= new ArrayList<>();
        for(var object: mock){
            object.change();
            coordList.add(object.mock);
            timeList.add(object.time);
        }
        location.mockRoute(coordList,timeList);
         **/
        Coord start= new Coord(32.73459618734685,-117.14936);
        location.giveMutable().setValue(start);
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
        instructionView.setText(route.getInstruction(current,location.getLastKnownCoords()));


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

        if(current == route.getSize()){
            ImageView img = findViewById(R.id.skip_btn);
            img.setVisibility(View.INVISIBLE);
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateTextBack(){
        TextView currentAnimalView = findViewById(R.id.current_animal);

        TextView nextAnimalView = findViewById(R.id.next_animal);

        currentAnimalView.setText(route.getExhibit(current));
        TextView backDistanceView = findViewById(R.id.current_distance);
        backDistanceView.setText(route.getBackDistance(current, false));

        TextView instructionView = findViewById(R.id.route_instruction);
        instructionView.setText(route.getBackInstruction(current+1));

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
        Button back = findViewById(R.id.back_btn);
        back.setVisibility(View.VISIBLE);

        ImageView img = findViewById(R.id.skip_btn);
        img.setVisibility(View.VISIBLE);

        current++;
        editor.putInt("current_index", current);
        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);

            img.setVisibility(View.INVISIBLE);
        }
        updateText();
        editor.apply();
    }

    public void onEnterClicked(View view){
         TextView dot=findViewById(R.id.Location);
         String text = dot.getText().toString();
         String[] names= text.split(",");
         Coord cur=new Coord(Double.parseDouble(names[0]),Double.parseDouble(names[1]));
         location.giveMutable().setValue(cur);
    }



    public void onSkipClicked(View view) {
        // get new distance
        Double new_dist = route.weight.get(current) + route.weight.get(current + 1);
        route.weight.set(current + 1, new_dist);
        route.weight.remove(current);

        // get new exhibit name
        route.exhibits.remove(current);

        updateText();


        // disable buttons when reach the last one
        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);

            ImageView img = findViewById(R.id.skip_btn);
            img.setVisibility(View.INVISIBLE);
        }

    }


    public void onBackClicked(View view){
        Button next = findViewById(R.id.next_btn);
        next.setVisibility(View.VISIBLE);

        ImageView img = findViewById(R.id.skip_btn);
        img.setVisibility(View.VISIBLE);

        current --;

        if(current < 0){
            Button button = findViewById(R.id.back_btn);
            button.setVisibility(View.INVISIBLE);

            img.setVisibility(View.INVISIBLE);

            TextView currentAnimalView = findViewById(R.id.current_animal);
            TextView nextAnimalView = findViewById(R.id.next_animal);

            currentAnimalView.setText("Entrance gate");
            TextView backDistanceView = findViewById(R.id.current_distance);
            backDistanceView.setText(route.getBackDistance(current, false));

            TextView instructionView = findViewById(R.id.route_instruction);
            instructionView.setText(route.getBackInstruction(current+1));

            nextAnimalView.setText(route.getExhibit(current + 1));

        } else {
            updateTextBack();
        }
    }



    public void onStopClicked(View view) {
        editor.clear();
        editor.apply();
        finish();
    }



    // belows are functions handling the reroute popup window


    public void createReplanMessage(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View replanView = inflater.inflate(R.layout.replan_popup, null);
        boolean focusable = true;

        float density = GetDirectionActivity.this.getResources().getDisplayMetrics().density;
        final PopupWindow replanPopupView = new PopupWindow(replanView, (int)density*400, (int)density*300, focusable);
        replanPopupView.showAtLocation(view, Gravity.CENTER, 0, 0);

        cancel_btn = (Button) replanView.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            // onClick method for cancel button
            @Override
            public void onClick(View view) {
                replanPopupView.dismiss();
            }
        });

        replan_btn = (Button) replanView.findViewById(R.id.replan_btn);
        replan_btn.setOnClickListener(new View.OnClickListener() {
            // onClick method for replan button
            @Override
            public void onClick(View view){
                // get current location and replan new route
            }
        });

    }


    public void onOffClick(View view) {
        createReplanMessage(view);
    }
}