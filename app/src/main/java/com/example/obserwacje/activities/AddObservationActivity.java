package com.example.obserwacje.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.example.obserwacje.entities.Image;
import com.example.obserwacje.entities.Observation;
import com.example.obserwacje.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

//activity allowing user to add observations to database
public class AddObservationActivity extends FragmentActivity implements OnMapReadyCallback {

    DatabaseReference firebaseDatabase;
    DatabaseReference firebaseDatabase2;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;

    private StorageTask storageTask;

    private GoogleMap map;
    private Marker marker;
    String latLngText;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageURI;

    private AutoCompleteTextView speciesMenu;
    private AutoCompleteTextView categoriesMenu;
    private AutoCompleteTextView criterionMenu;
    private AutoCompleteTextView accuracyMenu;
    private AutoCompleteTextView habitatsMenu;
    private TextInputEditText dateEditText;
    private TextInputEditText observerNameEditText;
    private TextInputEditText additionalInfoEditText;
    private TextInputEditText placeEditText;
    private TextInputEditText countyEditText;
    private TextInputEditText numberEditText;

    //string arrays used inside menus
    private static final String [] species = {"Łabędź czarnodzioby", "Gęś mała", "Hełmiatka", "Perkoz rogaty", "Lodowiec", "Cietrzew","Orzeł przedni", "Czapla nadobna", "Turkawka", "Bocian czarny", "Ślepowron", "Zielonka", "Czajka towarzyska", "Mewa romańska", "Dzierlatka"};
    private static final String [] categories = {"Lęgowe", "Wszystkie", "Fenologiczne", "Stado", "Martwe"};
    private static final String [] criterion = {"Brak", "O - Pojedyncze ptaki w siedlisku lęgowym", "P - para ptaków w siedlisku lęgowym", "BU - budowa gniazda", "WYS - wysiadywanie na gnieździe", "MŁO - młode zagniazdowniki poza gniazdem"};
    private static final String [] numberType = {"Liczba dokładna", "Liczba szacunkowa", "Liczba przybliżona (na oko)"};
    private static final String [] habitats = {"Aleja przydrożna", "Las liściasty", "Las mieszany", "Las iglasty", "Ols", "Łęg", "Park", "Jezioro", "Staw", "Rzeka", "Łąka", "Rozlewisko", "Pastwisko", "Pole uprawne", "Miasto", "Wieś", "Kopalnia", "Wysypisko śmieci", "Inne"};

    private String speciesObservation;
    private String name;
    private String place;
    private String county;
    private String date;

