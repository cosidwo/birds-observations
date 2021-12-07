package com.example.obserwacje.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obserwacje.entities.Image;
import com.example.obserwacje.adapters.ImageAdapter;
import com.example.obserwacje.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//activity used to display gallery of images added by users
public class GalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Image> images;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //initializing objects
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        images = new ArrayList<>();

        //getting refference to images from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //fetching images from database and adding them to List object
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Image image = postSnapshot.getValue(Image.class);
                    images.add(image);
                }

                //displaying images from List
                imageAdapter = new ImageAdapter(GalleryActivity.this, images);
                recyclerView.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GalleryActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
