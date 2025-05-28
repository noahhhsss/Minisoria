package com.example.minisoria.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Product implements Parcelable {
    private int id;
    private String imageUri;   // changed from int imageRes
    private String name;
    private double price;
    private String description;
    private ArrayList<String> materials;

    public Product(int id, String imageUri, String name, double price, String description, ArrayList<String> materials) {
        this.id = id;
        this.imageUri = imageUri;
        this.name = name;
        this.price = price;
        this.description = description;
        this.materials = materials;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        imageUri = in.readString();  // changed from readInt()
        name = in.readString();
        price = in.readDouble();
        description = in.readString();
        materials = in.createStringArrayList();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() { return id; }
    public String getImageUri() { return imageUri; }  // updated getter
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public ArrayList<String> getMaterials() { return materials; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(imageUri);  // changed from writeInt()
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeString(description);
        parcel.writeStringList(materials);
    }
}
