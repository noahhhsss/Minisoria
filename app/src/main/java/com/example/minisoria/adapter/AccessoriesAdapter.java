package com.example.minisoria.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.ProductDetail;
import com.example.minisoria.R;
import com.example.minisoria.model.Accessory;

import java.util.List;

public class AccessoriesAdapter extends RecyclerView.Adapter<AccessoriesAdapter.ViewHolder> {

    private List<Accessory> accessories;
    private Context context;

    public AccessoriesAdapter(Context context, List<Accessory> accessories) {
        this.context = context;
        this.accessories = accessories;
    }

    @NonNull
    @Override
    public AccessoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.productaccessory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccessoriesAdapter.ViewHolder holder, int position) {
        Accessory accessory = accessories.get(position);
        holder.productImage.setImageResource(accessory.getImageResId());
        holder.productName.setText(accessory.getName());
        holder.productPrice.setText(String.format("%.2f", accessory.getPrice()));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetail.class);
            intent.putExtra("name", accessory.getName());
            intent.putExtra("price", String.valueOf(accessory.getPrice())); // send as String
            intent.putExtra("description", accessory.getDescription());
            intent.putExtra("imageResId", accessory.getImageResId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return accessories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
        }
    }
}
