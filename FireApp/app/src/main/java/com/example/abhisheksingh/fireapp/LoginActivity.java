package com.example.abhisheksingh.fireapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity{
    private EditText edtPhoneNumber;
    private EditText edtOtp;
    private Button btnHandleOtp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPhoneNumber = findViewById(R.id.edt_phone_num);
        edtOtp = findViewById(R.id.edt_otp);
        btnHandleOtp = findViewById(R.id.btn_login)
        {

        }
       final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

        };
        btnHandleOtp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtOtp.getVisibility() == View.VISIBLE)
                {

                }
                else
                {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(edtPhoneNumber.getText().toString(), 120, TimeUnit.SECONDS, LoginActivity.this
                            , mCallbacks);
                }
            }

        });


    }
}

