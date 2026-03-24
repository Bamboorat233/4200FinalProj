package com.example.a4200finalproj.UI.Activities.Invoice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.Models.Invoice;
import com.example.a4200finalproj.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InvoiceListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InvoiceAdapter adapter;
    private List<Invoice> invoiceList = new ArrayList<>();
    private List<Invoice> filteredList = new ArrayList<>();
    private DatabaseHelper db;
    private EditText etSearch;
    private Spinner spinnerStatus;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;
    private String selectedStatus = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Invoices");
        }

        db = DatabaseHelper.getInstance(this);
        recyclerView   = findViewById(R.id.recyclerView);
        etSearch       = findViewById(R.id.etSearch);
        spinnerStatus  = findViewById(R.id.spinnerStatus);
        tvEmpty        = findViewById(R.id.tvEmpty);
        fabAdd         = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InvoiceAdapter(filteredList, this::onInvoiceClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        String[] statuses = {"All", "Pending", "Paid", "Overdue", "Cancelled"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = statuses[position];
                applyFilter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, InvoiceFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInvoices();
    }

    private void loadInvoices() {
        invoiceList.clear();
        Cursor cursor = db.getAllInvoices();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Invoice i = new Invoice();
                i.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_ID)));
                i.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_PATIENT_ID)));
                i.setInvoiceNumber(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_INVOICE_NUMBER)));
                i.setInvoiceDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_INVOICE_DATE)));
                i.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_DUE_DATE)));
                i.setSubtotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_SUBTOTAL)));
                i.setTax(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_TAX)));
                i.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_TOTAL)));
                i.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_STATUS)));
                i.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_NOTES)));

                Cursor patientCursor = db.getPatientById(i.getPatientId());
                if (patientCursor != null && patientCursor.moveToFirst()) {
                    i.setPatientName(patientCursor.getString(patientCursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
                    patientCursor.close();
                }
                invoiceList.add(i);
            }
            cursor.close();
        }
        applyFilter();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();
        for (Invoice i : invoiceList) {
            boolean matchesStatus = selectedStatus.equals("All") || selectedStatus.equals(i.getStatus());
            boolean matchesSearch = query.isEmpty()
                    || (i.getPatientName() != null && i.getPatientName().toLowerCase().contains(query))
                    || (i.getInvoiceNumber() != null && i.getInvoiceNumber().toLowerCase().contains(query));
            if (matchesStatus && matchesSearch) filteredList.add(i);
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onInvoiceClick(Invoice invoice) {
        Intent intent = new Intent(this, InvoiceFormActivity.class);
        intent.putExtra("invoice_id", invoice.getId());
        startActivity(intent);
    }

    private void onDeleteClick(Invoice invoice) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Invoice")
                .setMessage("Delete invoice " + invoice.getInvoiceNumber() + "?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteInvoice(invoice.getId());
                    Toast.makeText(this, "Invoice deleted", Toast.LENGTH_SHORT).show();
                    loadInvoices();
                })
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}