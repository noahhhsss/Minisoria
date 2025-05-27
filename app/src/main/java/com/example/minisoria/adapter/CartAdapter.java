package com.example.minisoria.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    // Updated constructor to accept showCheckbox
    public CartAdapter(List<Cartitem> itemList, boolean showCheckbox, OnPriceUpdateListener listener) {
        this.itemList = itemList;
        this.listener = listener;
        this.showCheckbox = showCheckbox;
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
        if (showCheckbox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(item.isChecked());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setChecked(isChecked);
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.title.setText(item.getTitle());
        holder.price.setText(item.getPrice());
        holder.material.setText(item.getMaterial());
        holder.quantityText.setText(String.valueOf(item.getQuantity()));
        holder.imageView.setImageResource(item.getImageResId());

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
        Cartitem item = itemList.get(position);
        int qty = item.getQuantity();
        if (qty > 1) {
            item.setQuantity(qty - 1);
        } else {
            itemList.remove(position);
        }
        notifyDataSetChanged();
        if (listener != null) listener.onPriceUpdated();
    }
}
