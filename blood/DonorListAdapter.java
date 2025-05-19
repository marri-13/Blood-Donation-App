package com.example.blood;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Random;

public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.ViewHolder> {
    private final Context context;
    private final List<Donor> donorList;
    private final Random random = new Random();

    public DonorListAdapter(Context context, List<Donor> donorList) {
        this.context = context;
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donor donor = donorList.get(position);
        holder.txtName.setText(donor.getName());
        holder.txtBloodGroup.setText(donor.getBloodGroup());
        holder.txtLocation.setText(donor.getLocation());

        // Generate a random color for each name
        int randomColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        holder.txtName.setTextColor(randomColor);
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtBloodGroup, txtLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtBloodGroup = itemView.findViewById(R.id.txtBloodGroup);
            txtLocation = itemView.findViewById(R.id.txtLocation);
        }
    }
}
