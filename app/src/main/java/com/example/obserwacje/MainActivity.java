package com.example.obserwacje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//login form, app starts with this activity
public class MainActivity extends AppCompatActivity {

    Intent registerIntent;
    Intent loginIntent;

    Button loginButton;
    Button registerButton;

    TextInputEditText email;
    TextInputEditText password;

    private FirebaseAuth mAuth;

    //initializng objects and setting click listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerIntent = new Intent(this,RegisterActivity.class);
        loginIntent = new Intent(this,DashboardActivity.class);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton= (Button) findViewById(R.id.registerButton);

        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(v);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity(v);
            }
        });
    }

    //called when user wants to log in
    public void logIn(View view){
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        //email field cant be empty
        if(emailText.isEmpty()){
            email.setError("Podaj e-mail");
            email.requestFocus();
            return;
        }

        //password field cant be empty
        if(passwordText.isEmpty()){
            password.setError("Podaj haslo");
            password.requestFocus();
            return;
        }

        //firebase authentication
        mAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Logowanie powiodło się",Toast.LENGTH_SHORT).show();
                    startActivity(loginIntent);
                }else Toast.makeText(MainActivity.this,"Wprowadź prawidłowe dane",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void goToRegisterActivity(View view){
        startActivity(registerIntent);
    }
}