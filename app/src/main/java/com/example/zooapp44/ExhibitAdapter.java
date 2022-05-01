package com.example.zooapp44;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ExhibitAdapter extends RecyclerView.Adapter<ExhibitAdapter.ViewHolder> {
    private List<ToAddExhibits> exhibits= Collections.emptyList();

    public void setExhibitListItems(List<ToAddExhibits> newExhibits){
        this.exhibits.clear();
        this.exhibits=newExhibits;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_exhibits,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setExhibit(exhibits.get(position));
    }

    @Override
    public int getItemCount() {
        return exhibits.size();
    }


    public String getItemType(int position){
        return exhibits.get(position).itemType;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private ToAddExhibits toAdd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView=itemView.findViewById(R.id.exhibit);
        }

        public ToAddExhibits getExhibits()
        {return toAdd;}

        public void setExhibit(ToAddExhibits toAdd){
            this.toAdd=toAdd;
            this.textView.setText(toAdd.id);
        }
    }

}
