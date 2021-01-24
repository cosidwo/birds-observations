package com.example.obserwacje;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OwnObservatationsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_observations);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Observations");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOwnObservations);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                ArrayList<Observation> observations = new ArrayList();
                ArrayList<ObservationItem> observationItems = new ArrayList<>();
                for(DataSnapshot child : children){
                    Observation observation = child.getValue(Observation.class);
                    if(firebaseUser.getEmail().equals(observation.getEmail())) {
                        observations.add(observation);
                        String date = observation.getDate();
                        String species = observation.getSpecies();
                        String number = "" + observation.getNumber();
                        String place = observation.getPlace();
                        String email = observation.getEmail();
                        observationItems.add(new ObservationItem(date, species, number, place, email));
                        Log.d("MyApp", "Hello");
                    }
                }
                adapter = new ObservationAdapter(observationItems);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                for (Observation observation : observations){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