    //initializing objects and setting click listeners
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        Button button = (Button) findViewById(R.id.addObservationButton);
        Button addImageButton = (Button) findViewById(R.id.addImage);
        speciesMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu);
        categoriesMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu2);
        criterionMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu3);
        accuracyMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu4);
        habitatsMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu5);
        dateEditText = (TextInputEditText) findViewById(R.id.date2);
        observerNameEditText = (TextInputEditText) findViewById(R.id.observer);
        additionalInfoEditText = (TextInputEditText) findViewById(R.id.additionalInfo);
        placeEditText = (TextInputEditText) findViewById(R.id.place);
        countyEditText = (TextInputEditText) findViewById(R.id.county);
        numberEditText = (TextInputEditText) findViewById(R.id.number);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAddObservation);
        mapFragment.getMapAsync(this);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Observations");
        firebaseDatabase2 = FirebaseDatabase.getInstance().getReference("Images");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Images");


        //initializng menu objects
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.option_item, species);
        speciesMenu.setText(arrayAdapter.getItem(0).toString(), false);
        speciesMenu.setAdapter(arrayAdapter);

        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.option_item, categories);
        categoriesMenu.setText(arrayAdapter2.getItem(0).toString(),false);
        categoriesMenu.setAdapter(arrayAdapter2);

        ArrayAdapter arrayAdapter3 = new ArrayAdapter(this, R.layout.option_item, criterion);
        criterionMenu.setText(arrayAdapter3.getItem(0).toString(), false);
        criterionMenu.setAdapter(arrayAdapter3);

        ArrayAdapter arrayAdapter4 = new ArrayAdapter(this, R.layout.option_item, numberType);
        accuracyMenu.setText(arrayAdapter4.getItem(0).toString(), false);
        accuracyMenu.setAdapter(arrayAdapter4);

        ArrayAdapter arrayAdapter5 = new ArrayAdapter(this, R.layout.option_item, habitats);
        habitatsMenu.setText(arrayAdapter5.getItem(0).toString(), false);
        habitatsMenu.setAdapter(arrayAdapter5);

        criterionMenu.setFocusableInTouchMode(false);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Wybierze date");
        final MaterialDatePicker datePicker = builder.build();
        dateEditText.setOnClickListener(v -> datePicker.show(getSupportFragmentManager(),"DATE PICKER"));

        //criterion menu is activated only if user chooses "breeding" category
        categoriesMenu.setOnClickListener(v -> {
            if(categoriesMenu.getText().toString().equals("Lęgowe")){
                criterionMenu.setFocusable(true);
                criterionMenu.setFocusableInTouchMode(true);
            }
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            dateEditText.setText(""+datePicker.getHeaderText());
            dateEditText.setFocusable(false);
            dateEditText.setClickable(true);
        });

    button.setOnClickListener(this::addObservation);

    addImageButton.setOnClickListener(this::addImage);

    }

    private void addObservation(View view){
        String category, criterium, accuracy, habitat, additional_info;
        int number, rare;


        //collecting info from form
        String email = firebaseUser.getEmail();
        speciesObservation = speciesMenu.getText().toString();
        date = Objects.requireNonNull(dateEditText.getText()).toString();
        category = categoriesMenu.getText().toString();
        criterium = criterionMenu.getText().toString();
        accuracy = accuracyMenu.getText().toString();
        name = Objects.requireNonNull(observerNameEditText.getText()).toString();
        habitat = habitatsMenu.getText().toString();
        additional_info = Objects.requireNonNull(additionalInfoEditText.getText()).toString();
        place = Objects.requireNonNull(placeEditText.getText()).toString().trim();
        county = Objects.requireNonNull(countyEditText.getText()).toString().trim();
        number = Integer.parseInt(Objects.requireNonNull(numberEditText.getText()).toString());

        //disallowing user to upload multiple files for single observation
        if(storageTask == null) {
            uploadFile();
        }

        //validation
        //full name is too short
        if(name.length()<6){
            observerNameEditText.setError("Nazwisko obserwatora jest zbyt krótkie");
            observerNameEditText.requestFocus();
            return;
        }

        //first character of place must be uppercase
        if(!Character.isUpperCase(place.charAt(0))){
            placeEditText.setError("Nazwa miejscowości powinna zaczynać się z wielkiej litery");
            placeEditText.requestFocus();
            return;
        }

        //first character of county must be uppercase
        if(!Character.isUpperCase(county.charAt(0))){
            countyEditText.setError("Nazwa gminy powinna zaczynać się z wielkiej litery");
            countyEditText.requestFocus();
            return;
        }

        //number of birds cant be over 50000
        if(number > 50000){
            numberEditText.setError("Liczba jest zbyt duża");
            numberEditText.requestFocus();
            return;
        }

        //name and surname should be separeted by space or dot if user wants to provide only first character of surname
        if(!(name.contains(" ") || name.contains("."))){
            observerNameEditText.setError("Imie i nazwisko powinno być oddzielone spacją lub kropką w przypadku gdy podana jest tylko pierwsza litera nazwiska lub imienia");
            observerNameEditText.requestFocus();
            return;
        }

        //additional info shouldnt be longer than 500 characters
        if(additional_info.length()>500){
            additionalInfoEditText.setError("Podany tekst jest zbyt długi (max 500 znakow");
            additionalInfoEditText.requestFocus();
            return;
        }

        //specifying how rare is the observation (from 0 to 3)
        if(speciesObservation.equals("Łabędź czarnodzioby")||speciesObservation.equals("Ślepowron")||speciesObservation.equals("Dzierlatka"))
            rare = 1;
        else if(speciesObservation.equals("Lodowiec")||speciesObservation.equals("Czajka towarzyska"))
            rare = 3;
        else if(speciesObservation.equals("Bocian czarny")||speciesObservation.equals("Zielonka")||speciesObservation.equals("Turkawka"))
            rare = 0;
        else rare = 2;

        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date dateObservation = dateFormat.parse(date);
            Date currentDate = new Date();
            dateFormat.format(currentDate);
            if(dateObservation.after(currentDate)){
                dateEditText.setError("Podana data jest nieprawidłowa");
                dateEditText.requestFocus();
                return;
            }
            String id = firebaseDatabase.push().getKey();

            if(!(latLngText==null || latLngText.equals(""))){
                Observation observation = new Observation(email,speciesObservation,date,category,criterium,accuracy,name,habitat,additional_info,place,county,latLngText,number,rare);
                firebaseDatabase.child(id).setValue(observation);
                Toast.makeText(AddObservationActivity.this,"Obserwacja zostala dodana",Toast.LENGTH_SHORT).show();
            }else Toast.makeText(AddObservationActivity.this,"Zaznacz miejsce na mapie",Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //called when user wants to add image, allows to choose image from gallery
    private void addImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    //used to get file extension of image chosen by user
    private String getFileExtensions(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //allows user to upload file to db
    private void uploadFile(){
        if(imageURI != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtensions(imageURI));
            storageTask = fileReference.putFile(imageURI).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(AddObservationActivity.this, "Udało się zapisać plik", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                Image image = new Image(firebaseUser.getEmail(),downloadUrl.toString(), speciesObservation, name, place, county, date);
                String uploadId = firebaseDatabase2.push().getKey();
                firebaseDatabase2.child(uploadId).setValue(image);
            }).addOnFailureListener(e -> Toast.makeText(AddObservationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show());
        }else{
            Toast.makeText(this,"Nie wybrano pliku",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageURI = data.getData();
        }
    }

    //map initialization
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //default map coordinates
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.183835, 22.585523)));
        map.setOnMapClickListener(latLng -> {
            if(marker!= null){
                marker.remove();
            }
            marker = map.addMarker(new MarkerOptions().position(latLng));
            latLngText = latLng.toString();
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
