package com.example.obserwacje;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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
                Iterable<DataSnapshot> children = snapshot.getChildren();
                ArrayList<User> users = new ArrayList();
                for(DataSnapshot child : children){
                    User user = child.getValue(User.class);
                    users.add(user);
                }
                for(User user : users){
                    if(user.getEmail().equals(email)){
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

    private void acceptChanges(View view){
        String name = changeNameInput.getText().toString().trim();
        String surname = changeSurnameInput.getText().toString().trim();
        String [] nameAndSurname = userFullName.split(" ");
        if(!(surname.isEmpty()) && !(name.isEmpty())){
            updateUserReference.child("DisplayName").setValue(name +" "+ surname);
        }else if(surname.isEmpty() && !(name.isEmpty())){
            surname = nameAndSurname[1];
            updateUserReference.child("DisplayName").setValue(name+ " "+ surname);
            Log.d("abc",surname);
        }else if(name.isEmpty() && (!surname.isEmpty())){
            name = nameAndSurname[0];
            updateUserReference.child("DisplayName").setValue(name+" "+surname);
            Log.d("abc",name);
        }else{
            Toast.makeText(ProfileActivity.this,"Wprowadź dane",Toast.LENGTH_SHORT).show();
        }
    }
}
