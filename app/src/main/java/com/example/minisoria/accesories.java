package com.example.minisoria;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class accesories extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accesories, container, false);

        ImageView myImageView = view.findViewById(R.id.accessories1);
        ImageView myImageview1 = view.findViewById(R.id.accessories2);
        ImageView myImageview2 = view.findViewById(R.id.accessories3);
        ImageView myImageview3 = view.findViewById(R.id.accessories4);
        ImageView myImageview4 = view.findViewById(R.id.accessories5);
        ImageView myImageview5 = view.findViewById(R.id.accessories6);

        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), product.class);
                startActivity(intent);
            }
        });

        myImageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Productbracelet.class);
                startActivity(intent);
            }
        });

        myImageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Producttotebag.class);
                startActivity(intent);
            }
        });

        myImageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Productkeychain.class);
                startActivity(intent);
            }
        });
        myImageview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Producttoys.class);
                startActivity(intent);
            }
        });
        myImageview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Productclothes.class);
                startActivity(intent);
            }
        });

        return view;

    }
}