package com.example.abhisheksingh.fireapp.Helpers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.abhisheksingh.fireapp.Models.UserProfile;
import com.example.abhisheksingh.fireapp.R;
import com.google.firebase.auth.FirebaseAuth;


public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public DialogSaveClickedListener dialogSaveClicked;
    public Button btnSave;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtOrg;

    public CustomDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        dialogSaveClicked = (DialogSaveClickedListener) activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btnSave = findViewById(R.id.btn_save);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtOrg = findViewById(R.id.edt_org);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                UserProfile profile = new UserProfile();
                profile.setEmail(edtEmail.getText().toString());
                profile.setPhoneNumber(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                profile.setName(edtName.getText().toString());
                profile.setOrganisation(edtOrg.getText().toString());
                dialogSaveClicked.onDialogSaveButtonClicked(profile);
                break;
            default:
                break;
        }
        dismiss();
    }

    public interface DialogSaveClickedListener
    {
       void onDialogSaveButtonClicked(UserProfile profile);
    }

}
