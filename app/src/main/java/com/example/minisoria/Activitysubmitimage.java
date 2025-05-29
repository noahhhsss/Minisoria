package com.example.minisoria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.minisoria.adapter.UriTypeAdapter;
import com.example.minisoria.model.Cartitem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activitysubmitimage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private ImageView imagePreview;
    private Button submitButton, chooseImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysubmitimage);

        // Clear corrupted or invalid submission_list data to prevent Gson errors


        imagePreview = findViewById(R.id.imagePreview);
        submitButton = findViewById(R.id.submitButton);
        chooseImageButton = findViewById(R.id.chooseImageButton);

        chooseImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        submitButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String username = prefs.getString("username", "Anonymous");

            if (selectedImageUri != null) {
                Cartitem submission = new Cartitem(
                        username, "", "", 0, "", 0, selectedImageUri
                );

                // Use Gson with UriTypeAdapter registered for both reading and writing
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriTypeAdapter())
                        .create();

                String json = prefs.getString("submission_list", "[]");

                Type type = new TypeToken<ArrayList<Cartitem>>() {}.getType();
                ArrayList<Cartitem> submissions = gson.fromJson(json, type);
                if (submissions == null) {
                    submissions = new ArrayList<>();  // Initialize if null
                }

                submissions.add(submission);

                prefs.edit().putString("submission_list", gson.toJson(submissions)).apply();

                Toast.makeText(this, "Submitted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            final int takeFlags = data.getFlags() &
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);

            imagePreview.setImageURI(selectedImageUri);
        }
    }
}
