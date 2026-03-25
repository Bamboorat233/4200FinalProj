package com.example.a4200finalproj.UI.Activities.Department;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.Department;
import com.example.a4200finalproj.R;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(Department department); }
    public interface OnDeleteClickListener { void onDelete(Department department); }

    private final List<Department> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public DepartmentAdapter(List<Department> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Department d = list.get(position);
        holder.tvName.setText(d.getName());
        holder.tvLocation.setText(d.getLocation() != null ? d.getLocation() : "No location");
        holder.tvPhone.setText(d.getPhone() != null ? d.getPhone() : "No phone");
        holder.tvDescription.setText(d.getDescription() != null ? d.getDescription() : "");
        holder.itemView.setOnClickListener(v -> clickListener.onClick(d));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(d));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvPhone, tvDescription;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvName        = itemView.findViewById(R.id.tvName);
            tvLocation    = itemView.findViewById(R.id.tvLocation);
            tvPhone       = itemView.findViewById(R.id.tvPhone);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDelete     = itemView.findViewById(R.id.btnDelete);
        }
    }
}