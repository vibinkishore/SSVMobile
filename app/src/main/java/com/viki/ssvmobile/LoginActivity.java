package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout emailTIP,passwordTIP;
    private Button signIn;
    String uname,pass;
    private TextView forgotPassword;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailTIP = findViewById(R.id.staffemail);
        passwordTIP = findViewById(R.id.staffpass);
        signIn = findViewById(R.id.signinButton);
        forgotPassword = findViewById(R.id.getHelpButton);

        progress = new ProgressDialog(LoginActivity.this);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = emailTIP.getEditText().getText().toString();
                pass = passwordTIP.getEditText().getText().toString();
                if (!validateEmail(uname)){
                    emailTIP.setError("Enter Valid Email");
                }
                if (pass.length()<6) {
                    passwordTIP.setError("Password must be atleast 6 characters");
                } else {
                    progress.setMessage("Please Wait");
                    progress.setTitle("Signing In");
                    progress.setCancelable(false);
                    progress.show();

                    signInUser(uname,pass);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            updateUI(currentUser);
        } else {
            Toast.makeText(this, "Login to Continue", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            if (uname.equals("ssv.admin@gmail.com")) {
                Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,AdminDashboardActivity.class));
            } else {
                Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }

        } else {
            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void signInUser(String uname,String pass) {
        final ProgressDialog load = new ProgressDialog(LoginActivity.this);
        load.setTitle("Signing In..");
        load.setMessage("Please Wait");
        load.show();

        mAuth.signInWithEmailAndPassword(uname, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                            Toast.makeText(LoginActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            progress.dismiss();
                            load.dismiss();
                            updateUI(user);

                        } else {
                            load.dismiss();
                            progress.dismiss();
                            updateUI(null);

                        }
                    }
                });
    }

    private boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}