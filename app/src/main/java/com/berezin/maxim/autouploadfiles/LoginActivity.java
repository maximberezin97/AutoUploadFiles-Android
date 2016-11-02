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
                String hostname = ((EditText) findViewById(R.id.editTextHostname)).getText().toString();
                String port = ((EditText) findViewById(R.id.editTextPort)).getText().toString();
                String username = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
                int portNum = Integer.valueOf(port);

                FtpUploader ftpUploader = new FtpUploader(hostname, portNum, username, password);

            }
        });
    }
}
