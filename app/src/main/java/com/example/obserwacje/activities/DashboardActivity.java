package com.example.obserwacje.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

//dashboard implementation
public class DashboardActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;

    private MaterialTextView mapKeyTextView;

    private Intent addObservationIntent;
    private Intent goToOwnObservationsIntent;
    private Intent goToAllObservationsIntent;
    private Intent goToProfileIntent;
    private Intent goToGalleryIntent;

    private DatabaseReference databaseReference;

    private Double lat, lng;

    //initializing objects and setting click listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //initializing map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottomApp);

        MaterialTextView logoutTextView = (MaterialTextView) findViewById(R.id.logoutTextView);
        mapKeyTextView = (MaterialTextView) findViewById(R.id.mapKey);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add);
        FloatingActionButton goToOwnObservationsButton = (FloatingActionButton) findViewById(R.id.own_list);
        FloatingActionButton goToAllObservationsButton = (FloatingActionButton) findViewById(R.id.list);
        FloatingActionButton goToProfileButton = (FloatingActionButton) findViewById(R.id.profile);
        FloatingActionButton goToGalleryButton = (FloatingActionButton) findViewById(R.id.gallery);

        addObservationIntent = new Intent(this, AddObservationActivity.class);
        goToOwnObservationsIntent = new Intent(this, OwnObservatationsActivity.class);
        goToAllObservationsIntent = new Intent(this, AllObservationsActivity.class);
        goToProfileIntent = new Intent(this, ProfileActivity.class);
        goToGalleryIntent = new Intent(this, GalleryActivity.class);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Observations");

        logoutTextView.setOnClickListener(v -> logout());

        addButton.setOnClickListener(v -> startActivity(addObservationIntent));

        goToOwnObservationsButton.setOnClickListener(v -> startActivity(goToOwnObservationsIntent));

        goToAllObservationsButton.setOnClickListener(v -> startActivity(goToAllObservationsIntent));

        goToProfileButton.setOnClickListener(v -> startActivity(goToProfileIntent));

        goToGalleryButton.setOnClickListener(v -> startActivity(goToGalleryIntent));
    }

    //called when user wants to logout
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                ArrayList<Observation> observations = new ArrayList();

                //fetching data from database
                for(DataSnapshot child : children){
                    Observation observation = child.getValue(Observation.class);
                    observations.add(observation);
                }

                //setting markers' credentials based on data fetched from db
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
                        //parsing date of observation and comparing it to current date later on
                        Date dateObservation = dateFormat.parse(dateObservationString);
                        Date dateCurrent = new Date();
                        dateFormat.format(dateCurrent);
                        LocalDateTime localDateTimeObservation = dateObservation.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        LocalDateTime localDateTimeCurrent = dateCurrent.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        long daysBetween = Duration.between(localDateTimeCurrent,localDateTimeObservation).toDays();

                        //displaying only newest observations from past week
                        if((daysBetween>=-7) && (daysBetween<=0)) {
                            //displaying different colored markers (blue, green and red) depenging on rarity of the observation
                            if (observation.getRare() == 1) {
                                MarkerOptions markerOptions = new MarkerOptions().position(point).title(title).snippet(snippet);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                map.addMarker(markerOptions);
                            } else if (observation.getRare() == 2) {
                                MarkerOptions markerOptions = new MarkerOptions().position(point).title(title).snippet(snippet);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                map.addMarker(markerOptions);
                            } else if (observation.getRare() == 3) {
                                MarkerOptions markerOptions = new MarkerOptions().position(point).title(title).snippet(snippet);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                map.addMarker(markerOptions);
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
}
