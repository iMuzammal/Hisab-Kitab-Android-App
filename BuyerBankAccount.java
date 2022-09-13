package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.AccountingAppAdmin.Adapter.BuyerAdapter;
import com.example.AccountingAppAdmin.Adapter.BuyerBankAdapter;
import com.example.AccountingAppAdmin.DataModel.PaymentMethodModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyerBankAccount extends AppCompatActivity {

    ArrayList<PaymentMethodModel> arrayList = new ArrayList<>();
    BuyerBankAdapter buyerBankAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    PaymentMethodModel paymentMethodModel;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    ImageView back;
    TextView msg,title,cashsum;
    String venderName,method,name;
    Intent intent;
    int total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_bank_account);
        intent = getIntent();
        venderName=intent.getStringExtra("venderName");
        method=intent.getStringExtra("method");
        name=intent.getStringExtra("name");
        msg =findViewById(R.id.vendermsg);
        title =findViewById(R.id.title);
        cashsum =findViewById(R.id.Cashsum);
        title.setText(name+" Account");
        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        recyclerView = (RecyclerView)findViewById(R.id.buyeraccount_recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        msg.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("PayHistory").child(venderName).child(method).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    paymentMethodModel = snapshot.getValue(PaymentMethodModel.class);
                    if (paymentMethodModel != null) {
                        msg.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        arrayList.add(paymentMethodModel);
                        int payment= Integer.parseInt(paymentMethodModel.getAmount());
                        total= total+payment;

                    }

                    cashsum.setText(total+".Rs");


                }
                progressDialog.dismiss();
                buyerBankAdapter = new BuyerBankAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(buyerBankAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                buyerBankAdapter.notifyDataSetChanged();
            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
