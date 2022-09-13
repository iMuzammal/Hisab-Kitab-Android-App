package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.AccountingAppAdmin.DataModel.Counter;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerBillFilter extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    DatabaseReference databaseReference;
    CreateVenderModel createVenderModel;
    String selectVender;
    Button search;
    Counter counter;
    int count;
    String vendername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_bill_filter);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        spinner = (Spinner)findViewById(R.id.spiner_Category);
        search =findViewById(R.id.subcateogry);
        spinner.setOnItemSelectedListener(this);
        final List<String> categories = new ArrayList<String>();
        categories.add("--Select Vender--v");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplication(), VenderDashBoard.class);
                intent.putExtra("venderName",selectVender);
                startActivity(intent);
            }
        });
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Vender").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    createVenderModel = snapshot.getValue(CreateVenderModel.class);
                    categories.add(createVenderModel.getCounter()+"-  "+createVenderModel.getShopeName());



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selectVender = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
