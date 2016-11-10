package com.berezin.maxim.autouploadfiles;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private final int readFilesPermissions = 1;
    private File[] selectedFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        selectedFiles = new File[]{};

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
                System.out.println("Hostname = "+hostname);
                System.out.println("Port = "+port);
                System.out.println("Username = "+username);
                System.out.println("Password = "+password);
                System.out.println("Save settings? = "+saveSettings);
                //TODO: Create a way to save persistent data if user chooses to save settings.

                //new FtpUploaderTask().execute(hostname, port, username, password);

            }
        });

        Button selectFilesButton = (Button) findViewById(R.id.buttonSelectFiles);
        selectFilesButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(16)
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    selectFile();
                } else {
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, readFilesPermissions);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case readFilesPermissions: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectFile();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void selectFile() {
        //TODO: Create a way to select multiple files from Downloads folder, possibly with a manual file explorer.
        File downloadLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //The following is temporarily used to explore accessible Android filesystem.
        System.out.println("downloadLocation = "+downloadLocation.getAbsolutePath());
        System.out.println("Downloads contains = "+Arrays.toString(downloadLocation.listFiles()));
        File file = new File(Environment.DIRECTORY_DOWNLOADS);
        System.out.println("root? = "+Arrays.toString(file.listFiles()));
        System.out.println(file.getAbsolutePath()+" exists? = "+file.exists());
        selectedFiles = new File[]{};
    }
}
