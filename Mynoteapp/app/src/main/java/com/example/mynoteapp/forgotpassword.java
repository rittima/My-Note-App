package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {

    private EditText mForgetEmailField;
    private Button mPasswordRecoverButton;
    private TextView mLoginGoBack;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpassword);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        mForgetEmailField = findViewById(R.id.forgotEmailField);  // Correct input field
        mPasswordRecoverButton = findViewById(R.id.sendResetLinkButton);
        mLoginGoBack = findViewById(R.id.backToLogin);
        auth = FirebaseAuth.getInstance();

        // Back to login
        mLoginGoBack.setOnClickListener(v -> {
            startActivity(new Intent(forgotpassword.this, MainActivity.class));
        });

        // Send reset link
        mPasswordRecoverButton.setOnClickListener(v -> {
            String email = mForgetEmailField.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_SHORT).show();
            } else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Reset link sent to your email", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(forgotpassword.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to send reset link", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
