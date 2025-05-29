package com.example.minisoria.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Product implements Parcelable {
    private int id;
    private String imageUri;
    private String name;
    private double price;
    private String description;
    private ArrayList<String> materials;
    private ArrayList<Double> materialPrices;
    private String category;  // new field

    public Product(int id, String imageUri, String name, double price, String description,
                   ArrayList<String> materials, ArrayList<Double> materialPrices,
                   String category) {  // add category parameter
        this.id = id;
        this.imageUri = imageUri;
        this.name = name;
        this.price = price;
        this.description = description;
        this.materials = materials;
        this.materialPrices = materialPrices;
        this.category = category;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        imageUri = in.readString();
        name = in.readString();
        price = in.readDouble();
        description = in.readString();
        materials = in.createStringArrayList();
        materialPrices = new ArrayList<>();
        in.readList(materialPrices, Double.class.getClassLoader());
        category = in.readString();  // read category
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

    // Getters
    public int getId() { return id; }
    public String getImageUri() { return imageUri; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public ArrayList<String> getMaterials() { return materials; }
    public ArrayList<Double> getMaterialPrices() { return materialPrices; }
    public String getCategory() { return category; }  // new getter

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(imageUri);
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeString(description);
        parcel.writeStringList(materials);
        parcel.writeList(materialPrices);
        parcel.writeString(category);  // write category
    }
}
