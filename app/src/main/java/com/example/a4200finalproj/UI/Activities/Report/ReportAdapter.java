package com.example.a4200finalproj.UI.Activities.Report;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.Report;
import com.example.a4200finalproj.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(Report report); }
    public interface OnDeleteClickListener { void onDelete(Report report); }

    private final List<Report> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public ReportAdapter(List<Report> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report r = list.get(position);
        holder.tvTitle.setText(r.getTitle() != null ? r.getTitle() : "Untitled");
        holder.tvType.setText(r.getReportType() != null ? r.getReportType() : "General");
        holder.tvGeneratedDate.setText(r.getGeneratedDate() != null ? r.getGeneratedDate() : "No date");
        holder.tvGeneratedBy.setText(r.getGeneratedBy() != null ? "By: " + r.getGeneratedBy() : "");
        holder.tvPatientName.setText(r.getPatientName() != null ? r.getPatientName() : "N/A");

        switch (r.getReportType() != null ? r.getReportType() : "") {
            case "Patient":     holder.tvType.setBackgroundColor(Color.parseColor("#2196F3")); break;
            case "Doctor":      holder.tvType.setBackgroundColor(Color.parseColor("#9C27B0")); break;
            case "Appointment": holder.tvType.setBackgroundColor(Color.parseColor("#009688")); break;
            case "Financial":   holder.tvType.setBackgroundColor(Color.parseColor("#FF9800")); break;
            default:            holder.tvType.setBackgroundColor(Color.parseColor("#607D8B")); break;
        }

        holder.itemView.setOnClickListener(v -> clickListener.onClick(r));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(r));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvType, tvGeneratedDate, tvGeneratedBy, tvPatientName;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle         = itemView.findViewById(R.id.tvTitle);
            tvType          = itemView.findViewById(R.id.tvType);
            tvGeneratedDate = itemView.findViewById(R.id.tvGeneratedDate);
            tvGeneratedBy   = itemView.findViewById(R.id.tvGeneratedBy);
            tvPatientName   = itemView.findViewById(R.id.tvPatientName);
            btnDelete       = itemView.findViewById(R.id.btnDelete);
        }
    }
}