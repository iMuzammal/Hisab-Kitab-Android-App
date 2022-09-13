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

import com.bumptech.glide.Glide;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProductActivity extends AppCompatActivity {

    EditText productname,buyerprice,salerprice,stock,unit;
    ImageView uploadimg;
    private static final int PICK_IMAGE = 100;
    Uri imguri;
    ImageView Showimg;
    Button update,delete;
    DatabaseReference databaseReference;
    String counts,category,categorysub,categorysubto;
    Intent intent;
    InventryModel inventryModel;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        intent= getIntent();
        counts= intent.getStringExtra("counts");

        category= intent.getStringExtra("category");
        categorysub= intent.getStringExtra("categorysub");
        categorysubto= intent.getStringExtra("categorysubto");
        productname=findViewById(R.id.productname);
        buyerprice=findViewById(R.id.buyerprice);
        salerprice=findViewById(R.id.salerprice);
        stock=findViewById(R.id.stock);
        unit=findViewById(R.id.unit);
        uploadimg=findViewById(R.id.uploadimg);
        Showimg=findViewById(R.id.imgshow);
        update=findViewById(R.id.update);
        delete=findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(counts).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child(category).child(categorysub).child(categorysubto).child(counts).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Intent intent = new Intent( getApplication(),AddProduct.class);
                                startActivity(intent);

                            }
                        });




                    }
                });













            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference();
        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengellery();
            }
        });

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(counts).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!= null){
                    inventryModel = dataSnapshot.getValue(InventryModel.class);
                    if(inventryModel!= null){

                        productname.setText(inventryModel.getName());
                        buyerprice.setText(inventryModel.getBuyerPrice());
                        salerprice.setText(inventryModel.getSalerPrice());
                        stock.setText(inventryModel.getStock());
                        unit.setText(inventryModel.getUnit());
                        Glide.with(getApplication()).load(inventryModel.getImage()).into(Showimg);

                    }else{

                    }

                }else{
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(counts).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        inventryModel = dataSnapshot.getValue(InventryModel.class);
                        dataSnapshot.getRef().child( "buyerPrice" ).setValue( buyerprice.getText().toString() );
                        dataSnapshot.getRef().child( "name" ).setValue( productname.getText().toString() );
                        dataSnapshot.getRef().child( "salerPrice" ).setValue( salerprice.getText().toString() );
                        dataSnapshot.getRef().child( "stock" ).setValue( stock.getText().toString() );
                        dataSnapshot.getRef().child( "unit" ).setValue( unit.getText().toString() );


                        StorageReference riversRef= FirebaseStorage.getInstance().getReference().child(SharedPreferencesManager.getSomeStringValue(getApplication())).child( "Owner-Profile" ).child( "Logo"+".jpg" );
                        riversRef.putFile( imguri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> abc = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                abc.addOnSuccessListener( new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        url  =uri.toString();
                                        Toast.makeText(getApplication(), ""+url, Toast.LENGTH_SHORT).show();
                                        dataSnapshot.getRef().child( "logo" ).setValue( url );

                                        Toast.makeText(getApplication(), "Profile updated", Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent = new Intent(getApplication(), Dashboard.class);
                                        startActivity(intent);

                                    }
                                } );
                            }
                        } ).addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        } );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    private void opengellery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int
            reusltCode, Intent data) {
        if (reusltCode == RESULT_OK && requestCode == PICK_IMAGE) {

            imguri = data.getData();
            Showimg.setImageURI(imguri);
        }
    }
}
