package com.example.minisoria;

import com.example.minisoria.model.Cartitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartManager {
    public static final List<Cartitem> cartItems = new ArrayList<>();


    public static void addToCart(Cartitem newItem) {
        for (Cartitem item : cartItems) {
            if (item.getTitle().equals(newItem.getTitle()) &&
                    Objects.equals(item.getMaterial(), newItem.getMaterial())) {
                int updatedQuantity = item.getQuantity() + newItem.getQuantity();
                item.setQuantity(updatedQuantity);
                return;
            }
        }

        cartItems.add(newItem);
    }

    // Get current cart items
    public static List<Cartitem> getCartItems() {
        return cartItems;
    }



    // Clear the cart
    public static void clearCart() {
        cartItems.clear();
    }

    public static void removeFromCart(Cartitem itemToRemove) {
        // Remove the matching item by title and material
        cartItems.removeIf(item ->
                item.getTitle().equals(itemToRemove.getTitle()) &&
                        Objects.equals(item.getMaterial(), itemToRemove.getMaterial())
        );
    }


}
