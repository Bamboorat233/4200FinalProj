package com.example.a4200finalproj.UI.Activities.Payment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.Models.Payment;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PaymentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private List<Payment> paymentList = new ArrayList<>();
    private List<Payment> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Payments");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch     = findViewById(R.id.etSearch);
        tvEmpty      = findViewById(R.id.tvEmpty);
        fabAdd       = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentAdapter(filteredList, this::onPaymentClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, PaymentFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPayments();
    }

    private void loadPayments() {
        paymentList.clear();
        Cursor cursor = db.getAllPayments();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Payment p = new Payment();
                p.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_ID)));
                p.setInvoiceId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_INVOICE_ID)));
                p.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PATIENT_ID)));
                p.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_AMOUNT)));
                p.setPaymentDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PAYMENT_DATE)));
                p.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PAYMENT_METHOD)));
                p.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_STATUS)));

                Cursor patientCursor = db.getPatientById(p.getPatientId());
                if (patientCursor != null && patientCursor.moveToFirst()) {
                    p.setPatientName(patientCursor.getString(patientCursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
                    patientCursor.close();
                }

                Cursor invoiceCursor = db.getInvoiceById(p.getInvoiceId());
                if (invoiceCursor != null && invoiceCursor.moveToFirst()) {
                    p.setInvoiceNumber(invoiceCursor.getString(invoiceCursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_INVOICE_NUMBER)));
                    invoiceCursor.close();
                }

                paymentList.add(p);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (Payment p : paymentList) {
            if (query.isEmpty()
                    || (p.getPatientName() != null && p.getPatientName().toLowerCase().contains(query))
                    || (p.getInvoiceNumber() != null && p.getInvoiceNumber().toLowerCase().contains(query))
                    || (p.getPaymentMethod() != null && p.getPaymentMethod().toLowerCase().contains(query))) {
                filteredList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onPaymentClick(Payment payment) {
        Intent intent = new Intent(this, PaymentFormActivity.class);
        intent.putExtra("payment_id", payment.getId());
        startActivity(intent);
    }

    private void onDeleteClick(Payment payment) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Payment")
                .setMessage("Delete this payment of $" + String.format("%.2f", payment.getAmount()) + "?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deletePayment(payment.getId());
                    Toast.makeText(this, "Payment deleted", Toast.LENGTH_SHORT).show();
                    loadPayments();
                })
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}