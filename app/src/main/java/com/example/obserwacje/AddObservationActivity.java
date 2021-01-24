package com.example.obserwacje;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private Button button;
    private Button addImageButton;
    private AutoCompleteTextView speciesMenu;
    private AutoCompleteTextView categoriesMenu;
    private AutoCompleteTextView criterionMenu;
    private AutoCompleteTextView accuracyMenu;
    private AutoCompleteTextView habitatsMenu;
    private TextInputLayout textInputLayout;
    private TextInputEditText dateEditText;
    private TextInputEditText observerNameEditText;
    private TextInputEditText additionalInfoEditText;
    private TextInputEditText placeEditText;
    private TextInputEditText countyEditText;
    private TextInputEditText numberEditText;

    private static String [] species = {"Łabędź czarnodzioby", "Gęś mała", "Hełmiatka", "Perkoz rogaty", "Lodowiec", "Cietrzew","Orzeł przedni", "Czapla nadobna", "Turkawka", "Bocian czarny", "Ślepowron", "Zielonka", "Czajka towarzyska", "Mewa romańska", "Dzierlatka"};
    private static String [] categories = {"Lęgowe", "Wszystkie", "Fenologiczne", "Stado", "Martwe"};
    private static String [] criterion = {"Brak", "O - Pojedyncze ptaki w siedlisku lęgowym", "P - para ptaków w siedlisku lęgowym", "BU - budowa gniazda", "WYS - wysiadywanie na gnieździe", "MŁO - młode zagniazdowniki poza gniazdem"};
    private static String [] numberType = {"Liczba dokładna", "Liczba szacunkowa", "Liczba przybliżona (na oko)"};
    private static String [] habitats = {"Aleja przydrożna", "Las liściasty", "Las mieszany", "Las iglasty", "Ols", "Łęg", "Park", "Jezioro", "Staw", "Rzeka", "Łąka", "Rozlewisko", "Pastwisko", "Pole uprawne", "Miasto", "Wieś", "Kopalnia", "Wysypisko śmieci", "Inne"};

    private String email, speciesObservation, name, place, county, date;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        button = (Button) findViewById(R.id.addObservationButton);
        addImageButton = (Button) findViewById(R.id.addImage);
        speciesMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu);
        categoriesMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu2);
        criterionMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu3);
        accuracyMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu4);
        habitatsMenu = (AutoCompleteTextView) findViewById(R.id.dropdown_menu5);
        textInputLayout = (TextInputLayout) findViewById(R.id.date);
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

        //PROBA WYLACZENIA WYBORU KRYTERIUM LEGOWOSCI NA STARCIE
        criterionMenu.setFocusable(false);
        criterionMenu.setFocusableInTouchMode(false);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Wybierze date");
        final MaterialDatePicker datePicker = builder.build();
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(),"DATE PICKER");
            }
        });

        //PROBA WLACZENIA KRYTERIUM LEGOWOSCI PO WYBRANIU KATEGORII LEGOWEJ
        categoriesMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoriesMenu.getText().toString()=="Lęgowe"){
                    criterionMenu.setFocusable(true);
                    criterionMenu.setFocusableInTouchMode(true);
                }
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dateEditText.setText(""+datePicker.getHeaderText());
                dateEditText.setFocusable(false);
                dateEditText.setClickable(true);
            }
        });

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addObservation(v);
        }
    });

    addImageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addImage(v);
        }
    });

    }

    private void addObservation(View view){
        String category, criterium, accuracy, habitat, additional_info;
        int number, rare;

        email = firebaseUser.getEmail();
        speciesObservation = speciesMenu.getText().toString();
        date = dateEditText.getText().toString();
        category = categoriesMenu.getText().toString();
        criterium = criterionMenu.getText().toString();
        accuracy = accuracyMenu.getText().toString();
        name = observerNameEditText.getText().toString();
        habitat = habitatsMenu.getText().toString();
        additional_info = additionalInfoEditText.getText().toString();
        place = placeEditText.getText().toString().trim();
        county = countyEditText.getText().toString().trim();
        number = Integer.parseInt(numberEditText.getText().toString());


        if(storageTask == null) {
            uploadFile();
        }

        if(name.length()<7){
            observerNameEditText.setError("Nazwisko obserwatora jest zbyt krótkie");
            observerNameEditText.requestFocus();
            return;
        }

        if(!Character.isUpperCase(place.charAt(0))){
            placeEditText.setError("Nazwa miejscowości powinna zaczynać się z wielkiej litery");
            placeEditText.requestFocus();
            return;
        }

        if(!Character.isUpperCase(county.charAt(0))){
            countyEditText.setError("Nazwa gminy powinna zaczynać się z wielkiej litery");
            countyEditText.requestFocus();
            return;
        }

        if(number > 50000){
            numberEditText.setError("Liczba jest zbyt duża");
            numberEditText.requestFocus();
            return;
        }
        if(!(name.contains(" ") || name.contains("."))){
            observerNameEditText.setError("Imie i nazwisko powinno być oddzielone spacją lub kropką w przypadku gdy podana jest tylko pierwsza litera nazwiska lub imienia");
            observerNameEditText.requestFocus();
            return;
        }

        if(additional_info.length()>500){
            additionalInfoEditText.setError("Podany tekst jest zbyt długi (max 500 znakow");
            additionalInfoEditText.requestFocus();
            return;
        }

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

            if(!(latLngText==null || latLngText=="")){
                Observation observation = new Observation(email,speciesObservation,date,category,criterium,accuracy,name,habitat,additional_info,place,county,latLngText,number,rare);
                firebaseDatabase.child(id).setValue(observation);
                Toast.makeText(AddObservationActivity.this,"Obserwacja zostala dodana",Toast.LENGTH_SHORT).show();
            }else Toast.makeText(AddObservationActivity.this,"Zaznacz miejsce na mapie",Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    private void addImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private String getFileExtensions(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(){
        if(imageURI != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtensions(imageURI));
            storageTask = fileReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddObservationActivity.this, "Udało się zapisać plik", Toast.LENGTH_SHORT).show();
                    /*Image image = new Image(firebaseUser.getEmail(),taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    String uploadID = firebaseDatabase2.push().getKey();
                    firebaseDatabase2.child(uploadID).setValue(image);*/
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    Image image = new Image(firebaseUser.getEmail(),downloadUrl.toString(), speciesObservation, name, place, county, date);
                    String uploadId = firebaseDatabase2.push().getKey();
                    firebaseDatabase2.child(uploadId).setValue(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddObservationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.183835, 22.585523)));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(marker!= null){
                    marker.remove();
                }
                marker = map.addMarker(new MarkerOptions().position(latLng));
                latLngText = latLng.toString();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
