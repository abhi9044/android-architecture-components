package com.example.abhisheksingh.fireapp.Helpers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.abhisheksingh.fireapp.Models.ImportantIssue;
import com.example.abhisheksingh.fireapp.Models.Stock;
import com.example.abhisheksingh.fireapp.R;

import pl.aprilapps.easyphotopicker.EasyImage;


public class CustomDialogForImportantIssue extends Dialog implements
        android.view.View.OnClickListener {

    public Dialog dialog;
    public Activity activity;
    public DialogSaveClickedListener dialogSaveClicked;
    public Button btnSave;
    public Button btnCancel;
    public ImageButton imgTakePicture;
    public ImageView imgIssue;
    private EditText edtIssue;

    public CustomDialogForImportantIssue(Activity activity, DialogSaveClickedListener fragment) {
        super(activity);
        this.activity = activity;
        dialogSaveClicked = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_imp_issue);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        imgTakePicture = findViewById(R.id.img_take_picture);
        imgIssue = findViewById(R.id.img_issue);
        edtIssue = findViewById(R.id.edt_issue_description);
        imgTakePicture.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                ImportantIssue importantIssue = new ImportantIssue();
                break;

            case R.id.btn_cancel:
                dismiss();

            case R.id.img_take_picture:
                EasyImage.openChooserWithGallery(activity, "Select", 0);
            default:
                break;
        }
        dismiss();
    }

    public interface DialogSaveClickedListener {
        void onDialogSaveButtonClicked(Stock stock);
    }


}

