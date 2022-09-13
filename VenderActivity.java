package com.example.AccountingAppAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class VenderActivity extends AppCompatActivity {

    RelativeLayout r1, r2;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vander_fragment_layout);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplication(),Dashboard.class);
                startActivity(intent);
            }
        });
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CreateVender.class);
                startActivity(intent);
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ViewVender.class);
                startActivity(intent);
            }
        });



    }
}
