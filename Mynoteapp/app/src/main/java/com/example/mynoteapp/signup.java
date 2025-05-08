package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText esignupemail, esignuppassword;
    private RelativeLayout esignup;
    private TextView mlogingoback;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        esignupemail = findViewById(R.id.emailField);
        esignuppassword = findViewById(R.id.passwordField);
        esignup = findViewById(R.id.signUpButton);
        mlogingoback = findViewById(R.id.loginLink);

        auth = FirebaseAuth.getInstance();

        mlogingoback.setOnClickListener(v -> {
            Intent intent = new Intent(signup.this, MainActivity.class);
            startActivity(intent);
        });

        esignup.setOnClickListener(v -> {
            String email = esignupemail.getText().toString().trim();
            String password = esignuppassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email and Password are required", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                // Register the user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to register: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Verification link sent to your email. Please verify and log in again.", Toast.LENGTH_LONG).show();
                    auth.signOut();
                    finish();
                    startActivity(new Intent(signup.this, MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to send verification link", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
