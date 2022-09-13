package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;

import com.example.AccountingAppAdmin.Adapter.ClientPurchaseAdapter;
import com.example.AccountingAppAdmin.Adapter.PurchaseAdapter;
import com.example.AccountingAppAdmin.DataModel.ClientPurchaseTemplate;
import com.example.AccountingAppAdmin.DataModel.PurchaseTemplate;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientPurchaseInvoice extends AppCompatActivity {

    DatabaseReference databaseReference;
    ClientPurchaseAdapter clientPurchaseAdapter;
    RecyclerView recyclerView;
    ArrayList<ClientPurchaseTemplate> arrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    EditText search;
    ClientPurchaseTemplate clientPurchaseTemplate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_purchase_invoice);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView)findViewById(R.id.purchase_R);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        search =findViewById(R.id.search);
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Purchase-Client-Template").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    clientPurchaseTemplate = snapshot.getValue(ClientPurchaseTemplate.class);
                    if (clientPurchaseTemplate != null) {
//                        msg.setVisibility(View.GONE);

                        arrayList.add(clientPurchaseTemplate);
                    }
                }

                clientPurchaseAdapter = new ClientPurchaseAdapter(getApplication(), arrayList);
                recyclerView.setAdapter(clientPurchaseAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                clientPurchaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
