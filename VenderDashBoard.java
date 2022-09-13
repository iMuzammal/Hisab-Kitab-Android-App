package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AccountingAppAdmin.DataModel.BuyerBalance;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.PaidAmountModel;
import com.example.AccountingAppAdmin.DataModel.VenderBalance;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VenderDashBoard extends AppCompatActivity {
    Intent intent;
    String venderName,name;
    TextView title,totalamount,Paidamount,remainimgamount;
    RelativeLayout next;
    DatabaseReference databaseReference;
    PaidAmountModel paidAmountModel;
    int paidint,totalint,ramainingint;
    CardView c1,c2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_dash_board);
        intent = getIntent();
        venderName = intent.getStringExtra("venderName");
        title = findViewById(R.id.title);
        totalamount = findViewById(R.id.totalamount);
        Paidamount = findViewById(R.id.Paidamount);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        remainimgamount = findViewById(R.id.remainimgamount);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Create-Vender").child(venderName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                  CreateVenderModel  createVenderModel = dataSnapshot.getValue(CreateVenderModel.class);
                  if(createVenderModel!=null){
                      name=createVenderModel.getShopeName();
                      title.setText(name + " Account");

                      databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("BuyerBalance").child("Vender-Acount").child(venderName).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              if (dataSnapshot.getValue() != null) {
                                  VenderBalance venderBalance = dataSnapshot.getValue(VenderBalance.class);
                                  if(venderBalance!=null){

                                      totalint=venderBalance.getBalance();
                                      String money=Integer.toString(venderBalance.getBalance());
                                      totalamount.setText(money+".Rs");
                                      ramainingint=totalint-paidint;
                                      remainimgamount.setText(ramainingint+".Rs");

                                  }else{

                                  }



                              } else {
                              }

                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });

                  }else{

                  }



                } else {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });



        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("BuyerBalance").child("PaidAmount").child(venderName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    paidAmountModel= dataSnapshot.getValue(PaidAmountModel.class);
                    if(paidAmountModel!=null){
                        paidint=paidAmountModel.getPaidAomount();

                        Paidamount.setText(""+paidAmountModel.getPaidAomount()+".Rs");

                    }else{

                    }


                }else{


                    paidAmountModel = new PaidAmountModel(SharedPreferencesManager.getSomeStringValue(getApplication()),0);
                    databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("PaidAmount").child(venderName).setValue(paidAmountModel);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplication(), BuyerAccount.class);
                intent.putExtra("venderName", venderName);
                intent.putExtra("name", name);
                intent.putExtra("method", "Cash By Hand");
                startActivity(intent);

            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplication(), BuyerBankAccount.class);
                intent.putExtra("venderName", venderName);
                intent.putExtra("name", name);
                intent.putExtra("method", "Bank Payment");
                startActivity(intent);

            }
        });
    }
}
