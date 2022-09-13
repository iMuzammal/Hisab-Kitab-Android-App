package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.AccountingAppAdmin.DataModel.OwnerProfileModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class OwnerProfile extends AppCompatActivity {

    EditText companyName,slogon,contact,adress;
    String logo;
    Button Create;
    ImageView uploadlogo,showimg;
    private static final int PICK_IMAGE = 100;
    Uri imguri;
    String url;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile2);
        companyName =findViewById(R.id.fname);
        slogon=findViewById(R.id.lname);
        contact=findViewById(R.id.numberShow);
        adress=findViewById(R.id.adress);
        uploadlogo=findViewById(R.id.uploadinglogo);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        showimg=findViewById(R.id.viewimg);
        Create =findViewById(R.id.createprofile);
        uploadlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengellery();
            }
        });
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(companyName.getText().length()==0){
                    companyName.setError("Enter Company Name");
                }else if(slogon.getText().length()==0){
                    slogon.setError("Enter Slogon");
                }else if(contact.getText().length()==0){
                    contact.setError("Enter Contact no");
                }else if(adress.getText().length()==0){
                    adress.setError("Enter Address");
                }
                else if (showimg.getDrawable() == null) {

                    Toast.makeText(OwnerProfile.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
                }else{

                    sendDataToDB();
                }
            }
        });


    }
    private void opengellery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int
            reusltCode, Intent data) {
        if (reusltCode == RESULT_OK && requestCode == PICK_IMAGE) {

            imguri = data.getData();
            showimg.setImageURI(imguri);
        }
    }

    private void sendDataToDB() {


       databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Owner-Profile").setValue(new OwnerProfileModel(SharedPreferencesManager.getSomeStringValue(getApplicationContext()), companyName.getText().toString(),slogon.getText().toString(), contact.getText().toString(),adress.getText().toString(),"Image"


        )).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                uploadimgstorage();
                startActivity(new Intent(OwnerProfile.this, Dashboard.class));

            }
        });
    }

    public void uploadimgstorage() {





        StorageReference riversRef= FirebaseStorage.getInstance().getReference().child(SharedPreferencesManager.getSomeStringValue(getApplication())).child( "Owner-Profile" ).child( "Logo"+".jpg" );


        riversRef.putFile( imguri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Get a URL to the uploaded content


                Task<Uri> abc = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                abc.addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String url=uri.toString();


                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Owner-Profile").setValue(new OwnerProfileModel(SharedPreferencesManager.getSomeStringValue(getApplicationContext()), companyName.getText().toString(),slogon.getText().toString(), contact.getText().toString(),adress.getText().toString(),url

                        )).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    }
                } );

            }
        } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                } );
    }

}
