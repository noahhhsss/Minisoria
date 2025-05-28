// com/example/minisoria/ProductManager.java
package com.example.minisoria;

import com.example.minisoria.model.Accessory;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static final List<Accessory> accessories = new ArrayList<>();


    public static List<Accessory> getAccessories() {
        return accessories;
    }

    public static void addAccessory(Accessory a) {
        accessories.add(a);
    }
}
