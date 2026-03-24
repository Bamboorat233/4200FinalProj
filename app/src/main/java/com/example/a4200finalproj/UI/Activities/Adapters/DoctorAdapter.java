package com.example.a4200finalproj.UI.Activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.Doctor;
import com.example.a4200finalproj.R;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    private final List<Doctor> doctorList;
    private final OnDoctorClickListener listener;

    public DoctorAdapter(List<Doctor> doctorList, OnDoctorClickListener listener) {
        this.doctorList = doctorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.tvDoctorName.setText(doctor.getFullName());
        holder.tvDoctorSpecialization.setText("Specialization: " + doctor.getSpecialization());
        holder.tvDoctorPhone.setText("Phone: " + doctor.getPhone());

        holder.itemView.setOnClickListener(v -> listener.onDoctorClick(doctor));
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvDoctorSpecialization, tvDoctorPhone;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorSpecialization = itemView.findViewById(R.id.tvDoctorSpecialization);
            tvDoctorPhone = itemView.findViewById(R.id.tvDoctorPhone);
        }
    }
}