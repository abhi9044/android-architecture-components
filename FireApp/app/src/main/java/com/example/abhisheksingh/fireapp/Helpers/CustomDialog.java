package com.example.abhisheksingh.fireapp.Helpers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.abhisheksingh.fireapp.R;

public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public Button btnSave;

    public CustomDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                activity.finish();
                break;
            default:
                break;
        }
        dismiss();
    }
}