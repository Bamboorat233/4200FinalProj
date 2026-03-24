package com.example.a4200finalproj.UI.Activities.Payment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.Payment;
import com.example.a4200finalproj.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(Payment payment); }
    public interface OnDeleteClickListener { void onDelete(Payment payment); }

    private final List<Payment> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public PaymentAdapter(List<Payment> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment p = list.get(position);
        holder.tvPatientName.setText(p.getPatientName() != null ? p.getPatientName() : "Patient ID: " + p.getPatientId());
        holder.tvInvoiceNumber.setText(p.getInvoiceNumber() != null ? "Invoice: " + p.getInvoiceNumber() : "Invoice ID: " + p.getInvoiceId());
        holder.tvAmount.setText(String.format("$%.2f", p.getAmount()));
        holder.tvPaymentDate.setText(p.getPaymentDate() != null ? p.getPaymentDate() : "No date");
        holder.tvMethod.setText(p.getPaymentMethod() != null ? p.getPaymentMethod() : "Unknown");
        holder.tvStatus.setText(p.getStatus());

        switch (p.getStatus() != null ? p.getStatus() : "") {
            case "Completed":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
                break;
            case "Pending":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"));
                break;
            case "Failed":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#F44336"));
                break;
            case "Refunded":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            default:
                holder.tvStatus.setBackgroundColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> clickListener.onClick(p));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(p));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvInvoiceNumber, tvAmount, tvPaymentDate, tvMethod, tvStatus;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvPatientName   = itemView.findViewById(R.id.tvPatientName);
            tvInvoiceNumber = itemView.findViewById(R.id.tvInvoiceNumber);
            tvAmount        = itemView.findViewById(R.id.tvAmount);
            tvPaymentDate   = itemView.findViewById(R.id.tvPaymentDate);
            tvMethod        = itemView.findViewById(R.id.tvMethod);
            tvStatus        = itemView.findViewById(R.id.tvStatus);
            btnDelete       = itemView.findViewById(R.id.btnDelete);
        }
    }
}