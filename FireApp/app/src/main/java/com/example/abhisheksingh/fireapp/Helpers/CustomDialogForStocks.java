package com.example.abhisheksingh.fireapp.Helpers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.abhisheksingh.fireapp.Models.Stock;
import com.example.abhisheksingh.fireapp.Models.UserProfile;
import com.example.abhisheksingh.fireapp.R;


public class CustomDialogForStocks extends Dialog implements
        android.view.View.OnClickListener {

    public Dialog dialog;
    public Activity activity;
    public DialogSaveClickedListener dialogSaveClicked;
    public Button btnSave;
    private EditText edtName;
    private EditText edtItem;
    private EditText edtInQuantity;
    private EditText edtOutQuantity;

    public CustomDialogForStocks(Activity activity,DialogSaveClickedListener fragment) {
        super(activity);
        this.activity = activity;
        dialogSaveClicked = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_for_stocks);
        btnSave = findViewById(R.id.btn_save);
        edtName = findViewById(R.id.edt_name);
        edtItem = findViewById(R.id.edt_item);
        edtInQuantity = findViewById(R.id.edt_in_quantity);
        edtOutQuantity = findViewById(R.id.edt_out_quantity);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                Stock stock = new Stock();
                stock.setId(String.valueOf(System.currentTimeMillis()));
                stock.setVendor(edtName.getText().toString());
                stock.setInQuantity(Integer.parseInt(edtInQuantity.getText().toString()));
                stock.setOutQuantity(Integer.parseInt(edtOutQuantity.getText().toString()));
                dialogSaveClicked.onDialogSaveButtonClicked(stock);
                break;

            case R.id.btn_cancel:
                dismiss();
            default:
                break;
        }
        dismiss();
    }

    public interface DialogSaveClickedListener
    {
        void onDialogSaveButtonClicked(Stock stock);
    }

}

