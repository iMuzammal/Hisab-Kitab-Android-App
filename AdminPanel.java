package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.AccountingAppAdmin.DataModel.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPanel extends AppCompatActivity {
    private ProgressDialog progressDialog;
    LinearLayout linearLayout;
    DatabaseReference databaseReference;
    ImageView imageView;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        progressDialog = new ProgressDialog(this);
        progressDialog.dismiss();
        imageView=findViewById(R.id.dp);
        name=findViewById(R.id.nameuser);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        linearLayout=findViewById(R.id.User);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplication(),Registration.class);
                startActivity(intent);
            }
        });
        databaseReference.child("Users").child("7flu6KPV9QXNp4tNIzMn8UNwgPt2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!= null){
                    UserProfile userProfile =dataSnapshot.getValue(UserProfile.class);
                    if(userProfile!=null){

                        name.setText(userProfile.getName());
                        Glide.with( AdminPanel.this).load( userProfile.getImage() ).into( imageView );

                    }
                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
