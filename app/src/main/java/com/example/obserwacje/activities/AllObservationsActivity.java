package com.example.obserwacje.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.example.obserwacje.entities.Observation;
import com.example.obserwacje.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//activity used to display all observations of given species
public class AllObservationsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String [] species = {"Łabędź czarnodzioby", "Gęś mała", "Hełmiatka", "Perkoz rogaty", "Lodowiec", "Cietrzew","Orzeł przedni", "Czapla nadobna", "Turkawka", "Bocian czarny", "Ślepowron", "Zielonka", "Czajka towarzyska", "Mewa romańska", "Dzierlatka"};

    private AutoCompleteTextView speciesMenu;
    private Button mapSpeciesButton;

    private GoogleMap map;

    private DatabaseReference databaseReference;

    private Double lat, lng;

    //initializing objects
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_observations);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapChosenSpecies);
        mapFragment.getMapAsync(this);

        speciesMenu = (AutoCompleteTextView) findViewById(R.id.dropdownMenuMapping);
        mapSpeciesButton = (Button) findViewById(R.id.mapSpeciesButton);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Observations");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.option_item, species);
        speciesMenu.setText(arrayAdapter.getItem(0).toString(), false);
        speciesMenu.setAdapter(arrayAdapter);
    }

    private void mapSpecies(View view, GoogleMap map){
        map.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            int numberOfSpecies=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                ArrayList<Observation> observations = new ArrayList();

                //fetching data from database
                for(DataSnapshot child : children){
                    Observation observation = child.getValue(Observation.class);
                    observations.add(observation);
                }

                String species = speciesMenu.getText().toString().trim();
                for(Observation observation : observations){

                    //displaying observations from single species given by user
                    if(observation.getSpecies().equals(species)){
                        String latLng = observation.getLatLng();
                        latLng = latLng.substring(10);
                        latLng = latLng.substring(0, latLng.length() - 1);
                        String[] s = latLng.split(",");
                        lat = Double.parseDouble(s[0]);
                        lng = Double.parseDouble(s[1]);
                        LatLng point = new LatLng(lat, lng);
                        String title = observation.getSpecies();
                        String snippet = observation.getDate()+"\nLiczebność: "+observation.getNumber();
                        MarkerOptions markerOptions = new MarkerOptions().position(point).title(title).snippet(snippet);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        map.addMarker(markerOptions);
                        numberOfSpecies++;
                    }

                }
                //if there are no observations of given species in db
                if(numberOfSpecies==0){
                    Toast.makeText(AllObservationsActivity.this,"Nie ma danych na temat tego gatunku",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //map implementation
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //setting default coordinates for map
        LatLng lublin = new LatLng(51.183835, 22.585523);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lublin.latitude, lublin.longitude), 7.0f));
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(AllObservationsActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(AllObservationsActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(AllObservationsActivity.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        mapSpeciesButton.setOnClickListener(v -> mapSpecies(v,map));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
