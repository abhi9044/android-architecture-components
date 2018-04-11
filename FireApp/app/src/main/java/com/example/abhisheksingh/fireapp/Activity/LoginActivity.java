package com.example.abhisheksingh.fireapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhisheksingh.fireapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity{
    private EditText edtPhoneNumber;
    private EditText edtOtp;
    private Button btnHandleOtp;
    private String verificationId;
    private ProgressBar mProgressBar;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPhoneNumber = findViewById(R.id.edt_phone_num);
        edtOtp = findViewById(R.id.edt_otp);
        btnHandleOtp = findViewById(R.id.btn_login);
        mProgressBar = findViewById(R.id.progress_bar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            finishLogin();
        }
        else
        {
            final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText( LoginActivity.this,"Verification Failed! Please try again",Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);

                }

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(verificationId, forceResendingToken);
                    edtOtp.setVisibility(View.VISIBLE);
                    btnHandleOtp.setText("Login");
                    LoginActivity.this.verificationId = verificationId;
                    mProgressBar.setVisibility(View.GONE);
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
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+edtPhoneNumber.getText().toString(), 120, TimeUnit.SECONDS, LoginActivity.this
                                , mCallbacks);
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }

            });


        }

    }

    private void finishLogin() {
        Intent loginSuccessIntent = new Intent(this,MasterDetailActivity.class);
        startActivity(loginSuccessIntent);
        finish();
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
                            if (user != null)
                            {
                                finishLogin();
                            }
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginActivity.this,"Invalid verification code",Toast.LENGTH_LONG);
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Login failed,please try again",Toast.LENGTH_LONG);
                            }
                        }
                    }
                });
    }
}

