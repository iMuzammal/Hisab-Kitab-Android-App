package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.AccountingAppAdmin.DataModel.AddClientModel;
import com.example.AccountingAppAdmin.DataModel.ClientRetrunModel;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.InventryModel;
import com.example.AccountingAppAdmin.DataModel.RetunPurchaseModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientViewRetunShowBill extends AppCompatActivity {

    Intent intent;
    String count, venderNode, discountotal;
    DatabaseReference databaseReference;
    AddClientModel addClientModel;
    TextView name, clientname, id, adress, contact, timeanddate2, gst, subtotal, finaltotal, gstview, discount;
    ArrayList<ClientRetrunModel> arrayList = new ArrayList<>();
    RetrunVeiwBillAdapter  retrunVeiwBillAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    long total;
    Button Retrun;
    String getingstock, node,category,subcate,subtosubcate, shopname,finalstock;
    InventryModel inventryModel;
    int allstock,intstock;
    ClientRetrunModel clientRetrunModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view_retun_show_bill);
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

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Create-Client").child(venderNode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    addClientModel = dataSnapshot.getValue(AddClientModel.class);
                    if (addClientModel != null) {
                        name.setText(addClientModel.getShopeName());
                        shopname=addClientModel.getShopeName();
                        clientname.setText(addClientModel.getVenderName());
                        adress.setText(addClientModel.getAdress());
                        contact.setText(addClientModel.getContactNo());


                    } else {

                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Retrun-Client-Billing").child("All-RetrunBill").child(count).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    clientRetrunModel = snapshot.getValue(ClientRetrunModel.class);
                    if (clientRetrunModel != null) {
//                        msg.setVisibility(View.GONE);

                        arrayList.add(clientRetrunModel);

                        id.setText(clientRetrunModel.getCount());
                        timeanddate2.setText(clientRetrunModel.getTimeandDate());
                        gst.setText(clientRetrunModel.getGst() + "%");
                        discount.setText(clientRetrunModel.getDiscount() + "%");
                        subtotal.setText(clientRetrunModel.getTotal() + "-/Rs");
                        getingstock = clientRetrunModel.getStock();
                        String node1 = clientRetrunModel.getCount();
                        String node2 = clientRetrunModel.getName();
                        node = node1 + "-  " + node2;
                        category=clientRetrunModel.getCategory();
                        subcate=clientRetrunModel.getSubcategory();
                        subtosubcate=clientRetrunModel.getSubcategoryto();



                    }


                }

                retrunVeiwBillAdapter = new RetrunVeiwBillAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(retrunVeiwBillAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                retrunVeiwBillAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



    }
}
