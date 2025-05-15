package com.example.minisoria.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.R;
import com.example.minisoria.model.Cartitem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cartitem> itemList;

    public CartAdapter(List<Cartitem> itemList) {
        this.itemList = itemList;
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
        holder.title.setText(item.getTitle());
        holder.price.setText(item.getPrice());
        holder.totalPrice.setText(item.getTotalPrice());
        holder.quantityText.setText(String.valueOf(item.getQuantity()));
        holder.imageView.setImageResource(item.getImageResId());

        // Add logic for + and - buttons
        holder.addButton.setOnClickListener(v -> {
            int qty = item.getQuantity() + 1;
            item.setQuantity(qty);
            holder.quantityText.setText(String.valueOf(qty));
            notifyItemChanged(position);
        });

        holder.minusButton.setOnClickListener(v -> {
            int qty = item.getQuantity();
            if (qty > 1) {
                qty--;
                item.setQuantity(qty);
                holder.quantityText.setText(String.valueOf(qty));
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, totalPrice, quantityText, addButton, minusButton;
        ImageView imageView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            totalPrice = itemView.findViewById(R.id.totalprice);
            quantityText = itemView.findViewById(R.id.quantityText);
            addButton = itemView.findViewById(R.id.addButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            imageView = itemView.findViewById(R.id.imageView7);
        }
    }
}

