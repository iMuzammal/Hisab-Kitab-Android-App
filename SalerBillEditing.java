package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.AccountingAppAdmin.DataModel.ClientBillModelProduct;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.VenderBillModelProduct;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SalerBillEditing extends AppCompatActivity {

    TextView buyerprice,unit,name;
    EditText salerprice, stock;
    Intent intent;
    String counts,vender,category,categorysub,categorysubto,count;
    DatabaseReference databaseReference;
    ClientBillModelProduct clientBillModelProduct;
    InventryModel inventryModel;
    Button update,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saler_bill_editing);
        buyerprice = findViewById(R.id.buyerprice);
        salerprice = findViewById(R.id.salerprice);
        stock = findViewById(R.id.stock);
        unit = findViewById(R.id.unit);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        name = findViewById(R.id.productname);
        intent = getIntent();
        counts = intent.getStringExtra("counts");
        count = intent.getStringExtra("count");
        vender = intent.getStringExtra("vender");
        category = intent.getStringExtra("category");
        categorysub = intent.getStringExtra("categorysub");
        categorysubto = intent.getStringExtra("categorysubto");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Client-Billing").child(count).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    clientBillModelProduct = dataSnapshot.getValue(ClientBillModelProduct.class);
                    if (clientBillModelProduct != null) {

                        buyerprice.setText(clientBillModelProduct.getBuyerPrice());
                        salerprice.setText(clientBillModelProduct.getSalerPrice());
                        stock.setText(clientBillModelProduct.getStock());
                        unit.setText(clientBillModelProduct.getUnit());
                        name.setText(clientBillModelProduct.getName());

                    } else {

                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Client-Billing").child(count).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        clientBillModelProduct = dataSnapshot.getValue(ClientBillModelProduct.class);
                        dataSnapshot.getRef().child( "buyerPrice" ).setValue( buyerprice.getText().toString() );
                        dataSnapshot.getRef().child( "salerPrice" ).setValue( salerprice.getText().toString() );
                        dataSnapshot.getRef().child( "stock" ).setValue( stock.getText().toString() );
                        dataSnapshot.getRef().child( "unit" ).setValue( unit.getText().toString() );



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Client-Billing").child(count).removeValue();
            }
        });

    }
}
