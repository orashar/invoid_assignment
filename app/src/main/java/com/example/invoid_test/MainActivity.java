package com.example.invoid_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invoid_lib_test.TestInv;
import com.example.invoid_lib_test.UploadActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int UPLOAD_CODE = 11;
    private boolean status = false;
    TextView statusText, desc2;
    Button uploadBtn;
    LinearLayout imgdone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.text);
        desc2 = findViewById(R.id.desc2);
        uploadBtn = findViewById(R.id.btn_upload);
        imgdone = findViewById(R.id.img_done);

        updateUI();
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, com.example.invoid_lib_test.UploadActivity.class), UPLOAD_CODE);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPLOAD_CODE){
            Bundle extras = data.getExtras();
            if(extras != null){
                status = extras.getBoolean("success_code");
                updateUI();
            }
        }
    }

    private void updateUI() {
        imgdone.setVisibility(status ? View.VISIBLE : View.GONE);
        desc2.setVisibility(status ? View.VISIBLE : View.GONE);
        statusText.setText(status ? "Update Document" : "Upload Document");
        uploadBtn.setText(status ? "Update" : "Upload");
    }

}