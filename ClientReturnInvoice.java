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

import com.example.AccountingAppAdmin.Adapter.ClientRetrunAdapter;
import com.example.AccountingAppAdmin.Adapter.RetrunAdapter;
import com.example.AccountingAppAdmin.DataModel.ClientRetrunModel;
import com.example.AccountingAppAdmin.DataModel.RetrunTemplate;
import com.example.AccountingAppAdmin.DataModel.RetunPurchaseModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientReturnInvoice extends AppCompatActivity {


    ArrayList<RetrunTemplate> arrayList = new ArrayList<>();
    ClientRetrunAdapter clientRetrunAdapter;
    RetrunTemplate retrunTemplate;
    ClientRetrunModel clientRetrunModel;
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
        setContentView(R.layout.activity_client_return_invoice);
        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        recyclerView = (RecyclerView) findViewById(R.id.retruninvoice_R);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
//        msg.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Client-Template").addListenerForSingleValueEvent(new ValueEventListener() {
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

                clientRetrunAdapter = new ClientRetrunAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(clientRetrunAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                clientRetrunAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
