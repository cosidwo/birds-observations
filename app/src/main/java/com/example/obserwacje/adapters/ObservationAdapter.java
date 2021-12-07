package com.example.obserwacje.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obserwacje.entities.ObservationItem;
import com.example.obserwacje.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

//class used to display observations in RecyclerView
public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    private final ArrayList<ObservationItem> observationItems;

    //nested static class
    public static class ObservationViewHolder extends RecyclerView.ViewHolder{
        public MaterialTextView date;
        public MaterialTextView species;
        public MaterialTextView number;
        public MaterialTextView place;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateOwnObservation);
            species = itemView.findViewById(R.id.speciesOwnObservation);
            number = itemView.findViewById(R.id.numberOwnObservation);
            place = itemView.findViewById(R.id.placeOwnObservation);
        }
    }

    //constructor
    public ObservationAdapter(ArrayList<ObservationItem> observationItems){
        this.observationItems = observationItems;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.observation_item,parent,false);
        ObservationViewHolder observationViewHolder = new ObservationViewHolder(v);
        return observationViewHolder;
    }

    //loading info about observations from ArrayList
    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        ObservationItem currentItem = observationItems.get(position);
        holder.date.setText(currentItem.getDate());
        holder.species.setText(currentItem.getSpecies());
        holder.number.setText("Licz.: " + currentItem.getNumber());
        holder.place.setText(currentItem.getName());
    }

    //getting number of items
    @Override
    public int getItemCount() {
        return observationItems.size();
    }
}
