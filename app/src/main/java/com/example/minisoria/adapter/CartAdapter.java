package com.example.minisoria.adapter;

import static com.example.minisoria.CartManager.cartItems;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.minisoria.CartManager;
import com.example.minisoria.R;
import com.example.minisoria.model.Cartitem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnPriceUpdateListener {
        void onPriceUpdated();
    }

    private List<Cartitem> itemList;
    private OnPriceUpdateListener listener;
    private boolean showCheckbox; // <-- added this flag
    private OnCartItemCheckoutListener checkoutListener;

    // Updated constructor to accept showCheckbox
    public CartAdapter(List<Cartitem> itemList, boolean showCheckbox, OnPriceUpdateListener listener) {
        this.itemList = itemList;
        this.listener = listener;
        this.showCheckbox = showCheckbox;
        this.checkoutListener = checkoutListener;
    }
    public interface OnCartItemCheckoutListener {
        void onCheckoutItem(Cartitem item);
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordercheckout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cartitem item = itemList.get(position);

        // Show or hide checkbox based on showCheckbox flag
        holder.checkBox.setOnCheckedChangeListener(null); // to prevent unwanted callbacks during recycling

        if (showCheckbox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(item.isChecked());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setChecked(isChecked);

                if (isChecked) {
                    // If you want only one item to be checked at a time, uncheck others:
                    for (Cartitem otherItem : itemList) {
                        if (otherItem != item) {
                            otherItem.setChecked(false);
                        }
                    }
                    notifyDataSetChanged();

                    // Trigger checkout callback for this single item
                    if (checkoutListener != null) {
                        checkoutListener.onCheckoutItem(item);
                    }
                }
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        if (item.getImageUri() != null) {
            Glide.with(holder.imageView.getContext())
                    .load(item.getImageUri())
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(item.getImageResId());
        }
        Log.d("CartAdapter", "ImageUri: " + item.getImageUri());



        holder.title.setText(item.getTitle());
        holder.price.setText(item.getPrice());
        holder.material.setText(item.getMaterial());
        holder.quantityText.setText(String.valueOf(item.getQuantity()));
        if (item.getImageUri() != null) {
            Glide.with(holder.imageView.getContext())
                    .load(item.getImageUri())
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(item.getImageResId());
        }

        try {
            float unitPrice = Float.parseFloat(item.getPrice().replace("₱", ""));
            float total = unitPrice * item.getQuantity();
            holder.totalPrice.setText("₱" + String.format("%.2f", total));
        } catch (Exception e) {
            holder.totalPrice.setText("₱0.00");
        }

        holder.addButton.setOnClickListener(v -> {
            int qty = item.getQuantity() + 1;
            item.setQuantity(qty);
            notifyItemChanged(position);
            if (listener != null) listener.onPriceUpdated();
        });

        holder.minusButton.setOnClickListener(v -> {
            decreaseQuantity(position);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, totalPrice, quantityText, addButton, minusButton, material;
        ImageView imageView;
        CheckBox checkBox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox1);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            totalPrice = itemView.findViewById(R.id.totalprice);
            quantityText = itemView.findViewById(R.id.quantityText);
            addButton = itemView.findViewById(R.id.addButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            imageView = itemView.findViewById(R.id.imageView7);
            material = itemView.findViewById(R.id.material);
        }
    }

    public void decreaseQuantity(int position) {
        if (position >= 0 && position < itemList.size()) {

            Cartitem item = itemList.get(position);
            int qty = item.getQuantity();
            if (qty > 1) {
                item.setQuantity(qty - 1);
                notifyItemChanged(position);
            } else {
                itemList.remove(position);
                notifyItemRemoved(position);
                CartManager.removeFromCart(item);
                notifyItemRangeChanged(position, itemList.size()); //   Optional: keeps RecyclerView consistent
            }

            if (listener != null) listener.onPriceUpdated();
        }

    }

}