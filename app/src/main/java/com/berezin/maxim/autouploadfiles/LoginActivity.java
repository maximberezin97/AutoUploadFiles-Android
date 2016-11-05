package com.berezin.maxim.autouploadfiles;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.File;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //EditText editTextFileList = (EditText) findViewById(R.id.editTextFileList);

        Button connectButton = (Button) findViewById(R.id.buttonConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hostname = ((EditText) findViewById(R.id.editTextHostname)).getText().toString();
                String port = ((EditText) findViewById(R.id.editTextPort)).getText().toString();
                String username = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
                boolean saveSettings = ((CheckBox) findViewById(R.id.checkBoxSaveSettings)).isChecked();
                File downloadLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                System.out.println("Hostname = "+hostname);
                System.out.println("Port = "+port);
                System.out.println("Username = "+username);
                System.out.println("Password = "+password);
                System.out.println("Save settings? = "+saveSettings);
                //TODO: Create a way to save persistent data if user chooses to save settings.
                //TODO: Create a way to select multiple files from Downloads folder, possibly with a manual file explorer.
                //The following is temporarily used to explore accessible Android filesystem.
                //System.out.println("downloadLocation = "+downloadLocation.getAbsolutePath());
                //System.out.println("Downloads contains = "+Arrays.toString(downloadLocation.listFiles()));
                //File file = new File(Environment.DIRECTORY_DOWNLOADS);
                //System.out.println("root? = "+Arrays.toString(file.listFiles()));
                //System.out.println(file.getAbsolutePath()+" exists? = "+file.exists());

                //new FtpUploaderTask().execute(hostname, port, username, password);

            }
        });
    }
}
