package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {

    EditText medittitleofnote, meditcontentofnote;
    FloatingActionButton msaveeditnote;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editnoteactivity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        medittitleofnote = findViewById(R.id.edittitleofnote);
        meditcontentofnote = findViewById(R.id.editcontentofnote);
        msaveeditnote = findViewById(R.id.update);

        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get Intent data
        Intent data = getIntent();
        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");
        String noteId = data.getStringExtra("noteId");

        // Pre-fill the form
        medittitleofnote.setText(noteTitle);
        meditcontentofnote.setText(noteContent);

        msaveeditnote.setOnClickListener(v -> {
            String newTitle = medittitleofnote.getText().toString().trim();
            String newContent = meditcontentofnote.getText().toString().trim();

            if (newTitle.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update Firestore note
            DocumentReference docRef = firebaseFirestore
                    .collection("notes")
                    .document(firebaseUser.getUid())
                    .collection("myNotes")
                    .document(noteId);

            Map<String, Object> note = new HashMap<>();
            note.put("title", newTitle);
            note.put("content", newContent);

            docRef.set(note)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "Note updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(editnoteactivity.this, noteactivity.class));
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getApplicationContext(), "Failed to update note", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
