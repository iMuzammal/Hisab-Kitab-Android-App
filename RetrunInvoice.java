package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.AccountingAppAdmin.Adapter.PurchaseAdapter;
import com.example.AccountingAppAdmin.Adapter.RetrunAdapter;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.PurchaseTemplate;
import com.example.AccountingAppAdmin.DataModel.RetrunTemplate;
import com.example.AccountingAppAdmin.DataModel.RetunPurchaseModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RetrunInvoice extends AppCompatActivity {

    ArrayList<RetrunTemplate> arrayList = new ArrayList<>();
    RetrunAdapter retrunAdapter;
    RetrunTemplate retrunTemplate;
    RetunPurchaseModel retunPurchaseModel;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
     DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    ImageView back;
    TextView msg;
    String venderName;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrun_invoice);
        progressDialog = new ProgressDialog(RetrunInvoice.this, R.style.AppCompatAlertDialogStyle);
        recyclerView = (RecyclerView) findViewById(R.id.retruninvoice_R);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
//        msg.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Template").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    retrunTemplate = snapshot.getValue(RetrunTemplate.class);

                    if (retrunTemplate != null) {
                        progressDialog.dismiss();
//                        msg.setVisibility(View.GONE);

                        arrayList.add(retrunTemplate);
                    }
                }

                retrunAdapter = new RetrunAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(retrunAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                retrunAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
