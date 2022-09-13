package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AccountingAppAdmin.Adapter.VenderShowAdapter;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewVender extends AppCompatActivity {

    ArrayList<CreateVenderModel> arrayList = new ArrayList<>();
    VenderShowAdapter venderShowAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
   CreateVenderModel createVenderModel;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    ImageView back;
    TextView msg;
    String getcount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vender);
        back = (ImageView)findViewById(R.id.back);
        msg =findViewById(R.id.vendermsg);
        progressDialog = new ProgressDialog(ViewVender.this, R.style.AppCompatAlertDialogStyle);
        recyclerView = (RecyclerView)findViewById(R.id.vender_recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        msg.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Vender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    createVenderModel = snapshot.getValue(CreateVenderModel.class);
                    if (createVenderModel != null) {
                        msg.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        arrayList.add(createVenderModel);
                    }


                }
                progressDialog.dismiss();
                venderShowAdapter = new VenderShowAdapter(getApplication(), arrayList
                , new VenderShowAdapter.Delete() {
                    @Override
                    public void pressdel(String counts) {
                        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Vender").child(counts).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();

                                    Intent intent = new Intent( getApplication(),VenderActivity.class);
                                    startActivity(intent);



                                }




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });
                recyclerView.setAdapter(venderShowAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                venderShowAdapter.notifyDataSetChanged();
            }

//
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( getApplication(),VenderActivity.class);
                startActivity(intent);
            }
        });


    }




    public void UpdateData() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ViewVender.this);


        alertDialog.setTitle("Delete Vender");


        alertDialog.setMessage("Are you sure you want to Delete this?");


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplicationContext())).child("Create-Vender").child(getcount).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            venderShowAdapter.notifyDataSetChanged();
//
//                                    Intent intent = new Intent(getApplication(), SuccessfullDelete.class);
//                                    startActivity(intent);
//                                    loaddata();


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }


        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplication(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });


        // Showing Alert Message
        alertDialog.show();

    }

}
