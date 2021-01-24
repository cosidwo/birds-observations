package com.example.obserwacje;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText email_register, password_register, imie_register, nazwisko_register;
    private Button registerButton;
    private Intent loginIntent;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        email_register = (TextInputEditText) findViewById(R.id.email_register);
        password_register = (TextInputEditText) findViewById(R.id.password_register);
        imie_register = (TextInputEditText) findViewById(R.id.imie_register);
        nazwisko_register = (TextInputEditText) findViewById(R.id.nazwisko_register);

        registerButton = (Button) findViewById(R.id.registerButton);

        loginIntent = new Intent(this,MainActivity.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    public void register(View view){
        String email, password, imie, nazwisko;
        email = email_register.getText().toString().trim();
        password = password_register.getText().toString().trim();
        imie = imie_register.getText().toString().trim();
        nazwisko = nazwisko_register.getText().toString().trim();

        if(email.isEmpty()){
            email_register.setError("Pole nie może być puste");
            email_register.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_register.setError("Podany email jest nieprawidłowy");
            email_register.requestFocus();
            return;
        }

        if(password.length()<6){
            password_register.setError("Podane hasło jest zbyt krótkie");
            password_register.requestFocus();
            return;
        }

        if(imie.matches(".*\\d.*")){
            imie_register.setError("Podano nieprawidłowe imię");
            imie_register.requestFocus();
            return;
        }

        if(nazwisko.matches(".*\\d.*")){
            nazwisko_register.setError("Podano nieprawidłowe imię");
            nazwisko_register.requestFocus();
            return;
        }

        if(!Character.isUpperCase(imie.charAt(0))){
            imie_register.setError("Imię musi zaczynać się z wielkiej litery");
            imie_register.requestFocus();
            return;
        }

        if(!Character.isUpperCase(nazwisko.charAt(0))){
            nazwisko_register.setError("Imię musi zaczynać się z wielkiej litery");
            nazwisko_register.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(email,password,imie,nazwisko);

                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Zarejestrowano",Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(RegisterActivity.this,"Rejestracja nie powiodła się", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else Toast.makeText(RegisterActivity.this,"Rejestracja nie powiodła się", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
