package com.example.minisoria.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.R;
import com.example.minisoria.model.Order;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> implements Filterable {

    private List<Order> orderList;     // filtered list shown
    private List<Order> orderListFull; // original full list

    private Context context;
    private OnOrderActionListener actionListener;

    public interface OnOrderActionListener {
        void onAccept(Order order, int position);
        void onDelete(Order order, int position);
    }

    // Constructor
    public AdminOrderAdapter(Context context, List<Order> orderList, OnOrderActionListener actionListener) {
        this.context = context;
        this.orderList = orderList;
        this.actionListener = actionListener;

        this.orderListFull = new ArrayList<>(orderList);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adminorderlist, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.userName.setText("User: " + order.getUserName());
        holder.title.setText(order.getTitle());
        holder.material.setText("Material: " + order.getMaterial());
        holder.quantity.setText("Quantity: " + order.getQuantity());
        holder.totalPrice.setText("Total: ₱" + String.format("%.2f", order.getTotalPrice()));
        holder.status.setText("Status: " + order.getStatus());

        holder.accept.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAccept(orderList.get(position), position);
            }
        });

        holder.delete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDelete(order, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public Filter getFilter() {
        return orderFilter;
    }

    private Filter orderFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Order> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(orderListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Order order : orderListFull) {
                    if (order.getUserName().toLowerCase().contains(filterPattern) ||
                            order.getTitle().toLowerCase().contains(filterPattern) ||
                            order.getMaterial().toLowerCase().contains(filterPattern)) {
                        filteredList.add(order);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            orderList.clear();
            orderList.addAll((List<Order>) results.values);
            notifyDataSetChanged();
        }
    };

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView userName, title, material, quantity, totalPrice, status, accept, delete;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.usersname);
            title = itemView.findViewById(R.id.title);
            material = itemView.findViewById(R.id.material);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalprice);
            status = itemView.findViewById(R.id.orderstatus);
            accept = itemView.findViewById(R.id.accept);
            delete = itemView.findViewById(R.id.tvDelete);
        }
    }
}
