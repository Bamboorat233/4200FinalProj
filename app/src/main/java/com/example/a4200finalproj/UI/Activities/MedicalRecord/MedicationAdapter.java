package com.example.a4200finalproj.UI.Activities.Medication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.Medication;
import com.example.a4200finalproj.R;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(Medication medication); }
    public interface OnDeleteClickListener { void onDelete(Medication medication); }

    private final List<Medication> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public MedicationAdapter(List<Medication> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication m = list.get(position);
        holder.tvName.setText(m.getName());
        holder.tvGenericName.setText(m.getGenericName() != null ? m.getGenericName() : "No generic name");
        holder.tvDosage.setText(m.getDosage() != null ? m.getDosage() : "No dosage");
        holder.tvForm.setText(m.getForm() != null ? m.getForm() : "No form");
        holder.tvManufacturer.setText(m.getManufacturer() != null ? m.getManufacturer() : "No manufacturer");
        holder.itemView.setOnClickListener(v -> clickListener.onClick(m));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(m));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGenericName, tvDosage, tvForm, tvManufacturer;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvName         = itemView.findViewById(R.id.tvName);
            tvGenericName  = itemView.findViewById(R.id.tvGenericName);
            tvDosage       = itemView.findViewById(R.id.tvDosage);
            tvForm         = itemView.findViewById(R.id.tvForm);
            tvManufacturer = itemView.findViewById(R.id.tvManufacturer);
            btnDelete      = itemView.findViewById(R.id.btnDelete);
        }
    }
}