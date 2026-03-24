package com.example.a4200finalproj.UI.Activities.Payment;

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

public class PaymentFormActivity extends AppCompatActivity {

    private EditText etInvoiceId, etPatientId, etAmount, etPaymentDate, etTransactionId, etNotes;
    private Spinner spinnerMethod, spinnerStatus;
    private Button btnSave;
    private DatabaseHelper db;
    private int paymentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        etInvoiceId     = findViewById(R.id.etInvoiceId);
        etPatientId     = findViewById(R.id.etPatientId);
        etAmount        = findViewById(R.id.etAmount);
        etPaymentDate   = findViewById(R.id.etPaymentDate);
        etTransactionId = findViewById(R.id.etTransactionId);
        etNotes         = findViewById(R.id.etNotes);
        spinnerMethod   = findViewById(R.id.spinnerMethod);
        spinnerStatus   = findViewById(R.id.spinnerStatus);
        btnSave         = findViewById(R.id.btnSave);

        String[] methods = {"Cash", "Credit Card", "Debit Card", "Insurance", "Bank Transfer"};
        spinnerMethod.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, methods));

        String[] statuses = {"Completed", "Pending", "Failed", "Refunded"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses));

        paymentId = getIntent().getIntExtra("payment_id", -1);
        boolean isEdit = paymentId != -1;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEdit ? "Edit Payment" : "New Payment");
        }

        setupDatePicker();
        if (isEdit) loadPaymentData();

        btnSave.setOnClickListener(v -> savePayment());
    }

    private void setupDatePicker() {
        etPaymentDate.setFocusable(false);
        etPaymentDate.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                etPaymentDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day));
            }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadPaymentData() {
        Cursor cursor = db.getPaymentById(paymentId);
        if (cursor != null && cursor.moveToFirst()) {
            etInvoiceId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_INVOICE_ID))));
            etPatientId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PATIENT_ID))));
            etAmount.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_AMOUNT))));
            etPaymentDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PAYMENT_DATE)));
            etTransactionId.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_TRANSACTION_ID)));
            etNotes.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_NOTES)));

            String method = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PAYMENT_METHOD));
            String[] methods = {"Cash", "Credit Card", "Debit Card", "Insurance", "Bank Transfer"};
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].equals(method)) { spinnerMethod.setSelection(i); break; }
            }

            String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_STATUS));
            String[] statuses = {"Completed", "Pending", "Failed", "Refunded"};
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equals(status)) { spinnerStatus.setSelection(i); break; }
            }
            cursor.close();
        }
    }

    private void savePayment() {
        String invoiceIdStr = etInvoiceId.getText().toString().trim();
        String patientIdStr = etPatientId.getText().toString().trim();
        String amountStr    = etAmount.getText().toString().trim();
        String paymentDate  = etPaymentDate.getText().toString().trim();
        String transactionId = etTransactionId.getText().toString().trim();
        String notes        = etNotes.getText().toString().trim();
        String method       = spinnerMethod.getSelectedItem().toString();
        String status       = spinnerStatus.getSelectedItem().toString();

        if (TextUtils.isEmpty(invoiceIdStr)) { etInvoiceId.setError("Required"); return; }
        if (TextUtils.isEmpty(patientIdStr)) { etPatientId.setError("Required"); return; }
        if (TextUtils.isEmpty(amountStr)) { etAmount.setError("Required"); return; }
        if (TextUtils.isEmpty(paymentDate)) { etPaymentDate.setError("Required"); return; }

        int invoiceId, patientId;
        try { invoiceId = Integer.parseInt(invoiceIdStr); }
        catch (NumberFormatException e) { etInvoiceId.setError("Enter a valid Invoice ID"); return; }

        try { patientId = Integer.parseInt(patientIdStr); }
        catch (NumberFormatException e) { etPatientId.setError("Enter a valid Patient ID"); return; }

        double amount;
        try { amount = Double.parseDouble(amountStr); }
        catch (NumberFormatException e) { etAmount.setError("Enter a valid amount"); return; }

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TablePayment.COLUMN_INVOICE_ID, invoiceId);
        cv.put(DatabaseHelper.TablePayment.COLUMN_PATIENT_ID, patientId);
        cv.put(DatabaseHelper.TablePayment.COLUMN_AMOUNT, amount);
        cv.put(DatabaseHelper.TablePayment.COLUMN_PAYMENT_DATE, paymentDate);
        cv.put(DatabaseHelper.TablePayment.COLUMN_PAYMENT_METHOD, method);
        cv.put(DatabaseHelper.TablePayment.COLUMN_TRANSACTION_ID, transactionId);
        cv.put(DatabaseHelper.TablePayment.COLUMN_STATUS, status);
        cv.put(DatabaseHelper.TablePayment.COLUMN_NOTES, notes);

        if (paymentId == -1) {
            long result = db.insertPayment(cv);
            if (result > 0) { Toast.makeText(this, "Payment recorded", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to record payment", Toast.LENGTH_SHORT).show();
        } else {
            int result = db.updatePayment(paymentId, cv);
            if (result > 0) { Toast.makeText(this, "Payment updated", Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this, "Failed to update payment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}