package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.AccountingAppAdmin.Adapter.AddporductAdapter;
import com.example.AccountingAppAdmin.Adapter.BuyerAdapter;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {

    ArrayList<InventryModel> arrayList = new ArrayList<>();
    AddporductAdapter addporductAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    InventryModel inventryModel;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    ImageView back;
    TextView msg;
    String venderName;
    Intent intent;
    Button addInventry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        back = (ImageView) findViewById(R.id.back);

        progressDialog = new ProgressDialog(AddProduct.this, R.style.AppCompatAlertDialogStyle);
        recyclerView = (RecyclerView) findViewById(R.id.addproduct_R);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        addInventry = findViewById(R.id.addproduct);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        addInventry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(), InventryManagementActivity.class);
                startActivity(intent);
            }
        });

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("InventrySystem").child("All-Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    inventryModel = snapshot.getValue(InventryModel.class);
                    if (inventryModel != null) {
//                        msg.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        arrayList.add(inventryModel);
                    }


                }
                progressDialog.dismiss();
                addporductAdapter = new AddporductAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(addporductAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                addporductAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
