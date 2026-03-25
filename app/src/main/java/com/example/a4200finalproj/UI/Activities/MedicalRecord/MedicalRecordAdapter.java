package com.example.a4200finalproj.UI.Activities.MedicalRecord;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.MedicalRecord;
import com.example.a4200finalproj.R;

import java.util.List;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(MedicalRecord record); }
    public interface OnDeleteClickListener { void onDelete(MedicalRecord record); }

    private final List<MedicalRecord> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public MedicalRecordAdapter(List<MedicalRecord> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medical_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalRecord r = list.get(position);
        holder.tvPatientName.setText(r.getPatientName() != null ? r.getPatientName() : "Patient ID: " + r.getPatientId());
        holder.tvDoctorName.setText("Dr. " + (r.getDoctorName() != null ? r.getDoctorName() : "ID: " + r.getDoctorId()));
        holder.tvVisitDate.setText(r.getVisitDate() != null ? r.getVisitDate() : "No date");
        holder.tvDiagnosis.setText(r.getDiagnosis() != null ? r.getDiagnosis() : "No diagnosis");
        holder.tvTreatment.setText(r.getTreatment() != null ? r.getTreatment() : "No treatment");
        holder.itemView.setOnClickListener(v -> clickListener.onClick(r));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(r));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvDoctorName, tvVisitDate, tvDiagnosis, tvTreatment;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDoctorName  = itemView.findViewById(R.id.tvDoctorName);
            tvVisitDate   = itemView.findViewById(R.id.tvVisitDate);
            tvDiagnosis   = itemView.findViewById(R.id.tvDiagnosis);
            tvTreatment   = itemView.findViewById(R.id.tvTreatment);
            btnDelete     = itemView.findViewById(R.id.btnDelete);
        }
    }
}