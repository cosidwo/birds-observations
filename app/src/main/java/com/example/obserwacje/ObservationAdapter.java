package com.example.obserwacje;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    private ArrayList<ObservationItem> observationItems;

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

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        ObservationItem currentItem = observationItems.get(position);
        holder.date.setText(currentItem.getDate());
        holder.species.setText(currentItem.getSpecies());
        holder.number.setText("Licz.: " + currentItem.getNumber());
        holder.place.setText(currentItem.getName());

    }

    @Override
    public int getItemCount() {
        return observationItems.size();
    }
}
