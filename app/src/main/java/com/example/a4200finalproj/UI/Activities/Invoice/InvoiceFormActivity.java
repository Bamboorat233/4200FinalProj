package com.example.a4200finalproj.UI.Activities.Invoice;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a4200finalproj.DAL.DatabaseHelper;
import com.example.a4200finalproj.R;

import java.util.Locale;

public class InvoiceFormActivity extends AppCompatActivity {

    private EditText etPatientId, etInvoiceDate, etDueDate, etSubtotal, etTax, etNotes;
    private Spinner spinnerStatus;
    private Button btnSave;
    private DatabaseHelper db;
    private int invoiceId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etPatientId   = findViewById(R.id.etPatientId);
        etInvoiceDate = findViewById(R.id.etInvoiceDate);
        etDueDate     = findViewById(R.id.etDueDate);
        etSubtotal    = findViewById(R.id.etSubtotal);
        etTax         = findViewById(R.id.etTax);
        etNotes       = findViewById(R.id.etNotes);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSave       = findViewById(R.id.btnSave);

        String[] statuses = {"Pending", "Paid", "Overdue", "Cancelled"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses));

        invoiceId = getIntent().getIntExtra("invoice_id", -1);
        boolean isEdit = invoiceId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit Invoice" : "New Invoice");
        }

        setupDatePicker(etInvoiceDate);
        setupDatePicker(etDueDate);

        if (isEdit) loadInvoiceData();

        btnSave.setOnClickListener(v -> saveInvoice());
    }

    private void setupDatePicker(EditText field) {
        field.setFocusable(false);
        field.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                field.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day));
            }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadInvoiceData() {
        Cursor cursor = db.getInvoiceById(invoiceId);
        if (cursor != null && cursor.moveToFirst()) {
            etPatientId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_PATIENT_ID))));
            etInvoiceDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_INVOICE_DATE)));
            etDueDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_DUE_DATE)));
            etSubtotal.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_SUBTOTAL))));
            etTax.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_TAX))));
            etNotes.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_NOTES)));

            String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_STATUS));
            String[] statuses = {"Pending", "Paid", "Overdue", "Cancelled"};
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equals(status)) { spinnerStatus.setSelection(i); break; }
            }
            cursor.close();
        }
    }

    private void saveInvoice() {
        String patientIdStr = etPatientId.getText().toString().trim();
        String invoiceDate  = etInvoiceDate.getText().toString().trim();
        String dueDate      = etDueDate.getText().toString().trim();
        String subtotalStr  = etSubtotal.getText().toString().trim();
        String taxStr       = etTax.getText().toString().trim();
        String notes        = etNotes.getText().toString().trim();
        String status       = spinnerStatus.getSelectedItem().toString();

        if (TextUtils.isEmpty(patientIdStr)) { etPatientId.setError("Required"); return; }
        if (TextUtils.isEmpty(invoiceDate)) { etInvoiceDate.setError("Required"); return; }
        if (TextUtils.isEmpty(subtotalStr)) { etSubtotal.setError("Required"); return; }

        int patientId;
        try { patientId = Integer.parseInt(patientIdStr); }
        catch (NumberFormatException e) { etPatientId.setError("Enter a valid Patient ID"); return; }

        double subtotal;
        try { subtotal = Double.parseDouble(subtotalStr); }
        catch (NumberFormatException e) { etSubtotal.setError("Enter a valid amount"); return; }

        double tax = 0;
        if (!TextUtils.isEmpty(taxStr)) {
            try { tax = Double.parseDouble(taxStr); }
            catch (NumberFormatException e) { etTax.setError("Enter a valid tax amount"); return; }
        }

        double total = subtotal + tax;

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableInvoice.COLUMN_PATIENT_ID, patientId);
        cv.put(DatabaseHelper.TableInvoice.COLUMN_INVOICE_DATE, invoiceDate);
        cv.put(DatabaseHelper.TableInvoice.COLUMN_DUE_DATE, dueDate);
        cv.put(DatabaseHelper.TableInvoice.COLUMN_SUBTOTAL, subtotal);
        cv.put(DatabaseHelper.TableInvoice.COLUMN_TAX, tax);
        cv.put(DatabaseHelper.TableInvoice.COLUMN_TOTAL, total);
        cv.put(DatabaseHelper.TableInvoice.COLUMN_STATUS, status);
        cv.put(DatabaseHelper.TableInvoice.COLUMN_NOTES, notes);

        if (invoiceId == -1) {
            cv.put(DatabaseHelper.TableInvoice.COLUMN_INVOICE_NUMBER, db.generateInvoiceNumber());
            long result = db.insertInvoice(cv);
            if (result > 0) { Toast.makeText(this, "Invoice created", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to create invoice", Toast.LENGTH_SHORT).show();
        } else {
            int result = db.updateInvoice(invoiceId, cv);
            if (result > 0) { Toast.makeText(this, "Invoice updated", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to update invoice", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}