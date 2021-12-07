package com.example.obserwacje.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obserwacje.R;
import com.example.obserwacje.entities.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

//activity allowing user to create an account
public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText email_register, password_register, imie_register, nazwisko_register;
    private FirebaseAuth mAuth;

    //initializing objects and setting click listener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        email_register = (TextInputEditText) findViewById(R.id.email_register);
        password_register = (TextInputEditText) findViewById(R.id.password_register);
        imie_register = (TextInputEditText) findViewById(R.id.imie_register);
        nazwisko_register = (TextInputEditText) findViewById(R.id.nazwisko_register);

        Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this::register);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    //called when user clicks "REGISTER" button
    public void register(View view){
        String email, password, imie, nazwisko;
        email = Objects.requireNonNull(email_register.getText()).toString().trim();
        password = Objects.requireNonNull(password_register.getText()).toString().trim();
        imie = Objects.requireNonNull(imie_register.getText()).toString().trim();
        nazwisko = Objects.requireNonNull(nazwisko_register.getText()).toString().trim();

        //email field cant be empty
        if(email.isEmpty()){
            email_register.setError("Pole nie może być puste");
            email_register.requestFocus();
            return;
        }

        //email doesnt match pattern
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_register.setError("Podany email jest nieprawidłowy");
            email_register.requestFocus();
            return;
        }

        //password is too short
        if(password.length()<6){
            password_register.setError("Podane hasło jest zbyt krótkie");
            password_register.requestFocus();
            return;
        }

        //name doesnt match pattern
        if(imie.matches(".*\\d.*")){
            imie_register.setError("Podano nieprawidłowe imię");
            imie_register.requestFocus();
            return;
        }

        //surname doesnt match pattern
        if(nazwisko.matches(".*\\d.*")){
            nazwisko_register.setError("Podano nieprawidłowe imię");
            nazwisko_register.requestFocus();
            return;
        }

        //name isnt uppercase
        if(!Character.isUpperCase(imie.charAt(0))){
            imie_register.setError("Imię musi zaczynać się z wielkiej litery");
            imie_register.requestFocus();
            return;
        }

        //surname isnt uppercase
        if(!Character.isUpperCase(nazwisko.charAt(0))){
            nazwisko_register.setError("Imię musi zaczynać się z wielkiej litery");
            nazwisko_register.requestFocus();
            return;
        }

        //creating user account in firebase db
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                User user = new User(email,password,imie,nazwisko);

                FirebaseDatabase.getInstance().getReference("User").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Zarejestrowano",Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(RegisterActivity.this,"Rejestracja nie powiodła się", Toast.LENGTH_SHORT).show();

                });
            } else Toast.makeText(RegisterActivity.this,"Rejestracja nie powiodła się", Toast.LENGTH_SHORT).show();
        });
    }


}
