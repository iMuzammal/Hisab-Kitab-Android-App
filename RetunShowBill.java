package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.AccountingAppAdmin.Adapter.RetrunAdapter;
import com.example.AccountingAppAdmin.Adapter.ShowBillAdapter;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.PurchaseBillModel;
import com.example.AccountingAppAdmin.DataModel.RetunPurchaseModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RetunShowBill extends AppCompatActivity {

    Intent intent;
    String count, venderNode, discountotal;
    DatabaseReference databaseReference;
    CreateVenderModel createVenderModel;
    TextView name, clientname, id, adress, contact, timeanddate2, gst, subtotal, finaltotal, gstview, discount;
    ArrayList<RetunPurchaseModel> arrayList = new ArrayList<>();
    ReturnShowBillAdapter  returnShowBillAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    long total;
    Button Retrun;
    String getingstock, node,category,subcate,subtosubcate, shopname,finalstock;
    InventryModel inventryModel;
    int allstock,intstock;
    RetunPurchaseModel retunPurchaseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retun_show_bill);
        intent = getIntent();
        venderNode = intent.getStringExtra("venderNode");
        count = intent.getStringExtra("count");
        discountotal = intent.getStringExtra("discountotal");
        name = findViewById(R.id.name);
        clientname = findViewById(R.id.clientname);
        id = findViewById(R.id.id);
        adress = findViewById(R.id.adress);
        subtotal = findViewById(R.id.total);
        finaltotal = findViewById(R.id.finaltotal);
        finaltotal.setText(discountotal);
        contact = findViewById(R.id.contact);
        gst = findViewById(R.id.gst);
        timeanddate2 = findViewById(R.id.timeanddate2);
        Retrun = findViewById(R.id.retrunbill);
        discount = findViewById(R.id.discount);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.addMoreList_recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Create-Vender").child(venderNode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    createVenderModel = dataSnapshot.getValue(CreateVenderModel.class);
                    if (createVenderModel != null) {
                        name.setText(createVenderModel.getShopeName());
                        shopname=createVenderModel.getShopeName();
                        clientname.setText(createVenderModel.getVenderName());
                        adress.setText(createVenderModel.getAdress());
                        contact.setText(createVenderModel.getContactNo());


                    } else {

                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Billing").child("All-RetrunBill").child(count).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    retunPurchaseModel = snapshot.getValue(RetunPurchaseModel.class);
                    if (retunPurchaseModel != null) {
//                        msg.setVisibility(View.GONE);

                        arrayList.add(retunPurchaseModel);

                        id.setText(retunPurchaseModel.getCount());
                        timeanddate2.setText(retunPurchaseModel.getTimeandDate());
                        gst.setText(retunPurchaseModel.getGst() + "%");
                        discount.setText(retunPurchaseModel.getDiscount() + "%");
                        subtotal.setText(retunPurchaseModel.getTotal() + "-/Rs");
                        getingstock = retunPurchaseModel.getStock();
                        String node1 = retunPurchaseModel.getCount();
                        String node2 = retunPurchaseModel.getName();
                        node = node1 + "-  " + node2;
                        category=retunPurchaseModel.getCategory();
                        subcate=retunPurchaseModel.getSubcategory();
                        subtosubcate=retunPurchaseModel.getSubcategoryto();



                    }


                }

                returnShowBillAdapter = new ReturnShowBillAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(returnShowBillAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                returnShowBillAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



    }
}
