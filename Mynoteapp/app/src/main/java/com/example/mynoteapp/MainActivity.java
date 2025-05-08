package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    private EditText signinEmailField, signinPasswordField;
    private RelativeLayout loginButton, registerButton;
    private TextView forgotPasswordText;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle system bars (status/nav)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        signinEmailField = findViewById(R.id.emailField);
        signinPasswordField = findViewById(R.id.passwordField);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressbarofmainactivity);

        auth = FirebaseAuth.getInstance();

        // Auto-login if user already authenticated and verified
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            startActivity(new Intent(MainActivity.this, noteactivity.class));
            finish();
        }

        registerButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, signup.class)));

        forgotPasswordText.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, forgotpassword.class)));

        loginButton.setOnClickListener(v -> {
            String email = signinEmailField.getText().toString().trim();
            String password = signinPasswordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        checkEmailVerification();
                    } else {
                        Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, noteactivity.class));
            finish();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please verify your email before logging in", Toast.LENGTH_LONG).show();
            auth.signOut();
        }
    }
}
