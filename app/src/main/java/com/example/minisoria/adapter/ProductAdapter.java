package com.example.minisoria.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.minisoria.ProductDetail;
import com.example.minisoria.R;
import com.example.minisoria.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(Product product, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    private final Context context;
    private List<Product> productList;
    private final OnDeleteClickListener deleteClickListener;
    private final OnItemClickListener itemClickListener;
    private final boolean isAdmin;

    // Admin mode constructor
    public ProductAdapter(Context context, List<Product> productList, boolean isAdmin,
                          OnDeleteClickListener deleteListener, OnItemClickListener itemClickListener) {
        this.context = context;
        this.productList = productList;
        this.isAdmin = isAdmin;
        this.deleteClickListener = deleteListener;
        this.itemClickListener = itemClickListener;
    }

    // User mode constructor
    public ProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.isAdmin = false;
        this.deleteClickListener = null;
        this.itemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return isAdmin ? 1 : 0;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = (viewType == 1) ? R.layout.productitem : R.layout.productaccessory;
        View view = LayoutInflater.from(context).inflate(layoutRes, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (productList == null || position >= productList.size()) return;

        Product product = productList.get(position);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetail.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_image_uri", product.getImageUri());
            context.startActivity(intent);
        });


        holder.name.setText(product.getName());
        holder.price.setText(String.format("₱%.2f", product.getPrice()));
        String imageUri = product.getImageUri();
        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(imageUri))
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.product);
        }

        if (isAdmin && holder.buttonDelete != null) {
            holder.buttonDelete.setVisibility(View.VISIBLE);
            final int pos = position;
            holder.buttonDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(product, pos);
                }
            });
        } else if (holder.buttonDelete != null) {
            holder.buttonDelete.setVisibility(View.GONE);
        }

        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(product));
        } else {
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return (productList == null) ? 0 : productList.size();
    }

    public void removeItem(int position) {
        if (productList != null && position >= 0 && position < productList.size()) {
            productList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, productList.size());
        }
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price;
        Button buttonDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            buttonDelete = itemView.findViewById(R.id.buttonDelete); // may be null in user layout
        }
    }
}
