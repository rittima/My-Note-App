package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails extends AppCompatActivity {

    TextView titleofnotedetail, contentofnotedetail;
    FloatingActionButton gotoedit;
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        // Initialize views
        titleofnotedetail = findViewById(R.id.titleofnotedetail);
        contentofnotedetail = findViewById(R.id.contentofnotedetail);
        gotoedit = findViewById(R.id.gotoedit);
        Toolbar toolbar = findViewById(R.id.toolbarofnotedetail);
        setSupportActionBar(toolbar);

        // Get note details from Intent
        data = getIntent();
        if (data != null) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            String noteId = data.getStringExtra("noteId");

            // Null checks for title, content, and noteId
            if (title != null && content != null && noteId != null) {
                titleofnotedetail.setText(title);
                contentofnotedetail.setText(content);
            } else {
                Toast.makeText(this, "Error: Missing note details", Toast.LENGTH_SHORT).show();
            }

            // Set up the edit button listener
            gotoedit.setOnClickListener(view -> {
                Intent intent = new Intent(notedetails.this, editnoteactivity.class);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("noteId", noteId);
                startActivity(intent);
            });
        } else {
            Toast.makeText(this, "Error: No note data received", Toast.LENGTH_SHORT).show();
        }
    }
}
