package com.example.a4200finalproj.UI.Activities.Prescription;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.Prescription;
import com.example.a4200finalproj.R;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(Prescription prescription); }
    public interface OnDeleteClickListener { void onDelete(Prescription prescription); }

    private final List<Prescription> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public PrescriptionAdapter(List<Prescription> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prescription p = list.get(position);
        holder.tvPatientName.setText(p.getPatientName() != null ? p.getPatientName() : "Patient ID: " + p.getPatientId());
        holder.tvDoctorName.setText("Dr. " + (p.getDoctorName() != null ? p.getDoctorName() : "ID: " + p.getDoctorId()));
        holder.tvMedicationName.setText(p.getMedicationName() != null ? p.getMedicationName() : "Medication ID: " + p.getMedicationId());
        holder.tvDosage.setText(p.getDosage() != null ? p.getDosage() : "No dosage");
        holder.tvFrequency.setText(p.getFrequency() != null ? p.getFrequency() : "No frequency");
        holder.tvDuration.setText(p.getDuration() != null ? p.getDuration() : "No duration");
        holder.itemView.setOnClickListener(v -> clickListener.onClick(p));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(p));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvDoctorName, tvMedicationName, tvDosage, tvFrequency, tvDuration;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvPatientName   = itemView.findViewById(R.id.tvPatientName);
            tvDoctorName    = itemView.findViewById(R.id.tvDoctorName);
            tvMedicationName = itemView.findViewById(R.id.tvMedicationName);
            tvDosage        = itemView.findViewById(R.id.tvDosage);
            tvFrequency     = itemView.findViewById(R.id.tvFrequency);
            tvDuration      = itemView.findViewById(R.id.tvDuration);
            btnDelete       = itemView.findViewById(R.id.btnDelete);
        }
    }
}