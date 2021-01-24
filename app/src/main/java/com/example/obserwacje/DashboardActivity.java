package com.example.obserwacje;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;

    private BottomAppBar bottomAppBar;

    private MaterialTextView logoutTextView;
    private MaterialTextView mapKeyTextView;

    private FloatingActionButton addButton;
    private FloatingActionButton goToOwnObservationsButton;
    private FloatingActionButton goToAllObservationsButton;
    private FloatingActionButton goToProfileButton;
    private FloatingActionButton goToGalleryButton;

    private Intent addObservationIntent;
    private Intent goToOwnObservationsIntent;
    private Intent goToAllObservationsIntent;
    private Intent goToProfileIntent;
    private Intent goToGalleryIntent;

    private DatabaseReference databaseReference;

    private Double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomApp);

        logoutTextView = (MaterialTextView) findViewById(R.id.logoutTextView);
        mapKeyTextView = (MaterialTextView) findViewById(R.id.mapKey);

        addButton = (FloatingActionButton) findViewById(R.id.add);
        goToOwnObservationsButton = (FloatingActionButton) findViewById(R.id.own_list);
        goToAllObservationsButton = (FloatingActionButton) findViewById(R.id.list);
        goToProfileButton = (FloatingActionButton) findViewById(R.id.profile);
        goToGalleryButton = (FloatingActionButton) findViewById(R.id.gallery);

        addObservationIntent = new Intent(this,AddObservationActivity.class);
        goToOwnObservationsIntent = new Intent(this,OwnObservatationsActivity.class);
        goToAllObservationsIntent = new Intent(this, AllObservationsActivity.class);
        goToProfileIntent = new Intent(this,ProfileActivity.class);
        goToGalleryIntent = new Intent(this,GalleryActivity.class);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Observations");

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(addObservationIntent);
            }
        });

        goToOwnObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(goToOwnObservationsIntent); }
        });

        goToAllObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(goToAllObservationsIntent); }
        });

        goToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(goToProfileIntent); }
        });

        goToGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(goToGalleryIntent); }
        });
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DashboardActivity.this,MainActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng lublin = new LatLng(51.183835, 22.585523);
        //map.addMarker(new MarkerOptions().position(lublin).title("Lublin"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(lublin));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lublin.latitude, lublin.longitude), 7.0f));
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(DashboardActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(DashboardActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(DashboardActivity.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BitmapDescriptor grey = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_24dp);
                BitmapDescriptor blue = BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_place_blue_24);
                BitmapDescriptor red = BitmapDescriptorFactory.fromResource(R.drawable.ic__place_red_24);
             ///////  ArrayList<String> list = collectLatLngs((Map<String,Object>) snapshot.getValue());
                for(String latLng : list){
                    latLng = latLng.substring(10);
                    latLng = latLng.substring(0, latLng.length()-1);
                    String [] s = latLng.split(",");
                    lat = Double.parseDouble(s[0]);
                    lng = Double.parseDouble(s[1]);
                    LatLng point = new LatLng(lat,lng);
                    if()
                        map.addMarker(new MarkerOptions().position(point));
           //////////     }

        try {
            ArrayList<Observation> list = collectObservations((Map<Observation, Object>) snapshot.getValue());
            for (Observation observation : list) {
                String latLng = observation.getLatLng();
                latLng = latLng.substring(10);
                latLng = latLng.substring(0, latLng.length() - 1);
                String[] s = latLng.split(",");
                lat = Double.parseDouble(s[0]);
                lng = Double.parseDouble(s[1]);
                LatLng point = new LatLng(lat, lng);
                if (observation.getRare() == 1)
                    map.addMarker(new MarkerOptions().icon(grey).position(point));
                else if (observation.getRare() == 2)
                    map.addMarker(new MarkerOptions().icon(blue).position(point));
                else if (observation.getRare() == 3)
                    map.addMarker(new MarkerOptions().icon(red).position(point));
            }
        } catch (NullPointerException nullPointerException) {
                    /*ArrayList<Observation> list = new ArrayList<>();
                    Observation o = new Observation("abc", "", "","","","","","", "", "", "", "",1,0);
                    list.add(o);
        }

    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                Log.d("MyApp","I am here");
                ArrayList<Observation> observations = new ArrayList();
                for(DataSnapshot child : children){
                    Observation observation = child.getValue(Observation.class);
                    observations.add(observation);
                }
                for (Observation observation : observations) {
                    String latLng = observation.getLatLng();
                    latLng = latLng.substring(10);
                    latLng = latLng.substring(0, latLng.length() - 1);
                    String[] s = latLng.split(",");
                    lat = Double.parseDouble(s[0]);
                    lng = Double.parseDouble(s[1]);
                    LatLng point = new LatLng(lat, lng);
                    String title = observation.getSpecies();
                    String snippet = observation.getDate()+"\nLiczebność: "+observation.getNumber();
                    String dateObservationString = observation.getDate();
                    DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    try {
                        Date dateObservation = dateFormat.parse(dateObservationString);
                        Date dateCurrent = new Date();
                        dateFormat.format(dateCurrent);
                        LocalDateTime localDateTimeObservation = dateObservation.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        LocalDateTime localDateTimeCurrent = dateCurrent.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        long daysBetween = Duration.between(localDateTimeCurrent,localDateTimeObservation).toDays();
                        Log.d("MyApp", ""+daysBetween);
                        if((daysBetween>=-7) && (daysBetween<=0)) {
                            if (observation.getRare() == 1) {
                                MarkerOptions markerOptions = new MarkerOptions().position(point).title(title).snippet(snippet);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                map.addMarker(markerOptions);
                                Log.d("MyApp", "I am blue");
                            } else if (observation.getRare() == 2) {
                                MarkerOptions markerOptions = new MarkerOptions().position(point).title(title).snippet(snippet);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                map.addMarker(markerOptions);
                                Log.d("MyApp", "I am green");
                            } else if (observation.getRare() == 3) {
                                MarkerOptions markerOptions = new MarkerOptions().position(point).title(title).snippet(snippet);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                map.addMarker(markerOptions);
                                Log.d("MyApp", "I am red");
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String mapKey = "<font color=#088DA5> Niebieski - nieliczne &nbsp </font> <font color=#BADA55> Zielony - rzadkie &nbsp </font> <font color=#880000> Czerwony - komisyjne </font> ";
        mapKeyTextView.setText(Html.fromHtml(mapKey));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private ArrayList<String> collectLatLngs(Map<String,Object> observations){
        ArrayList<String> latLngs = new ArrayList<>();

        for(Map.Entry<String,Object> entry : observations.entrySet()){
            Map singleLatLng = (Map) entry.getValue();
            latLngs.add((String) singleLatLng.get("latLng"));
        }
        return latLngs;
    }

    private ArrayList<Observation> collectObservations(Map<Observation,Object> observations){
        ArrayList<Observation> observation = new ArrayList<>();

        for(Map.Entry<Observation,Object> entry : observations.entrySet()){
            Map singleObservation = (Map) entry.getValue();
            observation.add((Observation) singleObservation);
        }
        return observation;
    }
}
