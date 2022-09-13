package com.example.AccountingAppAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.AccountingAppAdmin.Adapter.ViewPagerAdapter;
import com.example.AccountingAppAdmin.Fragment.MainCategoryFragment;
import com.example.AccountingAppAdmin.Fragment.SubCategoryFragment;
import com.example.AccountingAppAdmin.Fragment.SubCategorytwoFragment;
import com.google.android.material.tabs.TabLayout;

public class TabularActivity extends AppCompatActivity {
    private ViewPager viewPager;
    ImageView Back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categoryfragment_laayout);
        viewPager = findViewById(R.id.viewPager);
        Back=findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent= new Intent(getApplication(),Dashboard.class);
                startActivity(intent);
            }
        });

        addTabs(viewPager);
        ((TabLayout)findViewById(R.id.tabLayout)).setupWithViewPager( viewPager );




    }
    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MainCategoryFragment(), "Category");
        adapter.addFrag(new SubCategoryFragment(), "Sub Category");
        adapter.addFrag(new SubCategorytwoFragment(), "Sub Category");
        viewPager.setAdapter(adapter);
    }
}
