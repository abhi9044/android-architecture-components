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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.READ_CONTACTS;
import static android.content.ContentValues.TAG;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity{
    private EditText edtPhoneNumber;
    private EditText edtOtp;
    private Button btnHandleOtp;
    private String verificationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPhoneNumber = findViewById(R.id.edt_phone_num);
        edtOtp = findViewById(R.id.edt_otp);
        btnHandleOtp = findViewById(R.id.btn_login);
       final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText( LoginActivity.this,"Verification Failed! Please try again",Toast.LENGTH_LONG).show();

            }

           @Override
           public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
               super.onCodeSent(verificationId, forceResendingToken);
               edtOtp.setVisibility(View.VISIBLE);
               LoginActivity.this.verificationId = verificationId;
               Toast.makeText( LoginActivity.this,"Otp has been sent to your number",Toast.LENGTH_LONG).show();
           }
       };

        btnHandleOtp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtOtp.getVisibility() == View.VISIBLE)
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, edtOtp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
                else
                {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+edtPhoneNumber.getText().toString(), 120, TimeUnit.SECONDS, LoginActivity.this
                            , mCallbacks);
                }
            }

        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredentialfA:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}

