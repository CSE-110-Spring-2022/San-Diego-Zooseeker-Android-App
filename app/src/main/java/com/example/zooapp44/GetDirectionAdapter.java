//package com.example.zooapp44;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class GetDirectionAdapter extends RecyclerView.Adapter<GetDirectionAdapter.ViewHolder>{
//    private ExhibitRoute route;
//    public GetDirectionAdapter(ExhibitRoute route){
//        this.route = route;
//    }
//
//    @NonNull
//    @Override
//    public GetDirectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.instructions, parent, false);
//        return new GetDirectionAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GetDirectionAdapter.ViewHolder holder, int position) {
//        //holder.setGetDirection(route.getExhibit(position), route.getDistance(position), route.getExhibit(position+1), route.getDistance(position+1));
//    }
//
//    @Override
//    public int getItemCount() {
//        return route.getSize();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private final TextView currentName;
//        private final TextView currentDistance;
//        private final TextView nextName;
//        private final TextView nextDistance;
//        //private final TextView instruction;
//
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            this.currentName = itemView.findViewById(R.id.current_exhibit);
//            this.currentDistance = itemView.findViewById(R.id.current_distance);
//            this.nextDistance = itemView.findViewById(R.id.next);
//            this.nextName = itemView.findViewById(R.id.next_exhibit);
//            //this.instruction = itemView.findViewById(R.id.directions);
//        }
//
//        public void setGetDirection(String exhibitName, String distance, String nextName, String nextDistance){
//            this.currentName.setText(exhibitName);
//            this.currentDistance.setText(distance);
//            //this.instruction.setText(instruction);
//            this.nextName.setText(nextName);
//            this.nextDistance.setText(nextDistance);
//        }
//    }
//}
