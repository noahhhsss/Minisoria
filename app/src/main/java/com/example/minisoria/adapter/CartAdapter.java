package com.example.minisoria.adapter;

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

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnPriceUpdateListener {
        void onPriceUpdated();
    }

    public interface OnCartItemCheckoutListener {
        void onCheckoutItem(Cartitem item);
    }

    private List<Cartitem> cartItems;
    private OnPriceUpdateListener listener;
    private boolean showCheckbox;
    private OnCartItemCheckoutListener checkoutListener;

    public CartAdapter(List<Cartitem> cartItems, boolean showCheckbox,
                       OnPriceUpdateListener listener, OnCartItemCheckoutListener checkoutListener) {
        this.cartItems = new ArrayList<>(cartItems);  // defensive copy
        this.listener = listener;
        this.showCheckbox = showCheckbox;
        this.checkoutListener = checkoutListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordercheckout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cartitem item = cartItems.get(position);

        holder.checkBox.setOnCheckedChangeListener(null);  // clear old listener

        if (showCheckbox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(item.isChecked());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setChecked(isChecked);
                if (checkoutListener != null) {
                    checkoutListener.onCheckoutItem(item);
                }
                if (listener != null) {
                    listener.onPriceUpdated();
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

        holder.title.setText(item.getTitle());
        holder.price.setText(item.getPrice());
        holder.material.setText(item.getMaterial());
        holder.quantityText.setText(String.valueOf(item.getQuantity()));

        try {
            float unitPrice = Float.parseFloat(item.getPrice().replace("₱", "").trim());
            float total = unitPrice * item.getQuantity();
            holder.totalPrice.setText("₱" + String.format("%.2f", total));
        } catch (Exception e) {
            holder.totalPrice.setText("₱0.00");
        }

        holder.addButton.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            if (listener != null) listener.onPriceUpdated();
        });

        holder.minusButton.setOnClickListener(v -> decreaseQuantity(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateData(List<Cartitem> newItems) {
        cartItems.clear();
        cartItems.addAll(newItems);
        notifyDataSetChanged();
    }

    public void decreaseQuantity(int position) {
        if (position >= 0 && position < cartItems.size()) {
            Cartitem item = cartItems.get(position);
            int qty = item.getQuantity();
            if (qty > 1) {
                item.setQuantity(qty - 1);
                notifyItemChanged(position);
            } else {
                cartItems.remove(position);
                notifyItemRemoved(position);
                CartManager.removeFromCart(item);
                notifyItemRangeChanged(position, cartItems.size());
            }
            if (listener != null) listener.onPriceUpdated();
        }

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
}
