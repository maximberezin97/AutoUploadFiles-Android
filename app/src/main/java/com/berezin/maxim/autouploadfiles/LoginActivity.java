package com.berezin.maxim.autouploadfiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button connectButton = (Button) findViewById(R.id.buttonConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextHostname = (EditText) findViewById(R.id.editTextHostname);
                EditText editTextPort = (EditText) findViewById(R.id.editTextPort);
                EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
                EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            }
        });
    }
}
