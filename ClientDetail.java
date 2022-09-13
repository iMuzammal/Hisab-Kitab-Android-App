package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.AccountingAppAdmin.DataModel.AddClientModel;
import com.example.AccountingAppAdmin.DataModel.CreateVenderModel;
import com.example.AccountingAppAdmin.DataModel.OwnerProfileModel;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientDetail extends AppCompatActivity {

    Intent intent;
    String counter;
    ImageView logo;
    DatabaseReference databaseReference;
    Button delete, update;
    TextView shopname, name, contact, address, invoice, time, companyname, slogan, companycontact, companyadress;
    AddClientModel addClientModel;
    OwnerProfileModel ownerProfileModel;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);
        intent = getIntent();
        back = findViewById(R.id.back);
        companyname = findViewById(R.id.logoname);
        slogan = findViewById(R.id.slogon);
        companycontact = findViewById(R.id.ownernumber);
        companyadress = findViewById(R.id.Shopadress);
        logo = findViewById(R.id.ownerlogo);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.updateVender);
        shopname = findViewById(R.id.name);
        name = findViewById(R.id.clientname);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.adress);
        time = findViewById(R.id.timeanddate);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        invoice = findViewById(R.id.id);
        counter = intent.getExtras().getString("Counter");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ViewClient.class);
                startActivity(intent);
            }
        });

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Owner-Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ownerProfileModel = dataSnapshot.getValue(OwnerProfileModel.class);
                    if (ownerProfileModel != null) {
                        companyadress.setText(ownerProfileModel.getAddress());
                        companycontact.setText(ownerProfileModel.getContact());
                        slogan.setText(ownerProfileModel.getSlogon());
                        companyname.setText(ownerProfileModel.getCompanyName());
                        Glide.with(ClientDetail.this).load(ownerProfileModel.getLogo()).into(logo);


                    } else {

                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").child(counter).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    addClientModel = dataSnapshot.getValue(AddClientModel.class);
                    if (addClientModel != null) {

                        shopname.setText(addClientModel.getShopeName());
                        name.setText(addClientModel.getVenderName());
                        invoice.setText(addClientModel.getCounter());
                        address.setText(addClientModel.getAdress());
                        contact.setText(addClientModel.getContactNo());
                        time.setText(addClientModel.getTime_and_date());


                    } else {

                    }


                } else {


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").child(counter).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent = new Intent(getApplication(), SuccessfullDelete.class);
                        startActivity(intent);

                    }
                });


            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });


    }

    public void UpdateData() {

//        AlertDialog alertDialog = new AlertDialog.Builder(VenderDetail.this).create();
//     final AlertDialog.Builder alertDialog;
//        alertDialog = new android.support.v7.app.AlertDialog.Builder( getApplication() );
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ClientDetail.this);

        alertDialog.setTitle("Update Client");


        alertDialog.setMessage("Are you sure you want change this?");

//
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                updatechange();


            }


        });

//         Setting Negative "NO" Button
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

    private void updatechange() {


//        AlertDialog alertDialog = new AlertDialog.Builder(VenderDetail.this).create();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ClientDetail.this);

//        final android.support.v7.app.AlertDialog.Builder alertDialog;
//        alertDialog = new android.support.v7.app.AlertDialog.Builder( getApplication());
//

        alertDialog.setTitle("Update Vender");


        alertDialog.setMessage("Are you sure you want change this?");


        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.client_update, null);
        final EditText namef = (EditText) customView.findViewById(R.id.nameviewchange);
        final EditText namel = (EditText) customView.findViewById(R.id.lastnameviewchange);
        final EditText Cellno = (EditText) customView.findViewById(R.id.cellnoviewchange);
        final EditText adr = (EditText) customView.findViewById(R.id.adresschange);

        final Button updatedata = (Button) customView.findViewById(R.id.updated);

        alertDialog.setView(customView);


        databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").child(counter).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    addClientModel = dataSnapshot.getValue(AddClientModel.class);
                    if (addClientModel != null) {


                        namef.setText(addClientModel.getShopeName());
                        namel.setText(addClientModel.getVenderName());
                        Cellno.setText(addClientModel.getContactNo());
                        adr.setText(addClientModel.getAdress());


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updatedata.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.child(SharedPreferencesManager.getSomeStringValue(getApplication())).child("Create-Client").child(counter).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        addClientModel = dataSnapshot.getValue( AddClientModel.class );

                        dataSnapshot.getRef().child( "shopeName" ).setValue( namef.getText().toString() );
                        dataSnapshot.getRef().child( "venderName" ).setValue( namel.getText().toString() );
                        dataSnapshot.getRef().child( "contactNo" ).setValue( Cellno.getText().toString() );
                        dataSnapshot.getRef().child( "adress" ).setValue( adr.getText().toString() );

                        Toast.makeText(ClientDetail.this, "Vender Successfully updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplication(),ViewClient.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        } );


        alertDialog.setPositiveButton( "", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }


        } );

        alertDialog.setNegativeButton( "", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                dialog.cancel();
            }
        } );


        alertDialog.show();

    }
}