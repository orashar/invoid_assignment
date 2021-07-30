package com.example.invoid_lib_test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_CODE = 12;
    private InputStream imageStream;
    private ImageView docImg;
    private Uri imguri;

    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_layout);

        docImg = findViewById(R.id.lib_img);
        docImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_CODE);
            }
        });



        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        LottieAnimationView lottieUpload = findViewById(R.id.lottie_upload);
        LottieAnimationView lottiedone = findViewById(R.id.lottie_done);
        lottiedone.setVisibility(View.GONE);
        lottieUpload.setVisibility(View.GONE);
        Button uploadBtn = findViewById(R.id.lib_btn);
        uploadBtn.setVisibility(View.VISIBLE);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageStream != null){
                    String f2 = imguri.getLastPathSegment().replace(':', '_');
                    StorageReference mountainsRef = storageRef.child(f2+".jpg");
                    StorageReference mountainImageRef = storageRef.child("images/" + f2+".jpg");
                    UploadTask uploadTask = mountainsRef.putStream(imageStream);
                    uploadBtn.setVisibility(View.GONE);
                    lottieUpload.setVisibility(View.VISIBLE);
                    docImg.setEnabled(false);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                              uploadBtn.setVisibility(View.VISIBLE);
                              lottiedone.setVisibility(View.GONE);
                              lottieUpload.setVisibility(View.GONE);
                              docImg.setEnabled(true);
//                            Toast.makeText(UploadActivity.this, "Image upload failed", Toast.LENGTH_LONG).show();Intent intent = new Intent();
//                            intent.putExtra("success_code", false);
//                            setResult(RESULT_CANCELED, intent);
//                            finish();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadBtn.setVisibility(View.GONE);
                            lottiedone.setVisibility(View.VISIBLE);
                            lottieUpload.setVisibility(View.GONE);
                            lottiedone.playAnimation();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(UploadActivity.this, "Uploaded success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("success_code", true);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }, 3000);
                        }
                    });
                } else{
                    Toast.makeText(UploadActivity.this, "Please Select a picture to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void uploadFile(InputStream inputStream) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(UploadActivity.this, "Error retrieving image. Try again.", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                imguri = data.getData();
                docImg.setImageURI(imguri);
                imageStream = this.getApplicationContext().getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(UploadActivity.this, "Error retrieving image. Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

}