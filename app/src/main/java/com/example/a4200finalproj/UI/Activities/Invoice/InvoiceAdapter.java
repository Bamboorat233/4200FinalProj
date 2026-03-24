package com.example.a4200finalproj.UI.Activities.Invoice;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.Invoice;
import com.example.a4200finalproj.R;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(Invoice invoice); }
    public interface OnDeleteClickListener { void onDelete(Invoice invoice); }

    private final List<Invoice> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public InvoiceAdapter(List<Invoice> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice i = list.get(position);
        holder.tvInvoiceNumber.setText(i.getInvoiceNumber() != null ? i.getInvoiceNumber() : "N/A");
        holder.tvPatientName.setText(i.getPatientName() != null ? i.getPatientName() : "Patient ID: " + i.getPatientId());
        holder.tvInvoiceDate.setText(i.getInvoiceDate() != null ? i.getInvoiceDate() : "No date");
        holder.tvTotal.setText(String.format("$%.2f", i.getTotal()));
        holder.tvStatus.setText(i.getStatus());

        switch (i.getStatus() != null ? i.getStatus() : "") {
            case "Paid":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
                break;
            case "Pending":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"));
                break;
            case "Overdue":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#F44336"));
                break;
            case "Cancelled":
                holder.tvStatus.setBackgroundColor(Color.GRAY);
                break;
            default:
                holder.tvStatus.setBackgroundColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> clickListener.onClick(i));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(i));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvoiceNumber, tvPatientName, tvInvoiceDate, tvTotal, tvStatus;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvInvoiceNumber = itemView.findViewById(R.id.tvInvoiceNumber);
            tvPatientName   = itemView.findViewById(R.id.tvPatientName);
            tvInvoiceDate   = itemView.findViewById(R.id.tvInvoiceDate);
            tvTotal         = itemView.findViewById(R.id.tvTotal);
            tvStatus        = itemView.findViewById(R.id.tvStatus);
            btnDelete       = itemView.findViewById(R.id.btnDelete);
        }
    }
}