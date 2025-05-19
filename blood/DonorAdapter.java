package com.example.blood;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Random;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {
    private ArrayList<Donor> donorList;
    private Random random;

    public DonorAdapter(ArrayList<Donor> donorList) {
        this.donorList = donorList;
        this.random = new Random();
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donor, parent, false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        Donor donor = donorList.get(position);

        holder.txtName.setText(donor.getName());
        holder.txtBloodGroup.setText(donor.getBloodGroup());
        holder.txtLocation.setText(donor.getLocation());

        // Generate a light background color for readability
        int bgColor = Color.rgb(150 + random.nextInt(106), 150 + random.nextInt(106), 150 + random.nextInt(106));
        holder.itemView.setBackgroundColor(bgColor);

        // Generate a contrasting text color (dark if background is light)
        int textColor = (isDarkColor(bgColor)) ? Color.WHITE : Color.BLACK;
        holder.txtName.setTextColor(textColor);
        holder.txtBloodGroup.setTextColor(textColor);
        holder.txtLocation.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public static class DonorViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtBloodGroup, txtLocation;

        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtBloodGroup = itemView.findViewById(R.id.txtBloodGroup);
            txtLocation = itemView.findViewById(R.id.txtLocation);
        }
    }

    // Helper function to check if a color is dark
    private boolean isDarkColor(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;
        double brightness = (r * 0.299 + g * 0.587 + b * 0.114);
        return brightness < 128;
    }
}
