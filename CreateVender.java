package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.AccountingAppAdmin.DataModel.Counter;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.R;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateVender extends AppCompatActivity {

    EditText shopname, vendername, contactno,adress;
    Button create;
    DatabaseReference databaseReference;
    CreateVenderModel createVenderModel;
    int count;
    String counts;
    Counter counter;
    ImageView back;
    String timeanddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vender);

        shopname=findViewById(R.id.fname);
        vendername=findViewById(R.id.lname);
        contactno=findViewById(R.id.numberShow);
        adress=findViewById(R.id.adress);
        create=findViewById(R.id.createprofile);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("'Date-   'dd.MM.yyyy'  Time-' HH:mm:ss");
        timeanddate = sdf.format(new Date());
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!= null){

                    counter = dataSnapshot.getValue(Counter.class);
                    if(counter != null){

                        count= counter.getCounts();

                    }else{

                        counter = new Counter(0);
                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").setValue(counter);


                    }

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopname.getText().length()== 0){
                    shopname.setError("Enter Shope Name");
                }
                else if(vendername.getText().length()== 0){
                    vendername.setError("Enter Vender Name");
                }
                else if(contactno.getText().length()== 0){
                    contactno.setError("Enter Cell No");
                }
                else if(adress.getText().length() == 0){
                    adress.setError("Enter Address");
                }
                else
                {
                    counts= String.valueOf(count);
                    createVenderModel = new CreateVenderModel(SharedPreferencesManager.getSomeStringValue(getApplication()),counts,shopname.getText().toString(),vendername.getText().toString(),contactno.getText().toString(),adress.getText().toString(),timeanddate);
                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Vender").child(counts+"-  "+shopname.getText().toString()).setValue(createVenderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            count = count + 1;
                            counter = new Counter(count);
                            databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Counter").setValue(counter).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    finish();
                                }
                            });


                        }
                    });
                }
            }
        });

    }
}
