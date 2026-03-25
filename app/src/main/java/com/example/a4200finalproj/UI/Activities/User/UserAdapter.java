package com.example.a4200finalproj.UI.Activities.User;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.Models.User;
import com.example.a4200finalproj.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public interface OnItemClickListener { void onClick(User user); }
    public interface OnDeleteClickListener { void onDelete(User user); }

    private final List<User> list;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public UserAdapter(List<User> list, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User u = list.get(position);
        holder.tvFullName.setText(u.getFullName());
        holder.tvUsername.setText("@" + u.getUsername());
        holder.tvEmail.setText(u.getEmail() != null ? u.getEmail() : "No email");
        holder.tvRole.setText(u.getRole());
        holder.tvStatus.setText(u.getIsActive() == 1 ? "Active" : "Inactive");
        holder.tvStatus.setTextColor(u.getIsActive() == 1 ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336"));

        switch (u.getRole() != null ? u.getRole() : "") {
            case "Admin":        holder.tvRole.setBackgroundColor(Color.parseColor("#9C27B0")); break;
            case "Doctor":       holder.tvRole.setBackgroundColor(Color.parseColor("#2196F3")); break;
            case "Nurse":        holder.tvRole.setBackgroundColor(Color.parseColor("#009688")); break;
            default:             holder.tvRole.setBackgroundColor(Color.parseColor("#FF9800")); break;
        }

        holder.itemView.setOnClickListener(v -> clickListener.onClick(u));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(u));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvUsername, tvEmail, tvRole, tvStatus;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvEmail    = itemView.findViewById(R.id.tvEmail);
            tvRole     = itemView.findViewById(R.id.tvRole);
            tvStatus   = itemView.findViewById(R.id.tvStatus);
            btnDelete  = itemView.findViewById(R.id.btnDelete);
        }
    }
}