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

import com.example.AccountingAppAdmin.Adapter.AddClientAdapter;
import com.example.AccountingAppAdmin.Adapter.VenderShowAdapter;
import com.example.AccountingAppAdmin.DataModel.AddClientModel;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewClient extends AppCompatActivity {

    ArrayList<AddClientModel> arrayList = new ArrayList<>();
    AddClientAdapter addClientAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AddClientModel addClientModel;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    ImageView back;
    TextView msg;
    String getcount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);
        back = (ImageView)findViewById(R.id.back);
        msg =findViewById(R.id.vendermsg);
        progressDialog = new ProgressDialog(ViewClient.this, R.style.AppCompatAlertDialogStyle);
        recyclerView = (RecyclerView)findViewById(R.id.vender_recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        msg.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    addClientModel = snapshot.getValue(AddClientModel.class);
                    if (addClientModel != null) {
                        msg.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        arrayList.add(addClientModel);
                    }


                }
                progressDialog.dismiss();


                addClientAdapter = new AddClientAdapter(getApplication(), arrayList
                        , new AddClientAdapter.Delete() {
                    @Override
                    public void pressdel(String counts) {
                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").child(counts).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Intent intent= new Intent(getApplication(),CreateCustomer.class);
                                startActivity(intent);

                            }
                        });




                    }
                });
                recyclerView.setAdapter(addClientAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                addClientAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
