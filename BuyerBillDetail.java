package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.R;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuyerBillDetail extends AppCompatActivity {

    Intent intent;
    String counts;
    TextView timeanddate,name,id,adress,contact,no,product,size,quantity,price,Totlebalance;
    DatabaseReference databaseReference;
    InventryModel inventryModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_bill_detail);
        intent = getIntent();
        counts=intent.getStringExtra("counts");
        timeanddate=findViewById(R.id.timeanddate);
        name=findViewById(R.id.name);
        id=findViewById(R.id.id);
        adress=findViewById(R.id.adress);
        contact=findViewById(R.id.contact);
        no=findViewById(R.id.no);
        size=findViewById(R.id.size);
        product=findViewById(R.id.product);
        quantity=findViewById(R.id.quantity);
        price=findViewById(R.id.price);
        Totlebalance=findViewById(R.id.Totlebalance);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").child(counts).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

                    inventryModel =dataSnapshot.getValue(InventryModel.class);
                    if(inventryModel!=null){

                        timeanddate.setText(inventryModel.getTimeandDate());

                        id.setText(inventryModel.getCount());
                        no.setText(inventryModel.getCount());
                        product.setText(inventryModel.getName());
                        quantity.setText(inventryModel.getStock());
                        size.setText(inventryModel.getUnit());
                        price.setText(inventryModel.getBuyerPrice()+".Rs");
//                       int stock = Integer.parseInt( inventryModel.getStock());
//                       int Price= Integer.parseInt(inventryModel.getBuyerPrice());
//                       int total= stock*Price;
//                       Totlebalance.setText(total);



                    }else{

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
