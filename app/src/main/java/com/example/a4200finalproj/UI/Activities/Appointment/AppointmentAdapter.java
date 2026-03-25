package com.example.a4200finalproj.UI.Activities.Appointment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.Appointment;
import com.example.a4200finalproj.R;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(Appointment appointment); }
    public interface OnDeleteClickListener { void onDelete(Appointment appointment); }

    private final List<Appointment> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public AppointmentAdapter(List<Appointment> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment a = list.get(position);

        holder.tvPatientName.setText(a.getPatientName() != null ? a.getPatientName() : "Patient ID: " + a.getPatientId());
        holder.tvDoctorName.setText("Dr. " + (a.getDoctorName() != null ? a.getDoctorName() : "ID: " + a.getDoctorId()));
        holder.tvDateTime.setText(a.getAppointmentDate() + " at " + a.getAppointmentTime());
        holder.tvReason.setText(a.getReason() != null ? a.getReason() : "No reason specified");
        holder.tvStatus.setText(a.getStatus());

        switch (a.getStatus() != null ? a.getStatus() : "") {
            case "Scheduled":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case "Completed":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
                break;
            case "Cancelled":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#F44336"));
                break;
            default:
                holder.tvStatus.setBackgroundColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> clickListener.onClick(a));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(a));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvDoctorName, tvDateTime, tvReason, tvStatus;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDoctorName  = itemView.findViewById(R.id.tvDoctorName);
            tvDateTime    = itemView.findViewById(R.id.tvDateTime);
            tvReason      = itemView.findViewById(R.id.tvReason);
            tvStatus      = itemView.findViewById(R.id.tvStatus);
            btnDelete     = itemView.findViewById(R.id.btnDelete);
        }
    }
}