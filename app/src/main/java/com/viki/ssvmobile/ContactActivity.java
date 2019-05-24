package com.viki.ssvmobile;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    TextInputLayout userName,userEmail,userContact,userMessage;
    Button submitQuery;
    String uName,uEmail,uContact,uQuery,emailsubject,emailContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userContact = findViewById(R.id.userContact);
        userMessage = findViewById(R.id.messageText);
        submitQuery = findViewById(R.id.submitQuery);

        submitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uName = userName.getEditText().getText().toString();
                uEmail = userEmail.getEditText().getText().toString();
                uContact = userContact.getEditText().getText().toString();
                uQuery = userMessage.getEditText().getText().toString();

                emailsubject = "Customer Query | " + uName;
                emailContent = "User Name: " + uName + "\nUser Email: " + uEmail +
                        "\nUser Contact: " + uContact + "\n\n\nQuery:\n\t\t" + uQuery + "\n\n\n-- end of content";

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ssv.ngpit@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, emailsubject);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);
                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            "No email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
