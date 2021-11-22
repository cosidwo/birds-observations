package com.example.obserwacje.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.obserwacje.R;
import com.example.obserwacje.entities.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//activity allowing user to display and change personal information
public class ProfileActivity extends AppCompatActivity {

    private MaterialTextView emailTextView, nameTextView;
    private TextInputEditText changeNameInput, changeSurnameInput;
    Button acceptChagnesButton;

    private DatabaseReference dbReference, updateUserReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    String email, id, userFullName;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing objects
        emailTextView = (MaterialTextView) findViewById(R.id.emailTextView);
        nameTextView = (MaterialTextView) findViewById(R.id.nameTextView);
        changeNameInput = (TextInputEditText) findViewById(R.id.changeNameInput);
        changeSurnameInput = (TextInputEditText) findViewById(R.id.changeSurnameInput);
        acceptChagnesButton = (Button) findViewById(R.id.acceptChangesButton);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        updateUserReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        email = firebaseUser.getEmail();
        id = firebaseUser.getUid();
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //fetching data from firebase db
                Iterable<DataSnapshot> children = snapshot.getChildren();
                ArrayList<User> users = new ArrayList();
                for(DataSnapshot child : children){
                    User user = child.getValue(User.class);
                    users.add(user);
                }
                for(User user : users){
                    if(user.getEmail().equals(email)){
                        //displaying name and surename
                        nameTextView.setText("Imie i nazwisko: "+ user.getDisplayName2());
                        userFullName = user.getDisplayName2();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        emailTextView.setText("Email: "+ email);

        acceptChagnesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptChanges(v);
            }
        });
    }

    //called when user wants to make changes in his profile
    private void acceptChanges(View view){
        String name = changeNameInput.getText().toString().trim();
        String surname = changeSurnameInput.getText().toString().trim();
        String [] nameAndSurname = userFullName.split(" ");

        //surname and name fields cant be empty, personal info is changed if the conditions are met
        if(!(surname.isEmpty()) && !(name.isEmpty())){
            updateUserReference.child("DisplayName").setValue(name +" "+ surname);
        }else if(surname.isEmpty() && !(name.isEmpty())){
            surname = nameAndSurname[1];
            updateUserReference.child("DisplayName").setValue(name+ " "+ surname);
        }else if(name.isEmpty() && (!surname.isEmpty())){
            name = nameAndSurname[0];
            updateUserReference.child("DisplayName").setValue(name+" "+surname);
        }else{
            Toast.makeText(ProfileActivity.this,"Wprowadź dane",Toast.LENGTH_SHORT).show();
        }
    }
}
