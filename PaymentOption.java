package com.example.AccountingAppAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.AccountingAppAdmin.DataModel.PaidAmountModel;

import java.net.Inet4Address;

public class PaymentOption extends AppCompatActivity {
    CardView c1,c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        c1=findViewById(R.id.c1);
        c2=findViewById(R.id.c2);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Paymentmethod.class);
                startActivity(intent)
                ;
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),SalerPayment.class);
                startActivity(intent);
            }
        });

    }
}
