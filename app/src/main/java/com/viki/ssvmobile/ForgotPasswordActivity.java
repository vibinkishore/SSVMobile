package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputLayout resetEmail;
    private Button resetPassword;
    private TextView backtoLogin;
    private FirebaseAuth mAuth;
    private String emailVal;
    private String zone;
    HashMap<String,Object> changePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        resetEmail = findViewById(R.id.resetEmail);
        resetPassword = findViewById(R.id.resetPassButton);
        backtoLogin = findViewById(R.id.backToLogin);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailVal = resetEmail.getEditText().getText().toString();
                zone = emailVal.substring(4,5);
                if (!zone.equals("admin")) {
                    sendResetMail(emailVal);
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this, "Admin Password is Modified", Toast.LENGTH_SHORT).show();
                    sendResetMail(emailVal);
                }
            }
        });
        backtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
            }
        });
    }

    private void sendResetMail(final String email) {
        final ProgressDialog load = new ProgressDialog(ForgotPasswordActivity.this);
        load.setTitle("Udating..");
        load.setMessage("Please Wait");
        load.show();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Reset Password Link has been mailed to "+ email,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                            load.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(),"Something Went Wrong! Try Again",Toast.LENGTH_SHORT).show();
                            load.dismiss();
                        }
                    }
                });

    }
}
