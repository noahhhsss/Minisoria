package com.example.minisoria;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.adapter.AdminSubmissionAdapter;
import com.example.minisoria.adapter.UriTypeAdapter;
import com.example.minisoria.model.Cartitem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Adminattachimage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachadmin);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Use the same SharedPreferences name used in Activitysubmitimage
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Register the UriTypeAdapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(android.net.Uri.class, new UriTypeAdapter())
                .create();

        String json = prefs.getString("submission_list", "[]");
        Type type = new TypeToken<ArrayList<Cartitem>>() {}.getType();
        ArrayList<Cartitem> submissions = gson.fromJson(json, type);

        AdminSubmissionAdapter adapter = new AdminSubmissionAdapter(this, submissions);
        recyclerView.setAdapter(adapter);
    }
}
