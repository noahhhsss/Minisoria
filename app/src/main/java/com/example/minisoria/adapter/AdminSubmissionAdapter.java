package com.example.minisoria.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minisoria.R;
import com.example.minisoria.model.Cartitem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class AdminSubmissionAdapter extends RecyclerView.Adapter<AdminSubmissionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Cartitem> submissions;

    public AdminSubmissionAdapter(Context context, ArrayList<Cartitem> submissions) {
        this.context = context;
        this.submissions = submissions;
    }

    @NonNull
    @Override
    public AdminSubmissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adminattachment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminSubmissionAdapter.ViewHolder holder, int position) {
        Cartitem item = submissions.get(position);
        holder.removeButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                submissions.remove(pos);

                SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriTypeAdapter())
                        .create();
                prefs.edit().putString("submission_list", gson.toJson(submissions)).apply();

                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, submissions.size());
            }
        });


    // Set username
        holder.usernameTextView.setText(item.getUsername());

        // Load image safely from URI
        Uri imageUri = item.getImageUri();
        if (imageUri != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), imageUri);
                    holder.submissionImageView.setImageDrawable(ImageDecoder.decodeDrawable(source));
                } else {
                    holder.submissionImageView.setImageURI(imageUri); // fallback for older Android versions
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.submissionImageView.setImageResource(R.drawable.roundbluedashboard); // fallback image
            }
        } else {
            holder.submissionImageView.setImageResource(R.drawable.roundbluedashboard); // fallback image
        }
    }

    @Override
    public int getItemCount() {
        return submissions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView submissionImageView;
        TextView usernameTextView;
        Button removeButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            submissionImageView = itemView.findViewById(R.id.itemImage);
            usernameTextView = itemView.findViewById(R.id.itemUsername);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
